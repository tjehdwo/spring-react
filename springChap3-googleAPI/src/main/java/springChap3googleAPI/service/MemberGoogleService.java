package springChap3googleAPI.service;

import org.springframework.stereotype.Service;

import springChap3googleAPI.model.MemberGoogle;

@Service
public interface MemberGoogleService {
	MemberGoogle findByUsername(String username);
	void saveMember(MemberGoogle user);
}