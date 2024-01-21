package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.demo.task.TaskService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@Autowired
    private TaskService taskService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		assertThat(taskService).isNotNull();
	}

	// @Test
	// void greetingShouldReturnDefaultMessage() throws Exception {
	// 	assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/tasks",
	// 			String.class)).contains("[]");
	// }
}
