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

package edu.training.qrcodeapp.rest.controller;

import edu.training.qrcodeapp.model.BytesArray;
import edu.training.qrcodeapp.model.Error;
import edu.training.qrcodeapp.model.InputData;
import edu.training.qrcodeapp.model.Status;
import edu.training.qrcodeapp.model.Status.StatusEnum;
import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration;
import edu.training.qrcodeapp.rest.service.QRCodeGeneratorService;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller of the REST layer.
 */
@RestController()
@RequestMapping("/api/v1/qrcode")
public class QRCodeGeneratorController {

  private final Logger logger = LoggerFactory.getLogger(QRCodeGeneratorController.class);

  @Autowired
  private QRCodeGeneratorService generatorService;

  /**
   * Checks whether the application is alive and ready to use or not.
   *
   * @return Health status encapsulated as {@link ResponseEntity}
   */
  @GetMapping("/health")
  public ResponseEntity<Status> getHealthStatus() {

    Status status = new Status();

    if (generatorService == null) {

      logger.error("Application is not ready.");

      status.status(StatusEnum.UNAVAILABLE);
      return new ResponseEntity<>(status, HttpStatus.SERVICE_UNAVAILABLE);
    }

    logger.info("Application is ready.");

    status.setStatus(StatusEnum.READY);
    return new ResponseEntity<>(status, HttpStatus.OK);
  }

  /**
   * Generates the QR code for the given input data.
   *
   * @param inputData Input data sent in the body of the request
   * @return {@link ResponseEntity} containing either the correct data or error information
   */
  @PostMapping("/generate")
  public ResponseEntity<?> generateQRCode(@RequestBody InputData inputData) {

    byte[] output;

    try {
      output = generateOutput(inputData);
    }
    catch (ExceptionOnGeneration e) {
      logger.error("QR Code generation failed");
      return new ResponseEntity<>(createError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    BytesArray result = new BytesArray();
    result.setOutput(output);

    logger.debug("QR Code generated");
    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }

  private byte[] generateOutput(InputData inputData) throws ExceptionOnGeneration {

    if (Objects.isNull(inputData.getSize())) {
      logger.debug("Generating QR Code with default size: {}", QRCodeGeneratorService.DEFAULT_SIZE);
      return generatorService.generateQRCodeBytes(inputData.getUrl());
    }

    logger.debug("Generating QR Code with size provided in the input: {}", inputData.getSize());
    return generatorService.generateQRCodeBytes(inputData.getUrl(), inputData.getSize());
  }

  private Error createError(String message) {

    Error error = new Error();
    error.setMessage(message);
    return error;
  }
}
