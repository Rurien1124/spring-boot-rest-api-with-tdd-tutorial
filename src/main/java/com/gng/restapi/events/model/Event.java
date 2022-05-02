package com.gng.restapi.events.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * 
 * Lombok 어노테이션은 Meta 어노테이션에 추가할 수 없으므로 직접 작성해주어야 함
 * 또한, @Data 어노테이션을 Entity에 사용할 경우
 * 해당 어노테이션에 @EqualsAndHashCode가 포함되어 있기 때문에
 * StackOverflow가 발생할 수 있으므로 권장하지 않음
*/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
// equals를 사용하여 비교할 때, id 멤버변수의 값을 이용하여 확인
// 다른 Entity를 참조하는 멤버변수를 사용할 경우 StackOverflow가 발생할 수 있으므로 주의
@EqualsAndHashCode(of = {"id"})
@Entity
public class Event {
	
	@Id
	private Integer id;
	
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
	
	private boolean offline;
	private boolean free;
	
	// Enum의 기본 저장방식은 숫자이기 때문에
	// Enum이 변경될 경우 DB가 꼬일 수 있으므로 String으로 변경
	@Enumerated(EnumType.STRING)
	private EventStatus eventStatus;

	/**
	 * Update free/offline
	 */
	public void update() {
		if(this.basePrice == 0 && this.maxPrice == 0) {
			this.free = true;
		} else {
			this.free = false;
		}
		
		if(this.location != null && !this.location.isBlank()) {
			this.offline = true;
		} else {
			this.offline = false;
		}
	}
}
