package com.gng.restapi.events.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Event 테스트
 * @author gchyoo
 *
 */
public class EventTest {
	
	private final static String EVENT_NAME = "Spring boot REST API with TDD";
	private final static String EVENT_DESCRIPTION = "REST API development with Spring";
	
	@Test
	@DisplayName("Event 객체 Builder 테스트")
	void hasBuilder() {
		// Event 객체의 Builder가 존재하는지 확인
		Event event = Event.builder()
				.name(EVENT_NAME)
				.description(EVENT_DESCRIPTION)
				.build();
		
		// Null이 아닌지 확인
		assertThat(event).isNotNull();
	}
	
	@Test
	@DisplayName("Event 객체 생성자 테스트")
	void hasConstructor() {
		// Given
		Event event = new Event();
		
		// When
		event.setName(EVENT_NAME);
		event.setDescription(EVENT_DESCRIPTION);
		
		// Then
		assertThat(event.getName()).isEqualTo(EVENT_NAME);
		assertThat(event.getDescription()).isEqualTo(EVENT_DESCRIPTION);
	}
}
