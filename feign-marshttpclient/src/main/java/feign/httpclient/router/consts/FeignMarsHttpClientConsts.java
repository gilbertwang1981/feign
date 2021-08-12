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
package feign.httpclient.router.consts;

public interface FeignMarsHttpClientConsts {
  public final static String DEFAULT_ROUTE = "FEIGN_CLIENT_DEFAULT_ROUTE";

  public static final Long UPDATOR_TIMER_INTERVAL = 5000L;
  public static final Long UPDATOR_TIMER_INIT = 0L;

  public static final String EMPYT_STRING = "";

  public static final String FETCH_IPLIST_URL =
      "http://message-center-statis.int.chuxingyouhui.com/service/monitor/get_ips_by_service?service=";

  public static final String FETCH_SERVICELIST_URL =
      "http://message-center-statis.int.chuxingyouhui.com/service/monitor/get_service_list";

  public static final String FETCH_FLOWSOLUTION_URL =
      "http://message-center-statis.int.chuxingyouhui.com/service/monitor/get_weight_by_service?service=";

  public static final Integer CONNECT_EXCEPTION_CODE = 500;

  public static final Integer HTTP_STATUS_CODE_SUCCESS = 200;
}
