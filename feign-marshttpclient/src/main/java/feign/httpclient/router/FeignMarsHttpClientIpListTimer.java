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
import java.util.Set;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import feign.httpclient.router.consts.FeignMarsHttpClientConsts;
import feign.httpclient.router.util.HttpUtils;

public class FeignMarsHttpClientIpListTimer extends TimerTask {

  private static Logger logger = LoggerFactory.getLogger(FeignMarsHttpClientIpListTimer.class);

  private Gson gson = new Gson();

  @Override
  public void run() {
    Set<String> candidates =
        FeignMarsHttpClientRouter.getInstance().getFeignMarsHttpClientRouteCandidates();
    for (String candidate : candidates) {
      String ipList4String =
          HttpUtils.get(FeignMarsHttpClientConsts.FETCH_IPLIST_URL + candidate);
      if (ipList4String == null || FeignMarsHttpClientConsts.EMPYT_STRING.equals(ipList4String)) {
        continue;
      }

      logger.debug("【查询服务地址信息】service:{} ips:{}", candidate, ipList4String);

      List<String> ips = gson.fromJson(ipList4String, new TypeToken<List<String>>() {}.getType());

      FeignMarsHttpClientRefresher.getInstance().updateRouteMapByService(candidate, ips);
    }
  }
}
