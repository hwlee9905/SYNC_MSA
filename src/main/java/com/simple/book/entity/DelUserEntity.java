package com.simple.book.entity;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "DELUSERTBL")
@DynamicUpdate
public class DelUserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int no;
	
	private String id;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	private String password;
	
	private String birth;
	
	private String gender;
	
	@Column(name = "DEL_YN")
	private String delYn;
	
	@Column(name = "INS_DATE")
	private String insDate;
	
	@Column(name = "INS_TIME")
	private String insTime;
	
	@Column(name = "INS_ID")
	private String insId;
	
	@Column(name = "UPD_DATE")
	private String updDate;
	
	@Column(name = "UPD_TIME")
	private String updTime;
	
	@Column(name = "UPD_ID")
	private String updId;
}
