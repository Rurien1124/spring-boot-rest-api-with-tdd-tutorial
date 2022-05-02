package com.gng.restapi.events.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import com.gng.restapi.commons.controller.BaseControllerTest;
import com.gng.restapi.events.model.Event;
import com.gng.restapi.events.model.EventDto;
import com.gng.restapi.events.model.EventStatus;
import com.gng.restapi.events.repository.EventRepository;
import com.google.common.net.HttpHeaders;

/**
 * Event controller webMvc 테스트
 * @author gchyoo
 *
 */
@DisplayName("EventController 테스트")
public class EventControllerTest extends BaseControllerTest {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Nested
	@DisplayName("이벤트 등록")
	class CreateEvent {

		@Test
		@DisplayName("성공")
		public void ok() throws Exception {
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
			
			mockMvc.perform(post("/api/events") // 요청 URI
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
		@DisplayName("알 수 없는 요청")
		public void unknownRequest() throws Exception {
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
		@DisplayName("비어있는 요청")
		public void emptyRequest() throws Exception {
			
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
		@DisplayName("잘못된 입력값")
		public void invalidRequest() throws Exception {

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
					.andExpect(jsonPath("[0].rejectedValue").exists())
					;
		}
	}
	
	@Test
	@DisplayName("이벤트 페이징")
	void queryEvents() throws Exception {
		// Given
		IntStream.range(0, 30)
				.forEach(this::generateEvent);
		
		// When
		this.mockMvc.perform(get("/api/events")
						.param("page", "1")
						.param("size", "10")
						.param("sort", "name,DESC")
				)
		
		// Then
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("page").exists())
				.andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
				.andExpect(jsonPath("_links.self").exists())
				;
	}
	
	@Nested
	@DisplayName("이벤트 조회")
	class QueryEvent {
		@Test
		@DisplayName("성공")
		void ok() throws Exception {
			// Given
			Event event = generateEvent(100);
			
			// When
			mockMvc.perform(get("/api/events/{id}", event.getId()))
					
			// Then
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("name").exists())
					.andExpect(jsonPath("id").exists())
					.andExpect(jsonPath("_links.self").exists())
					;
		}
		
		@Test
		@DisplayName("존재하지 않는 ID")
		void notFound() throws Exception {
			// Given
			generateEvent(100);
			
			// When
			mockMvc.perform(get("/api/events/{id}", 1000))
					
			// Then
					.andDo(print())
					.andExpect(status().isNotFound())
					;
		}
	}
	
	@Nested
	@DisplayName("이벤트 수정")
	class updateEvent {
		@Test
		@DisplayName("성공")
		void ok() throws Exception {
			// Given
			Event event = generateEvent(100);
			
			EventDto eventDto = modelMapper.map(event, EventDto.class);
			eventDto.setName("Updated event");
			
			// When
			mockMvc.perform(patch("/api/events/{id}", event.getId())
							.content(objectMapper.writeValueAsString(eventDto))
							.accept(MediaTypes.HAL_JSON)
							.characterEncoding(StandardCharsets.UTF_8)
							.contentType(MediaType.APPLICATION_JSON)
					)
			
			// Then
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("name").value(eventDto.getName()))
					.andExpect(jsonPath("_links.self").exists())
					;
		}
		
		@Test
		@DisplayName("비어있는 요청")
		void emptyRequest() throws Exception {
			// Given
			Event event = generateEvent(100);
			
			EventDto eventDto = new EventDto();
			
			// When
			mockMvc.perform(patch("/api/events/{id}", event.getId())
							.content(objectMapper.writeValueAsString(eventDto))
							.accept(MediaTypes.HAL_JSON)
							.characterEncoding(StandardCharsets.UTF_8)
							.contentType(MediaType.APPLICATION_JSON)
					)
			
			// Then
					.andDo(print())
					.andExpect(status().isBadRequest())
					;
		}

		@Test
		@DisplayName("잘못된 입력값")
		void invalidRequest() throws Exception {
			// Given
			Event event = generateEvent(200);
			
			EventDto eventDto = modelMapper.map(event, EventDto.class);
			eventDto.setBasePrice(2000);
			eventDto.setMaxPrice(1000);
			
			// When
			mockMvc.perform(patch("/api/events/{id}", event.getId())
							.content(objectMapper.writeValueAsString(eventDto))
							.accept(MediaTypes.HAL_JSON)
							.characterEncoding(StandardCharsets.UTF_8)
							.contentType(MediaType.APPLICATION_JSON)
					)
			
			// Then
					.andDo(print())
					.andExpect(status().isBadRequest())
					;
		}

		@Test
		@DisplayName("존재하지 않는 ID")
		void notFound() throws Exception {
			// Given
			Event event = generateEvent(200);
			
			EventDto eventDto = modelMapper.map(event, EventDto.class);
			
			// When
			mockMvc.perform(patch("/api/events/2222")
							.content(objectMapper.writeValueAsString(eventDto))
							.accept(MediaTypes.HAL_JSON)
							.characterEncoding(StandardCharsets.UTF_8)
							.contentType(MediaType.APPLICATION_JSON)
					)
			
			// Then
					.andDo(print())
					.andExpect(status().isNotFound())
					;
		}
	}
	
	private Event generateEvent(int index) {
		Event event = Event.builder()
				.name("Spring_" + index)
				.description("Spring REST API with TDD_" + index)
				.beginEnrollmentDateTime(LocalDateTime.of(2018,  11, 23, 14, 21))
				.closeEnrollmentDateTime(LocalDateTime.of(2018,  11, 24, 14, 21))
				.beginEventDateTime(LocalDateTime.of(2018,  11, 25, 14, 21))
				.endEventDateTime(LocalDateTime.of(2018,  11, 26, 14, 21))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역")
				.free(false)
				.offline(true)
				.eventStatus(EventStatus.DRAFT)
				.build();
		
		Event newEvent = this.eventRepository.save(event);
		
		return newEvent;
	}
}
