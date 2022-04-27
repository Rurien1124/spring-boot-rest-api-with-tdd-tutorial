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

@RequestMapping("/api")
@Controller
public class EventController {
	
	@PostMapping(value = "/events", produces = {MediaTypes.HAL_JSON_VALUE})
	public ResponseEntity createEvent(
			@RequestBody(required = true) Event event
			) {
		
		// ControllerLinkBuilder was deprecated
		URI createdUri = WebMvcLinkBuilder.linkTo(EventController.class)
				.slash("{id}")
				.toUri();
		
		return ResponseEntity.created(createdUri)
				.body(event);
	}
}
