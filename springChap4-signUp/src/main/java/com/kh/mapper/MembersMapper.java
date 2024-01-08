package com.kh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.model.Member;

@Mapper
public interface MembersMapper {

	void insertMember(Member member);
	
	List<Member> selectAllMember();
	
	void updateMember(Member member);
	
	void deleteMember(Long memberID);
	
	Member getMemberById(Long memberID);
}
