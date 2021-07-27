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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import feign.Request;
import feign.httpclient.router.consts.FeignMarsHttpClientConsts;

public class FeignMarsHttpClientRouter {
  private static FeignMarsHttpClientRouter instance = null;

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

  public String route(Request request, URI uri) {
    routeCandidates.put(uri.getAuthority(), FeignMarsHttpClientConsts.EMPYT_STRING);

    List<String> ips =
        FeignMarsHttpClientRouteManager.getInstance().getRouteMapByService(uri.getAuthority());
    StringBuffer target = new StringBuffer();
    if (ips.isEmpty()) {
      target.append(FeignMarsHttpClientRouteManager.getInstance()
          .getDefaultRouteByService(uri.getAuthority()));
    } else {
      target.append(ips.get(0));
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
