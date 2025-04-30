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

package edu.training.qrcodeapp.rest.service.validator;

import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration;
import edu.training.qrcodeapp.rest.exception.ExceptionOnGeneration.ErrorCode;

public enum InputValidator {

  INSTANCE;

  public static final int MIN_SIZE = 100;
  public static final int MAX_SIZE = 500;

  public void validate(String data, int size) throws ExceptionOnGeneration {

    if (size < MIN_SIZE || size > MAX_SIZE) {
      String errorDescription = String.format(ErrorCode.INVALID_SIZE.getErrorDescription(),
          MIN_SIZE, MAX_SIZE);
      throw new ExceptionOnGeneration(errorDescription);
    }

    if (data == null) {
      throw new ExceptionOnGeneration(ErrorCode.NULL_INPUT);
    }

    if (data.isBlank()) {
      throw new ExceptionOnGeneration((ErrorCode.EMPTY_INPUT));
    }
  }
}
