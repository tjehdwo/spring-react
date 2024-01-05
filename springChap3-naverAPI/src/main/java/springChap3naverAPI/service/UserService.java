package springChap3naverAPI.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import springChap3naverAPI.model.UserSNS;
import springChap3naverAPI.repository.UserRepository;

public class UserService {
	
private final UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void googleLoginService(@AuthenticationPrincipal OAuth2User principal, Model model) {
		model.addAttribute("name",principal.getAttribute("name"));
		model.addAttribute("email",principal.getAttribute("email"));
	}
	
	public void naverLoginService(OAuth2User principal, String naverResponse) {
	System.out.println("OAuth2User Attributes : " + principal.getAttributes());
	String name = null;
	String email = null;
	
	if(naverResponse != null) {
		JsonNode responseNode;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			responseNode = objectMapper.readTree(naverResponse).get("response");
			
			if(responseNode != null) {
				name = responseNode.get("name").asText();
				email = responseNode.get("email").asText();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		if(name == null || email == null) {
			String principalName = principal.getName();
			
			String[] keyValue = principalName.replaceAll("[{}]","").split(",");
			for(String pair : keyValue) {
				String[] entry = pair.split("=");
				if(entry.length == 2) {
					String key = entry[0].trim();
					String value = entry[1].trim();
					if("name".equals(key)) {
						name = value;
					}else if("eamil".equals(key)) {
						email = value;
					}
				}
			}
		}
	}
	String provider = principal.getName();
	System.out.println("UserController 82 ↓");
	System.out.println("String principalName = principal.getName() : " + provider);
	//사용자 정보를 데이터베이스에 저장
	//1.model
	UserSNS user = new UserSNS();
	user.setName(name);
	user.setEmail(email);
	user.setProvider(provider);
	
	//저장
	userRepository.save(user);

	model.addAttribute("name",principal.getAttribute("name"));
	model.addAttribute("email",principal.getAttribute("email"));
	
	//모델이 naverResponse로 가지고 와야하는 경우 Naver 응답 추가
	model.addAttribute("naverRespnse",naverResponse);
	return "loginSuccess";
	
	}
}
