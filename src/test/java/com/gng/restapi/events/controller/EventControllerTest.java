package com.gng.restapi.events.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.restapi.events.model.Event;

/**
 * Event controller webMvc 테스트
 * @author gchyoo
 *
 */
@TestPropertySource(locations = "classpath:/application-test.yml") // 테스트 프로퍼티 파일 지정
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false) // Spring security filter 비활성화
@ExtendWith(MockitoExtension.class)
@DisplayName("EventController 테스트")
public class EventControllerTest {
	
	// 요청을 위한 MockMvc 객체
	// DispatcherServlet을 포함하고, SprinbootTest보다 속도가 빠르지만 단위테스트보다 느림
	@Autowired
	private MockMvc mockMvc;
	
	// JSON string 출력을 위한 ObjectMapper 의존성 주입
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	@DisplayName("이벤트 등록 테스트")
	public void createEvent() throws Exception {
		// 요청을 위한 Event 객체 생성
		Event event = Event.builder()
				.name("Spring")
				.description("Spring REST API with TDD")
				.beginEnrollmentDateTime(LocalDateTime.of(2018,  11, 23, 14, 21))
				.closeEnrollmentDateTime(LocalDateTime.of(2018,  11, 24, 14, 21))
				.beginEventDateTime(LocalDateTime.of(2018,  11, 25, 14, 21))
				.endEventDateTime(LocalDateTime.of(2018,  11, 26, 14, 21))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역")
				.build();
		
		mockMvc.perform(post("/api/events/") // 요청 URI
						.contentType(MediaType.APPLICATION_JSON) // 컨텐츠 형식 설정
						.characterEncoding(StandardCharsets.UTF_8) // 문자열 포맷 설정
						.accept(MediaTypes.HAL_JSON) // Hypertext Application Language에 준하는 요청
						.content(objectMapper.writeValueAsString(event))
				)
				.andDo(print()) // 요청과 응답을 출력
				.andExpect(status().isCreated()) // 응답이 201 CREATED인지 확인
				.andExpect(jsonPath("id").exists()); // JSON에 ID가 있는지 확인
	}
}
