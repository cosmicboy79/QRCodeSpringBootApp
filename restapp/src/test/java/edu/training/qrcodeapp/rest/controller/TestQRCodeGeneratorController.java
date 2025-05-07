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

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.training.qrcodeapp.model.InputData;
import edu.training.qrcodeapp.model.Status.StatusEnum;
import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration;
import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration.ErrorCode;
import edu.training.qrcodeapp.rest.service.QRCodeGeneratorService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Basic tests for {@link QRCodeGeneratorController}.
 * <p>
 * There is mocking but the application context is loaded, so this should be,
 * technically, considered as integration tests. However, it will for now run as "unit tests".
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TestQRCodeGeneratorController {

  private static final String BASE_PATH = "/api/v1/qrcode";
  private static final String QRCODE_GENERATION_PATH = BASE_PATH + "/generate";
  private static final String QRCODE_HEALTH_PATH = BASE_PATH + "/health";

  @MockitoBean
  QRCodeGeneratorService generatorService;

  @Captor
  ArgumentCaptor<String> inputUrlArgumentCaptor;

  @Autowired
  private MockMvc mockMvc;

  /**
   * GIVEN that application is up and running
   * WHEN using REST GET <code>qrcode/health</code>
   * THEN REST operation returns "ready" status
   */
  @Test
  public void testHealthStatus() throws Exception {

    mockMvc.perform(get(QRCODE_HEALTH_PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is(StatusEnum.READY.getValue().toUpperCase())));
  }

  /**
   * GIVEN that application is up and running
   * WHEN using REST GET <code>qrcode/generate</code>
   * AND input data is valid
   * THEN REST response is HTTP 201
   * AND operation returns QR code as bytes
   */
  @Test
  public void testGenerateQrCode() throws Exception {

    when(generatorService.generateQRCodeBytes(anyString())).thenReturn(new byte[]{34, 56, 102});

    InputData inputData = new InputData();
    inputData.setUrl("https://pdfobject.com/pdf/sample.pdf");

    mockMvc.perform(
            post(QRCODE_GENERATION_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(inputData.toJson()))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.output", is("Ijhm")));

    checkInputOnExecution(inputData);
  }

  /**
   * GIVEN that application is up and running
   * WHEN using REST GET <code>qrcode/generate</code>
   * AND input URL is empty
   * THEN REST response is HTTP 400
   * AND operation returns related error message in the response
   */
  @Test
  public void testBadRequestWhenEmptyInputURL() throws Exception {

    InputData inputData = new InputData();
    inputData.setUrl("");

    doThrow(new ExceptionOnGeneration(ErrorCode.EMPTY_INPUT)).when(generatorService)
        .generateQRCodeBytes(inputData.getUrl());

    mockMvc.perform(
            post(QRCODE_GENERATION_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(inputData.toJson()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is(ErrorCode.EMPTY_INPUT.getErrorDescription())));

    checkInputOnExecution(inputData);
  }

  /**
   * GIVEN that application is up and running
   * WHEN using REST GET <code>qrcode/generate</code>
   * AND input URL is not sent (null input)
   * THEN REST response is HTTP 400
   * AND operation returns related error message in the response
   */
  @Test
  public void testBadRequestWhenNullInputURL() throws Exception {

    InputData inputData = new InputData();
    inputData.setUrl(null);

    doThrow(new ExceptionOnGeneration(ErrorCode.NULL_INPUT)).when(generatorService)
        .generateQRCodeBytes(inputData.getUrl());

    mockMvc.perform(
            post(QRCODE_GENERATION_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(inputData.toJson()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is(ErrorCode.NULL_INPUT.getErrorDescription())));

    checkInputOnExecution(inputData);
  }

  private void checkInputOnExecution(InputData inputData) throws ExceptionOnGeneration {

    verify(generatorService, atMostOnce()).generateQRCodeBytes(inputUrlArgumentCaptor.capture());

    assertEquals(inputData.getUrl(), inputUrlArgumentCaptor.getValue());
  }
}
