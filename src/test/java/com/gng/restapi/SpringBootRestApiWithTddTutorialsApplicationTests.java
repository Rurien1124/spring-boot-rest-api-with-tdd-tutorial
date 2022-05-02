package com.gng.restapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@ActiveProfiles(profiles = "test")
@SpringBootTest
class SpringBootRestApiWithTddTutorialsApplicationTests {

	@Test
	void contextLoads() {
	}

}
