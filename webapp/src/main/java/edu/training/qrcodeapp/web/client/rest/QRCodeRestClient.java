/*
 * MIT License
 *
 * Copyright (c) 2025 Cristiano Silva
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package edu.training.qrcodeapp.web.client.rest;

import edu.training.qrcodeapp.model.BytesArray;
import edu.training.qrcodeapp.model.InputURL;
import edu.training.qrcodeapp.model.Status;
import edu.training.qrcodeapp.model.Status.StatusEnum;
import edu.training.qrcodeapp.web.client.QRCodeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class QRCodeRestClient implements QRCodeClient {

  private static final String BASE_REST_APP_URL = "http://localhost:9090/qrcode";

  private static final String GENERATE = BASE_REST_APP_URL + "/generate";

  private static final String HEALTH = BASE_REST_APP_URL + "/health";

  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  @Override
  public boolean isAlive() {

    Status status;

    try {

      RestTemplate restTemplate = restTemplateBuilder.build();
      status = restTemplate.getForEntity(HEALTH, Status.class).getBody();
    }
    catch (RestClientException e) {
      return false;
    }

    if (status == null) {
      return false;
    }

    return StatusEnum.ALIVE.equals(status.getStatus());
  }

  @Override
  public byte[] getQRCode(InputURL inputURL) {

    RestTemplate restTemplate = restTemplateBuilder.build();

    ResponseEntity<BytesArray> response = restTemplate.postForEntity(
        GENERATE,
        inputURL, BytesArray.class);

    if (response.getBody() == null) {
      return new byte[0];
    }

    return response.getBody().getOutput();
  }
}
