package com.gng.restapi.events.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.restapi.events.model.Event;
import com.gng.restapi.events.model.EventDto;
import com.google.common.net.HttpHeaders;

/**
 * Event controller webMvc 테스트
 * @author gchyoo
 *
 */
@TestPropertySource(locations = "classpath:/application-test.yml") // 테스트 프로퍼티 파일 지정
@SpringBootTest // API 테스트 시에는 SpringBootTest 사용
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
	private ObjectMapper objectMapper;
	
	@Test
	@DisplayName("이벤트 등록 성공 테스트")
	public void createEventSuccess() throws Exception {
		// 요청을 위한 EventDto 객체 생성
		EventDto eventDto = EventDto.builder()
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
						.accept(MediaTypes.HAL_JSON) // Hypertext Application Language에 준하는 요청
						.characterEncoding(StandardCharsets.UTF_8) // 문자열 포맷 설정
						.contentType(MediaType.APPLICATION_JSON) // 컨텐츠 형식 설정
						.content(objectMapper.writeValueAsString(eventDto))
				)
				.andDo(print()) // 요청과 응답을 출력
				.andExpect(status().isCreated()) // 응답이 201 CREATED인지 확인
				.andExpect(header().exists(HttpHeaders.LOCATION)) // Location 헤더 확인
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)) // Content-Type 헤더 확인
				.andExpect(jsonPath("id").exists()) // JSON에 ID가 있는지 확인
				.andExpect(jsonPath("free").value(false)) // 가격이 있으므로 비즈니스 로직에서 false로 변경
				.andExpect(jsonPath("offline").value(true)) // 장소가 지정되어 있으므로 비즈니스 로직에서 true로 변경
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.query-events").exists())
				.andExpect(jsonPath("_links.update-event").exists());
	}
	
	@Test
	@DisplayName("이벤트 등록 실패 테스트(Unkwnown property bad request)")
	public void createEventUnknownBadRequest() throws Exception {
		Event event = Event.builder()
				.id(100)
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
				.free(true)
				.offline(false)
				.build();
		
		mockMvc.perform(post("/api/events/")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(event))
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaTypes.HAL_JSON)
				)
				.andDo(print())
				// spring.jackson.deserialization.fail-on-unknown-properties를 통해 Unknown property에 대한 오류를 발생시킴
				.andExpect(status().isBadRequest()); // 응답이 400 BAD_REQUEST인지 확인
	}

	@Test
	@DisplayName("이벤트 등록 실패 테스트(Empty parameter bad request)")
	public void createEventEmptyBadRequest() throws Exception {
		
		EventDto eventDto = EventDto.builder()
				.build();
		
		mockMvc.perform(post("/api/events")
						.accept(MediaTypes.HAL_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(eventDto))
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
	

	@Test
	@DisplayName("이벤트 등록 실패 테스트(Wrong parameter bad request)")
	public void createEventWrongBadRequest() throws Exception {

		EventDto eventDto = EventDto.builder()
				.name("Spring")
				.description("Spring REST API with TDD")
				.beginEnrollmentDateTime(LocalDateTime.of(2018,  11, 24, 14, 21))
				.closeEnrollmentDateTime(LocalDateTime.of(2018,  11, 23, 14, 21))
				.beginEventDateTime(LocalDateTime.of(2018,  11, 26, 14, 21))
				.endEventDateTime(LocalDateTime.of(2018,  11, 25, 14, 21))
				.basePrice(10000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역")
				.build();
		
		
		mockMvc.perform(post("/api/events")
						.accept(MediaTypes.HAL_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(eventDto))
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				// 오류 응답 json 확인
				.andExpect(jsonPath("[0].objectName").exists())
				.andExpect(jsonPath("[0].defaultMessage").exists())
				.andExpect(jsonPath("[0].rejectedValue").exists());
	}
}
