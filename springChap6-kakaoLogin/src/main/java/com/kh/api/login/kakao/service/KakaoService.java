package com.kh.api.login.kakao.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kh.api.login.kakao.dto.KakaoDTO;



@Service
public class KakaoService {
	@Value("${kakao.client.id}")
	private String KAKAO_CLIENT_ID;
	
	@Value("${kakao.client.secret}")
	private String KAKAO_CLIENT_SECRET;
    //value 를 썼기 때문에 각 값을 변수에 넣어서 보관하겠다는 의미
	@Value("${kakao.redirect.url}")
	private String KAKAO_REDIRECT_URL;
	
	//uri는 url보다 큰 범위 url 은 uri에 있는 링크
	//카카오 자체에서 인증으로 들어가는 공식 주소
	private final static String KAKAO_AUTH_URI = "http://kauth.kakao.com";
	//카카오 자체에서 api로 들어가는 공식 주소
	private final static String KAKAO_API_URI = "https://kapi.kakao.com";
	
	public String getKakaoLogin() {
		return KAKAO_AUTH_URI + "/oauth/authorize" + "?client_id =" + KAKAO_CLIENT_ID + "&redirect_uri" + KAKAO_REDIRECT_URL + "&response_type = code";
	}
	
	public KakaoDTO getKakaoInfo(String code) throws Exception {
		if(code == null) throw new Exception("존재하는 인증 코드가 없습니다.");
		
		//로그인이 허용된 토큰이 들어갈 공간
		String accessToken="";
		//만약에 토큰이 재발급 된다면 재발급된 토큰이 들어갈 공간
		String refreshToken="";
		
		//http HEADER에 내 정보를 흘려 보내겠다 작성
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", "application/x-www-form-urlencoded");
			
			/*카카오 로그인 아이디 + 시크릿키 + 코드 + 리다이렉트 url 모두 붙여줌
			MultiValueMap spring 에서 제공하는 인터페이스
			여러 개의 값을 하나에 키에 연결할 수 있도록 합쳐주는 역할
			http 에서 요청이 여러개 일 떄 자주 사용
			<String,String> 인 이유는 key,value를 각각 String으로 넣을
			MultiValueMap<key,value> key와 value를 String으로 설정해서
			카카오톡에서 설정한 값을 적고 그값에 대한 내용ㄷ물을 갖고오겠다는 의미
			예를 들어 
			MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
			params.add("key1","value1");
			params.add("key1","value2");
			params.add("key2","value3");
			결과
			key1 -> value1,value2
			key2 -> value3
			*/
			
			MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", "authorization_code");
			params.add("client_id", KAKAO_CLIENT_ID);
			params.add("client_secret",KAKAO_CLIENT_SECRET);
			params.add("code", code);
			params.add("redirect_uri", KAKAO_REDIRECT_URL);
			
			//String에서 제공하는 것
			//RestTemplate 사용해서 HTTP에 요청을 보내고
			//요청에 대한 응답을 받아오는 템플릿
			//HTTP 요청을 생성하고 서버에 전달해주는 역할
			RestTemplate restTemplate = new RestTemplate();
			
			//HTTP 요청이나 응답의 헤더 본문 http 매서드를 포함하는 엔터티
			HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(params,headers);
			
			ResponseEntity<String> response = restTemplate.exchange(
					//요청보낼 uri
					//카카오 oauth 토큰을 얻기위해서 /oauth/token POST 요청 보냄
					KAKAO_AUTH_URI + "/oauth/token",
					HttpMethod.POST,
					httpEntity,
					String.class
					);
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
			
			accessToken = (String) jsonObj.get("access_token");			
			refreshToken = (String) jsonObj.get("refresh_token");			
		} catch (Exception err) {
			err.printStackTrace();
		}
		return getUserInforWithToken(accessToken);
	}
	
	//카카오에서 회사에게 로그인 할 수 있도록 허용 받은 로그인 허용 토큰을 사용해서
	//카카오 api에서 사용자 정보를 가져오는 메서드
	private KakaoDTO getUserInforWithToken(String accessToken) throws Exception {
		//토큰용 HTTPHeader 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization","Bearer"+accessToken);
		headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
		
		//내용을 담아놓을 템플릿 생성
		RestTemplate rt = new RestTemplate();
		HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = rt.exchange(
				KAKAO_API_URI + "/v2/user/m2",
				HttpMethod.POST,
				httpEntity,
				String.class
				);
		
		//Response 데이터를 가져오기
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
		JSONObject account = (JSONObject) jsonObj.get("kakao_account");
		JSONObject profile = (JSONObject) account.get("profile");
		
		long id = (long) jsonObj.get("id");
		String email = String.valueOf(account.get("email"));
		String nickname = String.valueOf(profile.get("nickname"));
		
		return KakaoDTO.builder()
				.id(id)
				.email(email)
				.nickname(nickname).build();
		
		
		
	}
}
