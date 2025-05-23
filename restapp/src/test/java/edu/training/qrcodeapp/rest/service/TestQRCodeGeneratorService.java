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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration;
import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration.ErrorCode;
import edu.training.qrcodeapp.rest.service.validator.InputDataValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Basic tests for implementations of interface {@link QRCodeGeneratorService}.
 * <p>
 * Since the application context is loaded and there is no mocking, this should be
 * considered as integration tests. However, it will for now run as "unit tests".
 */
@SpringBootTest
public class TestQRCodeGeneratorService {

  private static final String INPUT_URL = "some URL to a file";

  @Autowired
  private QRCodeGeneratorService generatorService;

  /**
   * GIVEN valid input URL
   * WHEN generating a QR code out of it
   * THEN QR code as array of bytes is returned
   */
  @Test
  public void testBytesGeneration() throws ExceptionOnGeneration {

    byte[] generatedQRCode = generatorService.generateQRCodeBytes(INPUT_URL);

    assertNotNull(generatedQRCode);
    assertTrue(generatedQRCode.length > 0);
  }

  /**
   * GIVEN valid input URL and size
   * WHEN generating a QR code out of them
   * THEN QR code as array of bytes is returned
   */
  @Test
  public void testBytesGenerationWithSize() throws ExceptionOnGeneration {

    byte[] generatedQRCode = generatorService.generateQRCodeBytes(INPUT_URL, 200);

    assertNotNull(generatedQRCode);
    assertTrue(generatedQRCode.length > 0);
  }

  /**
   * GIVEN no input URL
   * WHEN generating a QR code with it
   * THEN generate fails
   * AND related exception is thrown
   */
  @Test
  public void testInputIsNull() throws ExceptionOnGeneration {

    Exception expectedException = assertThrows(ExceptionOnGeneration.class, () -> {
      generatorService.generateQRCodeBytes(null);
    });

    assertEquals(expectedException.getMessage(), ErrorCode.NULL_INPUT.getErrorDescription());
  }

  /**
   * GIVEN empty input URL
   * WHEN generating a QR code with it
   * THEN generate fails
   * AND related exception is thrown
   */
  @Test
  public void testInputIsEmpty() throws ExceptionOnGeneration {

    Exception expectedException = assertThrows(ExceptionOnGeneration.class, () -> {
      generatorService.generateQRCodeBytes("");
    });

    assertEquals(expectedException.getMessage(), ErrorCode.EMPTY_INPUT.getErrorDescription());
  }

  /**
   * GIVEN valid input URL
   * AND size smaller than valid lower limit
   * WHEN generating a QR code
   * THEN generate fails
   * AND related exception is thrown
   */
  @Test
  public void testInvalidMinimumSizeOnInput() throws ExceptionOnGeneration {

    Exception expectedException = assertThrows(ExceptionOnGeneration.class, () -> {
      generatorService.generateQRCodeBytes(INPUT_URL, 50);
    });

    String errorMessage = getInvalidSizeErrorMessage();

    assertEquals(expectedException.getMessage(), errorMessage);
  }

  /**
   * GIVEN valid input URL
   * AND size bigger than valid upper limit
   * WHEN generating a QR code
   * THEN generate fails
   * AND related exception is thrown
   */
  @Test
  public void testInvalidMaximumSizeOnInput() throws ExceptionOnGeneration {

    Exception expectedException = assertThrows(ExceptionOnGeneration.class, () -> {
      generatorService.generateQRCodeBytes(INPUT_URL, 600);
    });

    String errorMessage = getInvalidSizeErrorMessage();

    assertEquals(expectedException.getMessage(), errorMessage);
  }

  private String getInvalidSizeErrorMessage() {

    return String.format(ErrorCode.INVALID_SIZE.getErrorDescription(),
        InputDataValidator.MIN_SIZE, InputDataValidator.MAX_SIZE);
  }
}
