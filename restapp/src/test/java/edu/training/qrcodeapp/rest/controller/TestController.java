package edu.training.qrcodeapp.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.training.qrcodeapp.rest.service.Generator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = Controller.class)
public class TestController {

  @MockitoBean
  Generator generatorService;
  @Autowired
  private MockMvc mockMvc;

  //@Test
  public void generateQrCode() throws Exception {
    mockMvc.perform(post("/qrcode/generate")).andExpect(status().isOk());
  }
}
