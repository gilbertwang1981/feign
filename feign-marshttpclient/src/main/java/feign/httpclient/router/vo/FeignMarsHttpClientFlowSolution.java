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
package feign.httpclient.router.vo;

import java.util.Map;

public class FeignMarsHttpClientFlowSolution {
  private Integer flowType;
  private Map<String, String> ipWeightList;

  public Integer getFlowType() {
    return flowType;
  }

  public void setFlowType(Integer flowType) {
    this.flowType = flowType;
  }

  public Map<String, String> getIpWeightList() {
    return ipWeightList;
  }

  public void setIpWeightList(Map<String, String> ipWeightList) {
    this.ipWeightList = ipWeightList;
  }
}
