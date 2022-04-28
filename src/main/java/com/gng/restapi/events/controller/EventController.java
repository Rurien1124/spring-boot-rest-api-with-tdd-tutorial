package com.gng.restapi.events.controller;

import java.net.URI;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gng.restapi.events.model.Event;
import com.gng.restapi.events.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@Controller
public class EventController {
	
	private final EventRepository eventRepository;
	
	@PostMapping(value = "/events", produces = {MediaTypes.HAL_JSON_VALUE})
	public ResponseEntity<Event> createEvent(
			@RequestBody(required = true) Event event
			) {
		
		// Save event
		Event newEvent = this.eventRepository.save(event);
		
		// ControllerLinkBuilder was deprecated
		// Create link
		URI createdUri = WebMvcLinkBuilder.linkTo(EventController.class)
				.slash(newEvent.getId())
				.toUri();
		
		return ResponseEntity.created(createdUri)
				.body(newEvent);
	}
}
