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
 * Event controller webMvc 테스트
 * @author gchyoo
 *
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class EventControllerTest {
	
	// 요청을 위한 MockMvc 객체
	// DispatcherServlet을 포함하고, SprinbootTest보다 속도가 빠르지만 단위테스트보다 느림
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void createEvent() throws Exception {
		mockMvc.perform(post("/api/events/") // 요청 URI
						.contentType(MediaType.APPLICATION_JSON) // Content type
						.accept(MediaTypes.HAL_JSON) // Hypertext Application Language에 준하는 요청
				)
				.andExpect(status().isCreated()); // 응답이 201 CREATED인지 확인
	}
}
