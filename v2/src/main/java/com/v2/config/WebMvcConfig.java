package com.v2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.v2.AppInterceptor;

/**
 * インスペクタークラス。
 * リクエスト前後の処理を行う
 * @author burip
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	private final JdbcTemplate jdbcTemplate;

	public WebMvcConfig(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Bean
	public AppInterceptor appInterceptor() {
		return new AppInterceptor(jdbcTemplate);
	}

	public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(appInterceptor());
	}
	
}