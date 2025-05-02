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

package edu.training.qrcodeapp.rest.service.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration;
import edu.training.qrcodeapp.rest.service.QRCodeGeneratorService;
import edu.training.qrcodeapp.rest.service.validator.InputDataValidator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Component;

/**
 * "Zebra Crossing" ZXing implementation of {@link QRCodeGeneratorService}.
 *
 * @see <a href="https://github.com/zxing/zxing?tab=readme-ov-file#get-started-developing">ZXing</a>
 */
@Component
public class ZXingBasedGeneratorServiceImpl implements QRCodeGeneratorService {

  private final Writer qrCodeWriter = new QRCodeWriter();

  @Override
  public byte[] generateQRCodeBytes(String url, int size) throws ExceptionOnGeneration {

    InputDataValidator.INSTANCE.validateUrl(url);
    InputDataValidator.INSTANCE.validateSize(size);

    byte[] result;

    try {
      BitMatrix bitMatrix = encodeInputData(url, size);
      result = createBytes(bitMatrix);
    }
    catch (WriterException | IOException e) {
      throw new ExceptionOnGeneration(e);
    }

    return result;
  }

  BitMatrix encodeInputData(String url, int size) throws WriterException {

    return qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size);
  }

  byte[] createBytes(BitMatrix bitMatrix) throws IOException {

    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArray, new MatrixToImageConfig());

    return byteArray.toByteArray();
  }
}
