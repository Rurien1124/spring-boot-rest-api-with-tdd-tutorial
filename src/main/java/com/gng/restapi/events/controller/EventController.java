package com.gng.restapi.events.controller;

import java.net.URI;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gng.restapi.events.model.Event;
import com.gng.restapi.events.model.EventDto;
import com.gng.restapi.events.model.EventResource;
import com.gng.restapi.events.model.EventValidator;
import com.gng.restapi.events.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@Controller
public class EventController {
	
	private final EventRepository eventRepository;
	
	private final EventValidator eventValidator;
	
	private final ModelMapper modelMapper;
	
	@PostMapping(value = "/events", produces = {MediaTypes.HAL_JSON_VALUE})
	public ResponseEntity createEvent(
			@Valid @RequestBody(required = true) EventDto eventDto,
			Errors errors
			) {
		// Validation 실패 시 bad request 반환
		if(errors.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(errors);
		}
		
		eventValidator.validate(eventDto, errors);
		if(errors.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(errors);
		}
		
		// EventDto 객체 안의 데이터를 Event.class의 형태로 변환
		Event event = modelMapper.map(eventDto, Event.class);
		event.update();
		
		// Save event
		Event newEvent = this.eventRepository.save(event);
		
		// ControllerLinkBuilder was deprecated
		// Create link
		WebMvcLinkBuilder selfLinkBuilder = WebMvcLinkBuilder.linkTo(EventController.class)
				.slash(newEvent.getId());
		URI createdUri = selfLinkBuilder.toUri();
		
		// Body를 event가 아닌 eventResource로 사용함으로써 링크를 추가할 수 있음
		EventResource eventResource = new EventResource(newEvent);
		
		eventResource.add(WebMvcLinkBuilder.linkTo(EventController.class).withRel("query-events"));
		eventResource.add(WebMvcLinkBuilder.linkTo(EventController.class).withRel("update-event"));
		
		return ResponseEntity.created(createdUri)
				.body(eventResource);
	}
}
