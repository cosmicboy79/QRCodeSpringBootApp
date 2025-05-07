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

import static edu.training.qrcodeapp.rest.service.QRCodeGeneratorService.DEFAULT_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link ZXingBasedGeneratorServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class TestZXingBasedQRCodeGeneratorServiceImpl {

  @Spy
  ZXingBasedGeneratorServiceImpl generatorService;

  /**
   * GIVEN QR code generation service
   * AND valid input URL
   * WHEN an encoding problem occurs during QR code generation
   * THEN proper {@link ExceptionOnGeneration} is thrown
   */
  @Test
  public void testExceptionOnEncoding() throws WriterException {

    String errorMessage = "some crazy error when encoding";
    String inputData = "some URL to a file";

    doThrow(new WriterException(errorMessage)).when(generatorService)
        .encodeInputData(inputData, DEFAULT_SIZE);

    Exception expectedException = assertThrows(ExceptionOnGeneration.class,
        () -> generatorService.generateQRCodeBytes(inputData));

    assertEquals(errorMessage, expectedException.getMessage());
  }

  /**
   * GIVEN QR code generation service
   * AND valid input URL
   * WHEN an I/O problem occurs during QR code generation
   * THEN proper {@link ExceptionOnGeneration} is thrown
   */
  @Test
  public void testExceptionOnOutputStream() throws WriterException, IOException {

    String errorMessage = "some crazy IO error";
    String inputData = "some URL to a file";

    BitMatrix bitMatrix = mock(BitMatrix.class);

    doReturn(bitMatrix).when(generatorService).encodeInputData(inputData, DEFAULT_SIZE);
    doThrow(new IOException(errorMessage)).when(generatorService).createBytes(bitMatrix);

    Exception expectedException = assertThrows(ExceptionOnGeneration.class,
        () -> generatorService.generateQRCodeBytes(inputData));

    assertEquals(errorMessage, expectedException.getMessage());
  }
}
