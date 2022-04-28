package com.gng.restapi.events.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Event entity 테스트
 * @author gchyoo
 *
 */
@DisplayName("Event 테스트")
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

	@CsvSource({
		"0,0,true",
		"100,0,false",
		"0,100,false"
	})
	@ParameterizedTest(name = "{index} {displayName} message={0}")
	@DisplayName("Free 여부 테스트")
	void isFree(ArgumentsAccessor argumentsAccessor) {
		// Given
		Event event = Event.builder()
				.basePrice(argumentsAccessor.getInteger(0))
				.maxPrice(argumentsAccessor.getInteger(1))
				.build();
		
		// When
		event.update();
		
		// Then
		assertThat(event.isFree()).isEqualTo(argumentsAccessor.getBoolean(2));
	}

	@CsvSource({
		"강남역, true",
		", false"
	})
	@ParameterizedTest(name = "{index} {displayName} message={0}")
	@DisplayName("Offline 여부 테스트")
	void testOffline(ArgumentsAccessor argumentsAccessor) {
		// Given
		Event event = Event.builder()
				.location(argumentsAccessor.getString(0))
				.build();
		
		// When
		event.update();
		
		// Then
		assertThat(event.isOffline()).isEqualTo(argumentsAccessor.getBoolean(1));
	}
}
