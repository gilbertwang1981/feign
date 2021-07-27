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

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
  private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

  private static CloseableHttpClient createDefault() {
    return HttpClientBuilder.create().build();
  }

  public static String get(String url) {
    try {
      CloseableHttpClient client = createDefault();
      HttpGet request = new HttpGet(url);
      HttpResponse response = client.execute(request);
      int code = response.getStatusLine().getStatusCode();
      if (code == HttpStatus.SC_OK) {
        String result = EntityUtils.toString(response.getEntity());
        return result;
      }
    } catch (IOException e) {
      logger.error("调用HTTP请求获取服务ip列表失败," + e.getMessage());
    }

    return null;
  }
}
