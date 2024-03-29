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

import java.util.List;

public class FeignMarsHttpClientRouteManager {
  private static FeignMarsHttpClientRouteManager instance = null;

  public static FeignMarsHttpClientRouteManager getInstance() {
    if (instance == null) {
      synchronized (FeignMarsHttpClientRouteManager.class) {
        if (instance == null) {
          instance = new FeignMarsHttpClientRouteManager();
        }
      }
    }

    return instance;
  }

  private FeignMarsHttpClientRouteManager() {
    FeignMarsHttpClientRefresher.getInstance().initialize();
    FeignMarsHttpClientRefresher.getInstance().start();
  }

  public List<String> getRouteMapByService(String service) {
    return FeignMarsHttpClientRefresher.getInstance().getRouteMapByService(service);
  }

  public String getDefaultRouteByService(String service) {
    return FeignMarsHttpClientRefresher.getInstance().getDefaultRouteByService(service);
  }
}
