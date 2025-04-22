package edu.training.qrcodeapp.rest.service;

import static edu.training.qrcodeapp.rest.service.Generator.DIMENSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doNothing;
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

  private final String INPUT_DATA = "some URL to a file";

  @Mock
  private BitMatrix bitMatrix;

  @Mock
  private Writer qrCodeWriter;

  @Spy
  private Generator generator;

  @BeforeEach
  public void setup() throws WriterException {

    when(qrCodeWriter.encode(INPUT_DATA, BarcodeFormat.QR_CODE, DIMENSION, DIMENSION)).thenReturn(
        bitMatrix);
    generator.setWriter(qrCodeWriter);
  }

  @Test
  public void testFileGeneration() throws WriterException, IOException {

    String inputFileName = "fileName";

    doNothing().when(generator).createFile(inputFileName, bitMatrix);

    generator.generateQRCodeFile(INPUT_DATA, inputFileName);

    verify(qrCodeWriter, atMostOnce()).encode(INPUT_DATA, BarcodeFormat.QR_CODE, DIMENSION,
        DIMENSION);
    verify(generator, atMostOnce()).createFile(inputFileName, bitMatrix);
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
