package com.simple.book.entity;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "BOARDTBL")
@DynamicUpdate
public class BoardEntity {
	@Id
	private int no;
	
	private String id;
	
	private String contents;
	
	@Column(name = "IMAGE_PATH")
	private String imagePath;
	
	@Column(name = "VIDEO_PATH")
	private String videoPath;
	
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
