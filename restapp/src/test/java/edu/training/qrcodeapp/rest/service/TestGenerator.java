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

import edu.training.qrcodeapp.rest.service.exception.ExceptionOnGeneration;
import edu.training.qrcodeapp.rest.service.exception.ExceptionOnGeneration.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestGenerator {

  @Autowired
  private Generator generator;

  @Test
  public void testBytesGeneration() throws ExceptionOnGeneration {

    byte[] generatedQRCode = generator.generateQRCodeBytes("some URL to a file");

    assertNotNull(generatedQRCode);
    assertTrue(generatedQRCode.length > 0);
  }

  @Test
  public void testInputIsNull() throws ExceptionOnGeneration {

    Exception expectedException = assertThrows(ExceptionOnGeneration.class, () -> {
      generator.generateQRCodeBytes(null);
    });

    assertEquals(expectedException.getMessage(), ErrorCode.NULL_INPUT.getErrorDescription());
  }

  @Test
  public void testInputIsEmpty() throws ExceptionOnGeneration {

    Exception expectedException = assertThrows(ExceptionOnGeneration.class, () -> {
      generator.generateQRCodeBytes("");
    });

    assertEquals(expectedException.getMessage(), ErrorCode.EMPTY_INPUT.getErrorDescription());
  }
}
