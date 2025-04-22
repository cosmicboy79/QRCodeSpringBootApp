package edu.training.qrcodeapp.rest.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Component
public class Generator {

  static final Integer DIMENSION = 300;

  private Writer qrCodeWriter = new QRCodeWriter();

  public void generateQRCodeFile(String data, String fileName) throws WriterException, IOException {
    BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, DIMENSION, DIMENSION);
    createFile(fileName, bitMatrix);
  }

  public byte[] generateQRCodeBytes(String data) throws WriterException, IOException {
    BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, DIMENSION, DIMENSION);
    return createBytes(bitMatrix);
  }

  byte[] createBytes(BitMatrix bitMatrix) throws IOException {
    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArray, new MatrixToImageConfig());
    return byteArray.toByteArray();
  }

  void createFile(String fileName, BitMatrix bitMatrix) throws IOException {
    Path path = FileSystems.getDefault().getPath(fileName);
    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
  }

  public void setWriter(Writer qrCodeWriter) {
    this.qrCodeWriter = qrCodeWriter;
  }
}
