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

import com.google.zxing.WriterException;
import edu.training.qrcodeapp.model.BytesArray;
import edu.training.qrcodeapp.rest.service.Generator;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Controller {

  @Autowired
  private Generator generatorService;

  @PostMapping("/qrcode/generate")
  public BytesArray getQRCodeBytes() {

    byte[] output;

    try {
      output = generatorService.generateQRCodeBytes("https://pdfobject.com/pdf/sample.pdf");
    }
    catch (WriterException | IOException e) {
      // TODO create proper error response
      throw new RuntimeException(e);
    }

    // TODO validate that generated output is not null

    BytesArray result = new BytesArray();
    result.setOutput(output);

    //createTestImage(output);

    return result;
  }

  /**
   * Temporary test method that will be removed soon.
   */
  private void createTestImage(byte[] output) {

    try (FileOutputStream fos = new FileOutputStream("qrcode.png")) {
      fos.write(output);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
