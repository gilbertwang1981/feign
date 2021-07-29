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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import feign.httpclient.router.consts.FeignMarsHttpClientConsts;
import feign.httpclient.router.util.HttpUtils;

public class FeignMarsHttpClientRefresher {
  private static FeignMarsHttpClientRefresher instance = null;

  private static Logger logger = LoggerFactory.getLogger(FeignMarsHttpClientRefresher.class);

  private Gson gson = new Gson();

  private Map<String, List<String>> routeMap = new ConcurrentHashMap<>();

  private Map<String, String> defaultAddress = new ConcurrentHashMap<>();

  public static FeignMarsHttpClientRefresher getInstance() {
    if (instance == null) {
      synchronized (FeignMarsHttpClientRefresher.class) {
        if (instance == null) {
          instance = new FeignMarsHttpClientRefresher();
        }
      }
    }

    return instance;
  }

  public void initialize() {
    String serviceList4String =
        HttpUtils.get(FeignMarsHttpClientConsts.FETCH_SERVICELIST_URL);
    if (serviceList4String == null
        || FeignMarsHttpClientConsts.EMPYT_STRING.equals(serviceList4String)) {
      logger.error("从服务器获取服务域名列表失败,返回为空");

      return;
    }

    Map<String, String> services =
        gson.fromJson(serviceList4String, new TypeToken<Map<String, String>>() {}.getType());
    for (Map.Entry<String, String> entry : services.entrySet()) {
      logger.debug("【查询服务域名信息】service:" + entry.getKey() + " domain:" + entry.getValue());

      updateDefaultRouteByService(entry.getKey(), entry.getValue());
    }
  }

  public void start() {
    new Timer().schedule(new FeignMarsHttpClientIpListTimer(),
        FeignMarsHttpClientConsts.UPDATOR_TIMER_INIT,
        FeignMarsHttpClientConsts.UPDATOR_TIMER_INTERVAL);

    new Timer().schedule(new FeignMarsHttpClientDomainListTimer(),
        FeignMarsHttpClientConsts.UPDATOR_TIMER_INIT,
        FeignMarsHttpClientConsts.UPDATOR_TIMER_INTERVAL);
  }

  public List<String> getRouteMapByService(String service) {
    return routeMap.get(service) == null ? Collections.emptyList() : routeMap.get(service);
  }

  public String getDefaultRouteByService(String service) {
    return defaultAddress.get(service);
  }

  public void updateDefaultRouteByService(String service, String domain) {
    defaultAddress.put(service, domain);
  }

  public void updateRouteMapByService(String service, List<String> ips) {
    routeMap.put(service, ips);
  }
}
