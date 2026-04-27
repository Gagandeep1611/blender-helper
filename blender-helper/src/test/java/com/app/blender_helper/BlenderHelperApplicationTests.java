package com.app.blender_helper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"openai.api.key=test-key",
		"openai.api.model=test-model"
})
class BlenderHelperApplicationTests {

	@Test
	void contextLoads() {
	}

}
