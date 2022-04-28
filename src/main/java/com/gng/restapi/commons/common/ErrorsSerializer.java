package com.gng.restapi.commons.common;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serialize Errors to JSON
 * @author gchyoo
 *
 */
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

	@Override
	public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// JSON array 열기
		gen.writeStartArray();
		
		errors.getFieldErrors().forEach(error -> {
					try {
						// JSON object 열기
						gen.writeStartObject();

						// JSON object 값 입력
						gen.writeStringField("field", error.getField());
						gen.writeStringField("objectName", error.getObjectName());
						gen.writeStringField("code", error.getCode());
						gen.writeStringField("defaultMessage", error.getDefaultMessage());
						
						Object rejectedValue = error.getRejectedValue();
						if(rejectedValue != null) {
							gen.writeStringField("rejectedValue", rejectedValue.toString());
						}

						// JSON object 닫기
						gen.writeEndObject();
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
		
		errors.getGlobalErrors().forEach(error -> {
			try {
				gen.writeStartObject();
				
				gen.writeStringField("objectName", error.getObjectName());
				gen.writeStringField("code", error.getCode());
				gen.writeStringField("defaultMessage", error.getDefaultMessage());
				
				gen.writeEndObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		// JSON array 닫기
		gen.writeEndArray();
	}
	
}
