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
package feign.httpclient.router.util;

import feign.httpclient.router.consts.FeignMarsHttpClientConsts;

public class FeignEnvUtils {
  public static final String getIPListUrl() {
    String ipList = System.getenv("FEIGN_FETCH_IPLIST_URL");
    if (ipList == null || FeignMarsHttpClientConsts.EMPYT_STRING.equals(ipList)) {
      return FeignMarsHttpClientConsts.FETCH_IPLIST_URL;
    } else {
      return ipList;
    }
  }

  public static final String getServiceListUrl() {
    String serviceList = System.getenv("FEIGN_FETCH_SERVICELIST_URL");
    if (serviceList == null || FeignMarsHttpClientConsts.EMPYT_STRING.equals(serviceList)) {
      return FeignMarsHttpClientConsts.FETCH_SERVICELIST_URL;
    } else {
      return serviceList;
    }
  }

  public static final String getFlowSolutionUrl() {
    String flowSolution = System.getenv("FEIGN_FETCH_FLOWSOLUTION_URL");
    if (flowSolution == null || FeignMarsHttpClientConsts.EMPYT_STRING.equals(flowSolution)) {
      return FeignMarsHttpClientConsts.FETCH_FLOWSOLUTION_URL;
    } else {
      return flowSolution;
    }
  }
}
