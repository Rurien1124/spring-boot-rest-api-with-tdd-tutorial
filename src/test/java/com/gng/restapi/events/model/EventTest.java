package com.gng.restapi.events.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Event �׽�Ʈ
 * @author gchyoo
 *
 */
public class EventTest {
	
	private final static String EVENT_NAME = "Spring boot REST API with TDD";
	private final static String EVENT_DESCRIPTION = "REST API development with Spring";
	
	@Test
	@DisplayName("Event ��ü Builder �׽�Ʈ")
	void hasBuilder() {
		// Event ��ü�� Builder�� �����ϴ��� Ȯ��
		Event event = Event.builder()
				.name(EVENT_NAME)
				.description(EVENT_DESCRIPTION)
				.build();
		
		// Null�� �ƴ��� Ȯ��
		assertThat(event).isNotNull();
	}
	
	@Test
	@DisplayName("Event ��ü ������ �׽�Ʈ")
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
