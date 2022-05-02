package com.gng.restapi.events.model;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.gng.restapi.events.controller.EventController;

/**
 * HATEOAS 링크 사용을 위한 클래스
 * @author gchyoo
 *
 */
// ResourceSupport was deprecated, use RepresentationalModel
// Resource was deprecated, use EntityModel
public class EventEntityModel extends EntityModel<Event> {
	
//	@JsonUnwrapped // event로 wrapping된 JSON을 unwrap시킴
// RepresentationalModal 사용시에는 Entity가 자동으로 unwrap되지 않으므로 사용해야 함
	public EventEntityModel(Event event) {
		super(event);
		add(WebMvcLinkBuilder.linkTo(EventController.class).slash(event.getId()).withSelfRel());
	}
}
