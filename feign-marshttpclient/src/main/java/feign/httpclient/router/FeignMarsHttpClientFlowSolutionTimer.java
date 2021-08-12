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
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import feign.httpclient.router.consts.FeignMarsHttpClientConsts;
import feign.httpclient.router.util.FeignEnvUtils;
import feign.httpclient.router.util.HttpUtils;
import feign.httpclient.router.vo.FeignMarsHttpClientFlowSolution;

public class FeignMarsHttpClientFlowSolutionTimer extends TimerTask {

  private static Logger logger =
      LoggerFactory.getLogger(FeignMarsHttpClientFlowSolutionTimer.class);

  private Gson gson = new Gson();

  @Override
  public void run() {
    List<String> services = FeignMarsHttpClientRefresher.getInstance().getServices();
    for (String service : services) {
      String solution4String = HttpUtils.get(FeignEnvUtils.getFlowSolutionUrl() + service);
      if (solution4String == null
          || FeignMarsHttpClientConsts.EMPYT_STRING.equals(solution4String)) {
        continue;
      }

      logger.info("更新分流方案  {} {}", service, solution4String);

      FeignMarsHttpClientFlowSolution solution =
          gson.fromJson(solution4String, FeignMarsHttpClientFlowSolution.class);
      if (solution != null) {
        FeignMarsHttpClientRefresher.getInstance().updateFlowSolution(service, solution);
      }
    }
  }
}
