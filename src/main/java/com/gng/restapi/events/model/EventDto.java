package com.gng.restapi.events.model;

import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
	@NotEmpty
	private String name;
	@NotEmpty
	private String description;
	private String location;
	
	@NotNull
	private LocalDateTime beginEnrollmentDateTime;
	@NotNull
	private LocalDateTime closeEnrollmentDateTime;
	@NotNull
	private LocalDateTime beginEventDateTime;
	@NotNull
	private LocalDateTime endEventDateTime;
	
	@Min(0)
	private int basePrice;
	@Min(0)
	private int maxPrice;
	@Min(0)
	private int limitOfEnrollment;
}
