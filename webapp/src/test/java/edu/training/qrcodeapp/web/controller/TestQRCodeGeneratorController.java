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

package edu.training.qrcodeapp.web.controller;

import static edu.training.qrcodeapp.web.controller.QRCodeGeneratorController.GENERATE_ENTRY_POINT;
import static edu.training.qrcodeapp.web.controller.QRCodeGeneratorController.MAIN_ENTRY_POINT;
import static edu.training.qrcodeapp.web.controller.QRCodeGeneratorController.MAIN_PAGE;
import static edu.training.qrcodeapp.web.controller.QRCodeGeneratorController.QRCODE_BYTES;
import static edu.training.qrcodeapp.web.controller.QRCodeGeneratorController.SHOW_MAIN_PAGE;
import static edu.training.qrcodeapp.web.controller.QRCodeGeneratorController.SHOW_QR_CODE;
import static edu.training.qrcodeapp.web.controller.QRCodeGeneratorController.SUBMIT_BUTTON_NAME;
import static edu.training.qrcodeapp.web.controller.QRCodeGeneratorController.SUBMIT_CLEAR;
import static edu.training.qrcodeapp.web.controller.QRCodeGeneratorController.SUBMIT_SEND;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import edu.training.qrcodeapp.web.client.QRCodeClient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Basic tests for {@link QRCodeGeneratorController}.
 * <p>
 * Only this controller will be bootstrapped.
 */
@WebMvcTest(QRCodeGeneratorController.class)
public class TestQRCodeGeneratorController {

  private static final String QRCODE_GENERATION_INPUT_PARAMETER_NAME = "inputDataForm";
  private static final String QRCODE_GENERATION_ATTRIBUTE_SIZE = "size";
  private static final String QRCODE_GENERATION_ATTRIBUTE_URL = "url";

  private static final String VALID_INPUT_URL = "https://pdfobject.com/pdf/sample.pdf";
  private static final String INVALID_VALID_INPUT_URL = "pdfobject.com/pdf/sample.pdf";

  @MockitoBean
  QRCodeClient qrCodeClient;

  @Autowired
  private MockMvc mockMvc;

  /**
   * GIVEN Backend service
   * WHEN it states that it is ready for use
   * THEN main web page is show as expected
   */
  @Test
  public void testMainPageWhenBackendIsReady() throws Exception {

    when(qrCodeClient.isReady()).thenReturn(true);

    mockMvc.perform(get(MAIN_ENTRY_POINT)).andExpect(view().name(MAIN_PAGE))
        .andExpect(model().attributeExists(SHOW_MAIN_PAGE))
        .andExpect(model().attribute(SHOW_MAIN_PAGE, true))
        .andExpect(model().attributeExists(SHOW_QR_CODE))
        .andExpect(model().attribute(SHOW_QR_CODE, false));
  }

  /**
   * GIVEN Backend service
   * WHEN it states that it is not ready for use
   * THEN main web page will not show its form but a warning instead
   */
  @Test
  public void testMainPageWhenBackendIsNotReady() throws Exception {

    when(qrCodeClient.isReady()).thenReturn(false);

    mockMvc.perform(get(MAIN_ENTRY_POINT)).andExpect(view().name(MAIN_PAGE))
        .andExpect(model().attributeExists(SHOW_MAIN_PAGE))
        .andExpect(model().attribute(SHOW_MAIN_PAGE, false))
        .andExpect(model().attributeDoesNotExist(SHOW_QR_CODE));
  }

  /**
   * GIVEN Main page's form
   * WHEN hitting the button for cleaning it
   * THEN right operation redirects to main page for clean up
   */
  @Test
  public void testClearMainPage() throws Exception {

    mockMvc.perform(post(GENERATE_ENTRY_POINT).param(SUBMIT_BUTTON_NAME, SUBMIT_CLEAR))
        .andExpect(model().hasNoErrors())
        .andExpect(view().name("redirect:" + MAIN_PAGE));
  }

  /**
   * GIVEN Main page's form
   * WHEN hitting the button for generating a QR code
   * AND valid URL and size are provided as input
   * THEN Main page shows generated QR code
   */
  @Test
  public void testQRCodeGeneration() throws Exception {

    Integer inputSize = 400;

    when(qrCodeClient.getQRCode(VALID_INPUT_URL, inputSize)).thenReturn(
        "QR_code_image_as_bytes".getBytes(
            StandardCharsets.UTF_8));

    mockMvc.perform(post(GENERATE_ENTRY_POINT).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param(SUBMIT_BUTTON_NAME, SUBMIT_SEND)
            .param(QRCODE_GENERATION_ATTRIBUTE_URL, VALID_INPUT_URL)
            .param(QRCODE_GENERATION_ATTRIBUTE_SIZE, inputSize.toString()))
        .andExpect(model().hasNoErrors())
        .andExpect(model().attributeExists(SHOW_MAIN_PAGE))
        .andExpect(model().attribute(SHOW_MAIN_PAGE, true))
        .andExpect(model().attributeExists(SHOW_QR_CODE))
        .andExpect(model().attribute(SHOW_QR_CODE, true))
        .andExpect(model().attributeExists(QRCODE_BYTES))
        .andExpect(view().name(MAIN_PAGE));
  }

  /**
   * GIVEN Main page's form
   * WHEN hitting the button for generating a QR code
   * AND invalid URL is provided as input
   * THEN Main page shows related validation error
   */
  @Test
  public void testQRCodeGenerationWithInvalidURL() throws Exception {

    mockMvc.perform(post(GENERATE_ENTRY_POINT).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param(SUBMIT_BUTTON_NAME, SUBMIT_SEND)
            .param(QRCODE_GENERATION_ATTRIBUTE_URL, INVALID_VALID_INPUT_URL))
        .andExpect(model().hasErrors())
        .andExpect(model().errorCount(1))
        .andExpect(model().attributeHasFieldErrorCode(QRCODE_GENERATION_INPUT_PARAMETER_NAME,
            QRCODE_GENERATION_ATTRIBUTE_URL, Pattern.class.getSimpleName()))
        .andExpect(model().attributeExists(SHOW_MAIN_PAGE))
        .andExpect(model().attribute(SHOW_MAIN_PAGE, true))
        .andExpect(model().attributeDoesNotExist(SHOW_QR_CODE))
        .andExpect(model().attributeDoesNotExist(QRCODE_BYTES))
        .andExpect(view().name(MAIN_PAGE));
  }

  /**
   * GIVEN Main page's form
   * WHEN hitting the button for generating a QR code
   * AND no URL is provided as input
   * THEN Main page shows related validation error
   */
  @Test
  public void testQRCodeGenerationWithNoURL() throws Exception {

    mockMvc.perform(post(GENERATE_ENTRY_POINT).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param(SUBMIT_BUTTON_NAME, SUBMIT_SEND))
        .andExpect(model().hasErrors())
        .andExpect(model().errorCount(1))
        .andExpect(model().attributeHasFieldErrorCode(QRCODE_GENERATION_INPUT_PARAMETER_NAME,
            QRCODE_GENERATION_ATTRIBUTE_URL, NotNull.class.getSimpleName()))
        .andExpect(model().attributeExists(SHOW_MAIN_PAGE))
        .andExpect(model().attribute(SHOW_MAIN_PAGE, true))
        .andExpect(model().attributeDoesNotExist(SHOW_QR_CODE))
        .andExpect(model().attributeDoesNotExist(QRCODE_BYTES))
        .andExpect(view().name(MAIN_PAGE));
  }

  /**
   * GIVEN Main page's form
   * WHEN hitting the button for generating a QR code
   * AND blank URL is provided as input
   * THEN Main page shows related validation error
   */
  @Test
  public void testQRCodeGenerationWithBlankURL() throws Exception {

    mockMvc.perform(post(GENERATE_ENTRY_POINT).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param(SUBMIT_BUTTON_NAME, SUBMIT_SEND).param(QRCODE_GENERATION_ATTRIBUTE_URL, "    "))
        .andExpect(model().hasErrors())
        .andExpect(model().errorCount(1))
        .andExpect(model().attributeHasFieldErrorCode(QRCODE_GENERATION_INPUT_PARAMETER_NAME,
            QRCODE_GENERATION_ATTRIBUTE_URL, Pattern.class.getSimpleName()))
        .andExpect(model().attributeExists(SHOW_MAIN_PAGE))
        .andExpect(model().attribute(SHOW_MAIN_PAGE, true))
        .andExpect(model().attributeDoesNotExist(SHOW_QR_CODE))
        .andExpect(model().attributeDoesNotExist(QRCODE_BYTES))
        .andExpect(view().name(MAIN_PAGE));
  }

  /**
   * GIVEN Main page's form
   * WHEN hitting the button for generating a QR code
   * AND valid URL is provided as input
   * AND too small size is provide as input
   * THEN Main page shows related validation error
   */
  @Test
  public void testQRCodeGenerationWithInvalidMinimumSize() throws Exception {

    mockMvc.perform(post(GENERATE_ENTRY_POINT).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param(SUBMIT_BUTTON_NAME, SUBMIT_SEND)
            .param(QRCODE_GENERATION_ATTRIBUTE_URL, VALID_INPUT_URL)
            .param(QRCODE_GENERATION_ATTRIBUTE_SIZE, String.valueOf(99)))
        .andExpect(model().hasErrors())
        .andExpect(model().errorCount(1))
        .andExpect(model().attributeHasFieldErrorCode(QRCODE_GENERATION_INPUT_PARAMETER_NAME,
            QRCODE_GENERATION_ATTRIBUTE_SIZE, Min.class.getSimpleName()))
        .andExpect(model().attributeExists(SHOW_MAIN_PAGE))
        .andExpect(model().attribute(SHOW_MAIN_PAGE, true))
        .andExpect(model().attributeDoesNotExist(SHOW_QR_CODE))
        .andExpect(model().attributeDoesNotExist(QRCODE_BYTES))
        .andExpect(view().name(MAIN_PAGE));
  }

  /**
   * GIVEN Main page's form
   * WHEN hitting the button for generating a QR code
   * AND valid URL is provided as input
   * AND too big size is provide as input
   * THEN Main page shows related validation error
   */
  @Test
  public void testQRCodeGenerationWithInvalidMaximumSize() throws Exception {

    mockMvc.perform(post(GENERATE_ENTRY_POINT).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param(SUBMIT_BUTTON_NAME, SUBMIT_SEND)
            .param(QRCODE_GENERATION_ATTRIBUTE_URL, VALID_INPUT_URL)
            .param(QRCODE_GENERATION_ATTRIBUTE_SIZE, String.valueOf(501)))
        .andExpect(model().hasErrors())
        .andExpect(model().errorCount(1))
        .andExpect(model().attributeHasFieldErrorCode(QRCODE_GENERATION_INPUT_PARAMETER_NAME,
            QRCODE_GENERATION_ATTRIBUTE_SIZE, Max.class.getSimpleName()))
        .andExpect(model().attributeExists(SHOW_MAIN_PAGE))
        .andExpect(model().attribute(SHOW_MAIN_PAGE, true))
        .andExpect(model().attributeDoesNotExist(SHOW_QR_CODE))
        .andExpect(model().attributeDoesNotExist(QRCODE_BYTES))
        .andExpect(view().name(MAIN_PAGE));
  }
}
