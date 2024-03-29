package springChap3googleAPI.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import springChap3googleAPI.model.MemberGoogle;
import springChap3googleAPI.repository.MemberGoogleRepository;

public class MemberDetailServiceImpl implements UserDetailsService{
	@Autowired
	private MemberGoogleRepository memberGoogleRepository;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberGoogle user = memberGoogleRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("일치하는 유저 정보를 찾을 수 없습니다." + username)); 
				return new org.springframework.security.core.userdetails.User(
						user.getUsername(),"",Collections.emptyList());
	}

}