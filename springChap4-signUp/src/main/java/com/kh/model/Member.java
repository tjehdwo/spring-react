package com.kh.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
	private Long memberID;
	private String username;
	private String password;
	private String fullname;
	private String email;
	private Date registrationDate;	

}
