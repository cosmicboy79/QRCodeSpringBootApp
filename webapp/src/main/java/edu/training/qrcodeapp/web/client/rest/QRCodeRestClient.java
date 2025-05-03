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
import edu.training.qrcodeapp.model.InputData;
import edu.training.qrcodeapp.model.Status;
import edu.training.qrcodeapp.model.Status.StatusEnum;
import edu.training.qrcodeapp.web.client.QRCodeClient;
import edu.training.qrcodeapp.web.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * REST client implementation of {@link QRCodeClient}.
 */
@Service
public class QRCodeRestClient implements QRCodeClient {

  // TODO put it as a property in the application file
  private static final String BASE_REST_APP_URL = "/api/v1/qrcode";
  private static final String GENERATE = BASE_REST_APP_URL + "/generate";
  private static final String HEALTH = BASE_REST_APP_URL + "/health";

  private final Logger logger = LoggerFactory.getLogger(QRCodeRestClient.class);

  @Autowired
  private AppConfig appConfig;
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  @Override
  public boolean isReady() {

    logger.debug("Checking whether Backend is up and running or not");

    Status status;

    try {

      RestTemplate restTemplate = restTemplateBuilder.build();
      status = restTemplate.getForEntity(resolveFullAddress(HEALTH), Status.class)
          .getBody();
    }
    catch (RestClientException e) {
      logger.error("Backend is not up and running: {}", e.getMessage());
      return false;
    }

    if (status == null) {
      logger.error("Backend is not up and running");
      return false;
    }

    logger.info("Backend is up and running");
    return StatusEnum.READY.equals(status.getStatus());
  }

  @Override
  public byte[] getQRCode(InputData inputData) {

    logger.debug("Getting QR code for the given input: {}", inputData.toJson());

    RestTemplate restTemplate = restTemplateBuilder.build();

    ResponseEntity<BytesArray> response = restTemplate.postForEntity(
        resolveFullAddress(GENERATE),
        inputData, BytesArray.class);

    if (response.getBody() == null) {
      logger.error("QR code generation response with no body");
      return new byte[0];
    }

    logger.debug("QR code retrieved: {}", response.getBody().getOutput());
    return response.getBody().getOutput();
  }

  private String resolveFullAddress(String operation) {

    return appConfig.getBackendAddress() + operation;
  }
}
