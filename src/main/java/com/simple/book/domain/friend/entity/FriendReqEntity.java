package com.simple.book.domain.friend.entity;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "FRIENDREQTBL")
@DynamicUpdate
public class FriendReqEntity {

	@Id
	@Column(name = "REQ_NO")
	private int reqNo;
	
	private String id;
	
	@Column(name = "REQ_ID")
	private String reqId;
	
	@Column(name = "ACCEPT_YN")
	private String acceptYn;
	
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
