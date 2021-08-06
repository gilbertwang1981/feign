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

import java.util.Map;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import feign.httpclient.router.consts.FeignMarsHttpClientConsts;
import feign.httpclient.router.util.FeignEnvUtils;
import feign.httpclient.router.util.HttpUtils;

public class FeignMarsHttpClientDomainListTimer extends TimerTask {

  private static Logger logger = LoggerFactory.getLogger(FeignMarsHttpClientDomainListTimer.class);

  private Gson gson = new Gson();

  @Override
  public void run() {
    String serviceList4String =
        HttpUtils.get(FeignEnvUtils.getServiceListUrl());
    if (serviceList4String == null
        || FeignMarsHttpClientConsts.EMPYT_STRING.equals(serviceList4String)) {
      logger.error("从服务器获取服务域名列表失败,返回为空");

      return;
    }

    Map<String, String> services =
        gson.fromJson(serviceList4String, new TypeToken<Map<String, String>>() {}.getType());
    for (Map.Entry<String, String> entry : services.entrySet()) {
      logger.debug("【查询服务域名信息】service:" + entry.getKey() + " domain:" + entry.getValue());

      FeignMarsHttpClientRefresher.getInstance().updateDefaultRouteByService(entry.getKey(),
          entry.getValue());
    }
  }
}
