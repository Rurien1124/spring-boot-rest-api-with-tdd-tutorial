package com.gng.restapi.events.model;

import org.springframework.hateoas.EntityModel;

/**
 * HATEOAS 링크 사용을 위한 클래스
 * @author gchyoo
 *
 */
// ResourceSupport was deprecated, use RepresentationalModel
// Resource was deprecated, use EntityModel
public class EventResource extends EntityModel<Event> {
	
//	@JsonUnwrapped // event로 wrapping된 JSON을 unwrap시킴
// RepresentationalModal 사용시에는 Entity가 자동으로 unwrap되지 않으므로 사용해야 함
	public EventResource(Event event) {
		super(event);
	}
}
