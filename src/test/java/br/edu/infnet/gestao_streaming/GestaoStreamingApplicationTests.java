package br.edu.infnet.gestao_streaming;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestRepositoryConfiguration.class)
class GestaoStreamingApplicationTests {

  @Test
  void contextLoads() {}
}
