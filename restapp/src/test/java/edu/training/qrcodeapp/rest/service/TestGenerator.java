package edu.training.qrcodeapp.rest.service;

import static edu.training.qrcodeapp.rest.service.Generator.DIMENSION;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.io.IOException;
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

  @Test
  public void testGeneration() throws WriterException, IOException {

    String inputData = "some URL to a file";
    String inputFileName = "fileName";

    generator.setWriter(qrCodeWriter);
    when(qrCodeWriter.encode(inputData, BarcodeFormat.QR_CODE, DIMENSION, DIMENSION)).thenReturn(
        bitMatrix);
    doNothing().when(generator).createFile(inputFileName, bitMatrix);

    generator.generateQRCode(inputData, inputFileName);

    verify(qrCodeWriter, atMostOnce()).encode(inputData, BarcodeFormat.QR_CODE, DIMENSION,
        DIMENSION);
    verify(generator, atMostOnce()).createFile(inputFileName, bitMatrix);
  }
}
