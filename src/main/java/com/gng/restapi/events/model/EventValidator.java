package com.gng.restapi.events.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * @Valid 어노테이션으로 판단할 수 없는 경우를 처리(비즈니스 로직에 위반되는 경우)
 * @author gchyoo
 *
 */
@Component
public class EventValidator {
	
	public void validate(EventDto eventDto, Errors errors) {
		if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
			errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
			errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");
			
			// Global error
			errors.reject("wrongPrices", "Values to prices are wrong");
		}
		
		if(eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEventDateTime()) ||
				eventDto.getEndEventDateTime().isBefore(eventDto.getCloseEnrollmentDateTime()) ||
				eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEnrollmentDateTime())
				) {
			errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong");
		}
		
	}
}
