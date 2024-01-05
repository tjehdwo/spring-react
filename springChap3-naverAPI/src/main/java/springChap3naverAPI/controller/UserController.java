package springChap3naverAPI.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import springChap3naverAPI.model.UserSNS;
import springChap3naverAPI.repository.UserRepository;
import springChap3naverAPI.service.UserService;

@Controller
public class UserController {
	
	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	
	@GetMapping("/login")
	public String loginPage() {
		return "index";
	}
	
	
	@GetMapping("/loginSuccess")
	public String loginSuccess(@AuthenticationPrincipal OAuth2User principal,@RequestParam(value= "naverResponse",required= false)String naverResponse, Model model) {
		
		userService.naverLoginService(principal, naverResponse);
		
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
		//userRepository.save(user);
	
		model.addAttribute("name",principal.getAttribute("name"));
		model.addAttribute("email",principal.getAttribute("email"));
		
		//모델이 naverResponse로 가지고 와야하는 경우 Naver 응답 추가
		model.addAttribute("naverRespnse",naverResponse);
		return "loginSuccess";
	}
}
