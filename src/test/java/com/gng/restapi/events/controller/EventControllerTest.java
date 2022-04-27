package com.gng.restapi.events.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Event controller webMvc �׽�Ʈ
 * @author gchyoo
 *
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class EventControllerTest {
	
	// ��û�� ���� MockMvc ��ü
	// DispatcherServlet�� �����ϰ�, SprinbootTest���� �ӵ��� �������� �����׽�Ʈ���� ����
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void createEvent() throws Exception {
		mockMvc.perform(post("/api/events/") // ��û URI
						.contentType(MediaType.APPLICATION_JSON) // Content type
						.accept(MediaTypes.HAL_JSON) // Hypertext Application Language�� ���ϴ� ��û
				)
				.andExpect(status().isCreated()); // ������ 201 CREATED���� Ȯ��
	}
}
