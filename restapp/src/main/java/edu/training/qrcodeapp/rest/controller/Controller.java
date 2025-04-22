package edu.training.qrcodeapp.rest.controller;

import com.google.zxing.WriterException;
import edu.training.qrcodeapp.model.BytesArray;
import edu.training.qrcodeapp.rest.service.Generator;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Controller {

  @Autowired
  private Generator generatorService;

  @PostMapping("/qrcode/generate")
  public BytesArray getQRCodeBytes() {

    byte[] output;

    try {
      output = generatorService.generateQRCodeBytes("https://pdfobject.com/pdf/sample.pdf");
    } catch (WriterException | IOException e) {
      throw new RuntimeException(e);
    }

    BytesArray result = new BytesArray();
    result.setOutput(output);

    // test
    /*
    try (FileOutputStream fos = new FileOutputStream("qrcode.png")) {
      fos.write(output);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    */

    return result;
  }
}
