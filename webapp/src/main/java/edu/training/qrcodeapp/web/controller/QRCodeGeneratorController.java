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

import edu.training.qrcodeapp.web.client.QRCodeClient;
import edu.training.qrcodeapp.web.form.InputDataForm;
import jakarta.validation.Valid;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Main web controller of the application.
 */
@Controller
public class QRCodeGeneratorController {

  // Model attribute for the generated QR code as array of bytes
  static final String QRCODE_BYTES = "qrcodeBytes";
  // Model boolean attribute to show or not the main page's form
  static final String SHOW_MAIN_PAGE = "showMainPage";
  // Model boolean attribute to display or not the QR code image
  static final String SHOW_QR_CODE = "showQrCode";
  // Model attribute with the name of the main page
  static final String MAIN_PAGE = "qrcode";

  // Main page's entry point name
  static final String MAIN_ENTRY_POINT = "/qrcode";
  // Entry point related to the form's action to generate QR code
  static final String GENERATE_ENTRY_POINT = "/generate";
  // Form's submit button name
  static final String SUBMIT_BUTTON_NAME = "action";
  // Form's submit button to clean up its fields
  static final String SUBMIT_CLEAR = "Clear";
  // Form's submit button to send data for QR code generation
  static final String SUBMIT_SEND = "Send";

  private final Logger logger = LoggerFactory.getLogger(QRCodeGeneratorController.class);

  @Autowired
  QRCodeClient qrCodeClient;

  /**
   * Shows the main page's form, in case the backend is up and running.
   * <p>
   * If not the case, then the main page will display a warning message.
   *
   * @param model Holder for attributes used in the page
   * @return Main page
   */
  @GetMapping(MAIN_ENTRY_POINT)
  public String showPage(InputDataForm inputDataForm, Model model) {

    if (qrCodeClient.isReady()) {

      logger.debug("Backend is ready to use");

      model.addAttribute(SHOW_MAIN_PAGE, true);
      model.addAttribute(SHOW_QR_CODE, false);

      return MAIN_PAGE;
    }

    logger.info("Warning that Backend is not ready to use");

    model.addAttribute(SHOW_MAIN_PAGE, false);
    return MAIN_PAGE;
  }

  /**
   * Operation posted for QR code generation, based on the data inputted in the main page's form.
   *
   * @param inputDataForm Input information for QR code generation
   * @param bindingResult Injected instance related to form validation
   * @param model         Holder for attributes used in the page
   * @return Main page
   */
  @PostMapping(value = GENERATE_ENTRY_POINT, params = SUBMIT_BUTTON_NAME + "=" + SUBMIT_SEND)
  public String generateQRCode(@Valid InputDataForm inputDataForm, BindingResult bindingResult,
      Model model) {

    model.addAttribute(SHOW_MAIN_PAGE, true);

    if (bindingResult.hasErrors()) {
      return MAIN_PAGE;
    }

    byte[] result = qrCodeClient.getQRCode(inputDataForm.getUrl(), inputDataForm.getSize());

    model.addAttribute(QRCODE_BYTES,
        "data:image/png;base64," + Base64.getEncoder().encodeToString(result));
    model.addAttribute(SHOW_QR_CODE, true);

    return MAIN_PAGE;
  }

  /**
   * Operation posted for cleaning the main page's form.
   *
   * @return Main page
   */
  @PostMapping(value = GENERATE_ENTRY_POINT, params = SUBMIT_BUTTON_NAME + "=" + SUBMIT_CLEAR)
  public String clearForm() {

    logger.debug("Cleaning up the page");

    return "redirect:" + MAIN_PAGE;
  }
}
