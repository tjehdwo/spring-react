package com.kh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChan(HttpSecurity http) throws Exception{
		http
		.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				// 모든 요청에 대해 권한 검사를 하지 않고
				// 모든 사용자한테 엔드포인트로 접근할 수 있도록 허용
				.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
		.formLogin((formLogin) -> formLogin
				.loginPage("/members/login")
				.defaultSuccessUrl("/"))
		.logout((logout) -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true));
		
		return http.build();
	}
}
