package edu.training.qrcodeapp.rest;

import com.google.zxing.WriterException;
import edu.training.qrcodeapp.rest.service.Generator;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//public class QRCodeGeneratorRestApp implements CommandLineRunner {
public class QRCodeGeneratorRestApp {

  @Autowired
  Generator generator;

  public static void main(String[] args) {
    SpringApplication.run(QRCodeGeneratorRestApp.class, args);
  }

  //@Override
  public void run(String... args) {
    String url = "https://drive.google.com/file/d/14nwJ13Q4m4nNrQ0bveye8xKdn9cq4RQx/view?usp=share_link";
    String fileName = "qrcode.png";

    try {
      byte[] output = generator.generateQRCodeBytes(url);
      System.out.println("QR Code generated and saved to: " + fileName);
    } catch (WriterException | IOException e) {
      System.err.println("Error generating QR Code: " + e.getMessage());
    }
  }
}
