/**
 * Copyright 2012-2021 The Feign Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package feign.httpclient.router;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import feign.Request;
import feign.httpclient.router.consts.FeignFlowSolutionType;
import feign.httpclient.router.consts.FeignMarsHttpClientConsts;
import feign.httpclient.router.vo.FeignMarsHttpClientFlowSolution;
import feign.httpclient.router.vo.FeignMarsHttpClientIPWeight;

public class FeignMarsHttpClientRouter {
  private static FeignMarsHttpClientRouter instance = null;

  private AtomicLong counter = new AtomicLong();

  private static final Logger logger = LoggerFactory.getLogger(FeignMarsHttpClientRouter.class);

  private Map<String, String> routeCandidates = new ConcurrentHashMap<>();

  public static FeignMarsHttpClientRouter getInstance() {
    if (instance == null) {
      synchronized (FeignMarsHttpClientRouter.class) {
        if (instance == null) {
          instance = new FeignMarsHttpClientRouter();
        }
      }
    }

    return instance;
  }

  private FeignMarsHttpClientRouter() {
    counter.set(0L);
  }

  // the range of the weight is between 0 and 100;
  private Integer getWeight(String sourceIp, List<FeignMarsHttpClientIPWeight> weights) {
    for (FeignMarsHttpClientIPWeight weight : weights) {
      if (sourceIp.equals(weight.getIp())) {
        return weight.getWeight();
      }
    }

    return 0;
  }

  private List<String> getTargetIpList(List<String> source, String service) {
    FeignMarsHttpClientFlowSolution solution =
        FeignMarsHttpClientRefresher.getInstance().getFlowSolution(service);
    if (solution != null && solution
        .getFlowType() == FeignFlowSolutionType.FEIGN_SOLUTION_NONE_DOMAIN_TYPE.getType()) {
      List<String> target = new ArrayList<>();
      for (String ip : source) {
        int total = (int) (getWeight(ip, solution.getIpWeightList()) / 10.0);
        for (int i = 0; i < total; i++) {
          target.add(ip);
        }

        logger.debug("{}的权重值为{}", ip, total);
      }

      return target.size() == 0 ? source : target;
    } else {
      return source;
    }
  }

  public String route(Request request, URI uri, boolean isRetry) {
    routeCandidates.put(uri.getAuthority(), FeignMarsHttpClientConsts.EMPYT_STRING);

    StringBuffer target = new StringBuffer();
    boolean domainSolution = false;
    if (!isRetry) {
      FeignMarsHttpClientFlowSolution solution =
          FeignMarsHttpClientRefresher.getInstance().getFlowSolution(uri.getAuthority());
      if (solution != null) {
        domainSolution =
            (solution.getFlowType() != null && solution
                .getFlowType() == FeignFlowSolutionType.FEIGN_SOLUTION_DOMAIN_TYPE.getType())
                    ? true
                    : false;
      }
    } else {
      domainSolution = true;
    }
    
    List<String> ips =
        FeignMarsHttpClientRouteManager.getInstance().getRouteMapByService(uri.getAuthority());

    logger.info("是否重试 {} , 是否使用域名 {} , ip列表大小{}", isRetry, domainSolution, ips.size());

    if (domainSolution) {
      target.append(FeignMarsHttpClientRouteManager.getInstance()
          .getDefaultRouteByService(uri.getAuthority()));
    } else if (ips.isEmpty()) {
      target.append(FeignMarsHttpClientRouteManager.getInstance()
          .getDefaultRouteByService(uri.getAuthority()));
    } else {
      counter.compareAndSet(Long.MAX_VALUE, 0L);

      List<String> targetIps = getTargetIpList(ips, uri.getAuthority());

      target.append(targetIps.get((int) (counter.getAndIncrement() % targetIps.size())));
    }

    if (FeignMarsHttpClientConsts.EMPYT_STRING.equals(target.toString())) {
      throw new RuntimeException("无法找到路由 : " + "[schema:" + uri.getScheme() + "]" + "[authority:"
          + uri.getAuthority() + "]"
          + "[path:" + uri.getRawPath() + "]");
    }

    logger.info("路由信息: [schema:" + uri.getScheme() + "]" + "[authority:" + uri.getAuthority() + "]"
        + "[path:" + uri.getRawPath() + "]" + "[target:" + target.toString() + "]");

    return uri.getScheme() + "://" + target.toString() + uri.getRawPath();
  }

  public Set<String> getFeignMarsHttpClientRouteCandidates() {
    return routeCandidates.keySet();
  }
}
