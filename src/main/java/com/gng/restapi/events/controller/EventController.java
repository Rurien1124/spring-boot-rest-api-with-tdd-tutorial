package com.gng.restapi.events.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping("/events")
	public ResponseEntity queryEvents(
			Pageable pageable, // 페이징을 위한 파라미터
			PagedResourcesAssembler<Event> pageAssembler // 페이징 정보를 위한 파라미터
			) {
		
		Page<Event> pages = this.eventRepository.findAll(pageable);
		
		// toResource was deprecated, use toModel
		// page informations
		PagedModel<EntityModel<Event>> pagedModel = pageAssembler.toModel(pages, event -> new EventResource(event));
		
		return ResponseEntity.ok()
				.body(pagedModel);
	}
	
	@GetMapping("/events/{id}")
	public ResponseEntity queryEvent(
			@PathVariable Integer id
			) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		
		if(optionalEvent.isEmpty()) {
			return ResponseEntity.notFound()
					.build();
		}
		
		Event event = optionalEvent.get();
		EventResource eventResource = new EventResource(event);
		
		return ResponseEntity.ok()
				.body(eventResource);
	}
	
	@PatchMapping("/events/{id}")
	public ResponseEntity updateEvent(
			@PathVariable Integer id,
			@RequestBody @Valid EventDto eventDto,
			Errors errors
			) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		
		if(optionalEvent.isEmpty()) {
			return ResponseEntity.notFound()
					.build();
		}

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
		
		Event prevEvent = optionalEvent.get();
		this.modelMapper.map(eventDto, prevEvent);
		
		Event savedEvent = this.eventRepository.save(prevEvent);
		
		EventResource eventResource = new EventResource(savedEvent);
		
		return ResponseEntity.ok()
				.body(eventResource);
	}
}
