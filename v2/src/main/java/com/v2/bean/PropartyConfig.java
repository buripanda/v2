package com.v2.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "content.image")
@Data
public class PropartyConfig {
	
	/**
	 * 画像パスルート
	 */
	public String pathroot;
	
}
