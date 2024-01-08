package com.kh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.mapper.MembersMapper;
import com.kh.model.Member;

@Service
public class MemberService {
	
	@Autowired
	private MembersMapper membersMapper;
	
	// 회원 정보 저장하기
	public void signUpMember(Member member) {
		membersMapper.insertMember(member);
	}
	
	// 전체 조회
	public List<Member> selectAllMember(){
		return membersMapper.selectAllMember();
	}
	
	// 아이디 조회
	public Member getMemberById(Long memberID) {
		return membersMapper.getMemberById(memberID);
	}
	
	// 수정
	public void updateMember(Member member) {
		membersMapper.updateMember(member);
	}
	
	// 삭제
	public void deleteMember(Long memberID) {
		membersMapper.deleteMember(memberID);
	}
	
	
	
	
	
}
