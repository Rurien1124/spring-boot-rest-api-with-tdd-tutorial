package com.gng.restapi.events.model;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * 
 * Lombok ������̼��� Meta ������̼ǿ� �߰��� �� �����Ƿ� ���� �ۼ����־�� ��
 * ����, @Data ������̼��� Entity�� ����� ���
 * �ش� ������̼ǿ� @EqualsAndHashCode�� ���ԵǾ� �ֱ� ������
 * StackOverflow�� �߻��� �� �����Ƿ� �������� ����
*/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
// equals�� ����Ͽ� ���� ��, id ��������� ���� �̿��Ͽ� Ȯ��
// �ٸ� Entity�� �����ϴ� ��������� ����� ��� StackOverflow�� �߻��� �� �����Ƿ� ����
@EqualsAndHashCode(of = {"id"})
public class Event {
	
	private Integer id;
	private String name;
	private String description;
	@ApiParam(value = "�������� ���� ��� �¶��� ����", required = false)
	private String location;
	
	private LocalDateTime beginEnrollmentDateTime;
	private LocalDateTime closeEnrollmentDateTime;
	private LocalDateTime beginEventDateTime;
	private LocalDateTime endEventDateTime;

	@ApiParam(required = false)
	private int basePrice;
	@ApiParam(required = false)
	private int maxPrice;
	private int limitOfEnrollment;
	
	private boolean offline;
	private boolean free;
	private EventStatus eventStatus;
}
