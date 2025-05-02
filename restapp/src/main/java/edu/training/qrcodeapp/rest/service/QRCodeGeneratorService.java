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

import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration;

/**
 * Defines the QR code generation related operations.
 */
public interface QRCodeGeneratorService {

  // in pixels, define both height and width values of the generated image
  Integer DEFAULT_SIZE = 300;

  /**
   * Generates a QR code for the given input URL with a fixed, default size, i.e.,
   * height and width values in pixels.
   * <p>
   * This is a default implementation that will simply make use of other
   * specific implementations.
   *
   * @param url URL to be used as the input for the QR code generation
   * @return QR code as array of bytes
   * @throws ExceptionOnGeneration in case of any failure during generation, this exception
   *                               encapsulates the original error
   */
  default byte[] generateQRCodeBytes(String url) throws ExceptionOnGeneration {

    return generateQRCodeBytes(url, DEFAULT_SIZE);
  }

  /**
   * Generates a QR code for the given input URL and size, i.e., height and width values in pixels.
   *
   * @param url  URL to be used as the input for the QR code generation
   * @param size both height and width values in pixels
   * @return QR code as array of bytes
   * @throws ExceptionOnGeneration in case of any failure during generation, this exception
   *                               encapsulates the original error
   */
  byte[] generateQRCodeBytes(String url, int size) throws ExceptionOnGeneration;
}
