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
import edu.training.qrcodeapp.model.InputURL;
import edu.training.qrcodeapp.model.Status;
import edu.training.qrcodeapp.model.Status.StatusEnum;
import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration;
import edu.training.qrcodeapp.rest.service.QRCodeGeneratorService;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/qrcode")
public class QRCodeGeneratorController {

  @Autowired
  private QRCodeGeneratorService generatorService;

  @GetMapping("/health")
  public ResponseEntity<Status> getHealthStatus() {

    Status status = new Status();

    if (generatorService == null) {

      status.status(StatusEnum.UNAVAILABLE);
      return new ResponseEntity<>(status, HttpStatus.SERVICE_UNAVAILABLE);
    }

    status.setStatus(StatusEnum.READY);
    return new ResponseEntity<>(status, HttpStatus.OK);
  }

  @PostMapping("/generate")
  public ResponseEntity<?> getQRCodeBytes(@RequestBody InputURL inputURL) {

    byte[] output;

    try {
      output = generateOutput(inputURL);
    }
    catch (ExceptionOnGeneration e) {
      return new ResponseEntity<>(createError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    BytesArray result = new BytesArray();
    result.setOutput(output);

    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }

  private byte[] generateOutput(InputURL inputURL) throws ExceptionOnGeneration {

    if (Objects.isNull(inputURL.getSize())) {
      return generatorService.generateQRCodeBytes(inputURL.getUrl());
    }

    return generatorService.generateQRCodeBytes(inputURL.getUrl(), inputURL.getSize());
  }

  @NotNull
  private Error createError(String message) {

    Error error = new Error();
    error.setMessage(message);
    return error;
  }
}
