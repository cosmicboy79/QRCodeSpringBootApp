package edu.training.qrcodeapp.rest.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Component
public class Generator {

  static final Integer DIMENSION = 300;

  private Writer qrCodeWriter = new QRCodeWriter();

  public void generateQRCode(String data, String fileName) throws WriterException, IOException {
    BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, DIMENSION, DIMENSION);
    createFile(fileName, bitMatrix);
  }

  void createFile(String fileName, BitMatrix bitMatrix) throws IOException {
    Path path = FileSystems.getDefault().getPath(fileName);
    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
  }

  public void setWriter(Writer qrCodeWriter) {
    this.qrCodeWriter = qrCodeWriter;
  }
}
