package com.gng.restapi.events.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity와 DTO를 분리함으로써 입력되지 않아야 하는 값을 제어할 수 있음
 * @author gchyoo
 *
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
	private String name;
	private String description;
	private String location;
	
	private LocalDateTime beginEnrollmentDateTime;
	private LocalDateTime closeEnrollmentDateTime;
	private LocalDateTime beginEventDateTime;
	private LocalDateTime endEventDateTime;

	private int basePrice;
	private int maxPrice;
	private int limitOfEnrollment;
}
