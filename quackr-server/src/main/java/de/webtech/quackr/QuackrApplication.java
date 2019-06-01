package de.webtech.quackr;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


@Configuration
@ControllerAdvice
@SpringBootApplication
@EntityScan("de.webtech.quackr.persistence")
public class QuackrApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuackrApplication.class, args);
	}
/*
	@ExceptionHandler(AuthorizationException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String handleException(AuthorizationException e, Model model) {

		log.debug("AuthorizationException was thrown", e);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", HttpStatus.FORBIDDEN.value());
		map.put("message", "No message available");
		model.addAttribute("errors", map);

		return "error";
	}

	@Bean
	public Realm realm() {
		TextConfigurationRealm realm = new TextConfigurationRealm();
*//*
		realm.setUserDefinitions("joe.coder=password,user\n" +
				"jill.coder=password,admin");

		realm.setRoleDefinitions("admin=read,write\n" +
				"user=read");
*//*

		realm.setCachingEnabled(true);
		return realm;
	}

	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
		//chainDefinition.addPathDefinition("/login", "auth");
		return chainDefinition;
	}*/
}
