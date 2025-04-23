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

package edu.training.qrcodeapp.rest.service;

import static edu.training.qrcodeapp.rest.service.Generator.DIMENSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestGenerator {

  @Mock
  private BitMatrix bitMatrix;

  @Mock
  private Writer qrCodeWriter;

  @Spy
  private Generator generator;

  @BeforeEach
  public void setup() throws WriterException {

    when(qrCodeWriter.encode("some URL to a file", BarcodeFormat.QR_CODE, DIMENSION,
        DIMENSION)).thenReturn(
        bitMatrix);

    generator.setWriter(qrCodeWriter);
  }

  @Test
  public void testBytesGeneration() throws WriterException, IOException {

    byte[] bytes = new byte[]{45, 32, 89};
    String inputData = "some URL to a file";

    doReturn(bytes).when(generator).createBytes(bitMatrix);

    byte[] generatedQRCode = generator.generateQRCodeBytes(inputData);

    assertEquals(generatedQRCode, bytes);
    verify(qrCodeWriter, atMostOnce()).encode(inputData, BarcodeFormat.QR_CODE, DIMENSION,
        DIMENSION);
    verify(generator, atMostOnce()).createBytes(bitMatrix);
  }
}
