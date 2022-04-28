package com.gng.restapi.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gng.restapi.events.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer>{

}
