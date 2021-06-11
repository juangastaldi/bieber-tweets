package org.interview.config;

import org.interview.context.AppContext;
import org.interview.entity.Authenticator;
import org.interview.oauth.twitter.TwitterAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class AppConfig {

	@Autowired
	private AppContext appContext;

	@Bean
	public TwitterAuthenticator getTwitterAuthenticator() {
		Authenticator auth = new Authenticator();
		auth.setConsumerKey(appContext.getConsumerKey());
		auth.setConsumerSecret(appContext.getConsumerSecret());
		return new TwitterAuthenticator(auth);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).select()
				.apis(RequestHandlerSelectors.basePackage("org.interview.controller")).paths(PathSelectors.any())
				.build();
	}

}
