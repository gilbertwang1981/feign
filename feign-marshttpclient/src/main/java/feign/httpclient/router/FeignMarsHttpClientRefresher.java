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
import feign.httpclient.router.consts.FeignMarsHttpClientConsts;

public class FeignMarsHttpClientRefresher {
  private static FeignMarsHttpClientRefresher instance = null;

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

  public void start() {
    new Timer().schedule(new FeignMarsHttpClientTimer(),
        FeignMarsHttpClientConsts.UPDATOR_TIMER_INIT,
        FeignMarsHttpClientConsts.UPDATOR_TIMER_INTERVAL);
  }

  public List<String> getRouteMapByService(String service) {
    return routeMap.get(service) == null ? Collections.emptyList() : routeMap.get(service);
  }

  public String getDefaultRouteByService(String service) {
    return defaultAddress.get(service);
  }

  public void updateRouteMapByService(String service, List<String> ips) {
    routeMap.put(service, ips);
  }
}
