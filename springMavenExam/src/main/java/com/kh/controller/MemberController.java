package com.kh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MemberController {
	@RequestMapping("/login")
	public String memberLogin() {
		//로그인 할 때 필요한 코드를 작성
		return "redirect:/";
	}

}
