package com.kh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.model.Member;
import com.kh.service.MemberService;

/*
member 에 대한 조회나 회원가입
 */

@Controller
@RequestMapping("/members")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("member", new Member());
		
		return "register";
	}
	
	@PostMapping("/register")
	public String registerMember(Member member) {
		memberService.signUpMember(member);
		
		return "redirect:/";
	}
	
	@GetMapping("/List")
	public String selectAllMember(Model model) {
		List<Member> members = memberService.selectAllMember();
		model.addAttribute("members", members);
		return "MemberList";
	}
	
	@PostMapping("/update/{memberID}")
	public String updateMember(@PathVariable Long memberID, @ModelAttribute Member member) {
		member.setMemberID(memberID);
		memberService.updateMember(member);
		
		return "redirect:/members/List";
	}
	
	@GetMapping("/update/{memberID}")
	public String updateMember(@PathVariable Long memberID, Model model) {
		Member member = memberService.getMemberById(memberID);
		model.addAttribute("member",member);
		
		return "Member-Form";
	}
	
	@GetMapping("/delete/{memberID}")
	public String deleteMember(@PathVariable Long memberID) {
		memberService.deleteMember(memberID);
		return "redirect:/members/List";
	}
	
}
