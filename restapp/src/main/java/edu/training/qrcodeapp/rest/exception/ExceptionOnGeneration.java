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

package edu.training.qrcodeapp.rest.exception;

public class ExceptionOnGeneration extends Exception {

  private String message;

  public ExceptionOnGeneration(Throwable cause) {

    super(cause);
    this.message = cause.getMessage();
  }

  public ExceptionOnGeneration(ErrorCode errorCode) {

    this.message = errorCode.getErrorDescription();
  }

  @Override
  public String getMessage() {

    return message;
  }

  public enum ErrorCode {

    NULL_INPUT("Input URL is null"),
    EMPTY_INPUT("Input URL is empty");

    private final String errorDescription;

    ErrorCode(String errorDescription) {

      this.errorDescription = errorDescription;
    }

    public String getErrorDescription() {

      return errorDescription;
    }
  }
}
