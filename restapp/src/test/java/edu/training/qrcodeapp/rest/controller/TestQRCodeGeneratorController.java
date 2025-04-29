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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.training.qrcodeapp.model.InputURL;
import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration;
import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration.ErrorCode;
import edu.training.qrcodeapp.rest.service.QRCodeGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = QRCodeGeneratorController.class)
public class TestQRCodeGeneratorController {

  private static final String QRCODE_GENERATION_PATH = "/qrcode/generate";

  @MockitoBean
  QRCodeGeneratorService generatorService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testGenerateQrCode() throws Exception {

    when(generatorService.generateQRCodeBytes(anyString())).thenReturn(new byte[]{34, 56, 102});

    InputURL inputURL = new InputURL();
    inputURL.setUrl("https://pdfobject.com/pdf/sample.pdf");

    mockMvc.perform(
            post(QRCODE_GENERATION_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(inputURL.toJson()))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.output", is("Ijhm")));
  }

  @Test
  public void testBadRequestWhenEmptyInputURL() throws Exception {

    InputURL inputURL = new InputURL();
    inputURL.setUrl("");

    doThrow(new ExceptionOnGeneration(ErrorCode.EMPTY_INPUT)).when(generatorService)
        .generateQRCodeBytes(inputURL.getUrl());

    mockMvc.perform(
            post("/qrcode/generate").contentType(MediaType.APPLICATION_JSON).content(inputURL.toJson()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is(ErrorCode.EMPTY_INPUT.getErrorDescription())));
  }

  @Test
  public void testBadRequestWhenNullInputURL() throws Exception {

    InputURL inputURL = new InputURL();
    inputURL.setUrl(null);

    doThrow(new ExceptionOnGeneration(ErrorCode.NULL_INPUT)).when(generatorService)
        .generateQRCodeBytes(inputURL.getUrl());

    mockMvc.perform(
            post("/qrcode/generate").contentType(MediaType.APPLICATION_JSON).content(inputURL.toJson()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is(ErrorCode.NULL_INPUT.getErrorDescription())));
  }
}
