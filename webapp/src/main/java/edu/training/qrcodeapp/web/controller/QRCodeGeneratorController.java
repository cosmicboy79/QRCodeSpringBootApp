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

import edu.training.qrcodeapp.model.InputData;
import edu.training.qrcodeapp.web.client.QRCodeClient;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Main web controller of the application.
 */
@Controller
public class QRCodeGeneratorController {

  private final Logger logger = LoggerFactory.getLogger(QRCodeGeneratorController.class);

  // Model attribute for the generated QR code as array of bytes
  private static final String QRCODE_BYTES = "qrcodeBytes";
  // Model attribute for the input information for QR code generation
  private static final String INPUT_MODEL = "inputData";
  // Model boolean attribute to show or not the main page's form
  private static final String SHOW_MAIN_PAGE = "showMainPage";
  // Model boolean attribute to display or not the QR code image
  private static final String SHOW_QR_CODE = "showQrCode";
  // Model attribute with the name of the main page
  private static final String MAIN_PAGE = "qrcode";

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
  @GetMapping("/qrcode")
  public String showPage(Model model) {

    if (qrCodeClient.isReady()) {

      logger.info("Warning that Backend is not ready to use");

      model.addAttribute(INPUT_MODEL, new InputData());
      model.addAttribute(SHOW_MAIN_PAGE, true);
      model.addAttribute(SHOW_QR_CODE, false);

      return MAIN_PAGE;
    }

    model.addAttribute(SHOW_MAIN_PAGE, false);
    return MAIN_PAGE;
  }

  /**
   * Operation posted for QR code generation, based on the data inputted in the main page's form.
   *
   * @param model     Holder for attributes used in the page
   * @param inputData Input information for QR code generation
   * @return Main page
   */
  @PostMapping(value = "/generate", params = "action=Send")
  public String generateQRCode(Model model, @ModelAttribute(INPUT_MODEL) InputData inputData) {

    byte[] result = qrCodeClient.getQRCode(inputData);

    model.addAttribute(QRCODE_BYTES,
        "data:image/png;base64," + Base64.getEncoder().encodeToString(result));
    model.addAttribute(INPUT_MODEL, inputData);
    model.addAttribute(SHOW_MAIN_PAGE, true);
    model.addAttribute(SHOW_QR_CODE, true);

    return MAIN_PAGE;
  }

  /**
   * Operation posted for cleaning the main page's form.
   *
   * @param model Holder for attributes used in the page
   * @return Main page
   */
  @PostMapping(value = "/generate", params = "action=Clear")
  public String clearPage(Model model) {

    logger.debug("Cleaning up the page");

    model.addAttribute(INPUT_MODEL, new InputData());
    model.addAttribute(SHOW_MAIN_PAGE, true);
    model.addAttribute(SHOW_QR_CODE, false);

    return MAIN_PAGE;
  }
}
