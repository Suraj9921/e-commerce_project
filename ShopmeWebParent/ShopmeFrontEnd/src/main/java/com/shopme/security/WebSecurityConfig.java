package com.shopme.security;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomerOAuth2UserService oAuth2UserService;
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginHandler;
    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginHandler;


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration googleClientRegistration = ClientRegistration
                .withRegistrationId("google")
                .clientId("")
                .clientSecret("")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8081/Shopme/login/oauth2/code/google")
                .scope("email","profile")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .clientName("")
                .build();

        ClientRegistration facebookClientRegistration = ClientRegistration
                .withRegistrationId("facebook")
                .clientId("")
                .clientSecret("")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8081/Shopme/login/oauth2/code/facebook")
                .scope("email", "public_profile")
                .authorizationUri("https://www.facebook.com/v12.0/dialog/oauth")
                .tokenUri("https://graph.facebook.com/v12.0/oauth/access_token")
                .userInfoUri("https://graph.facebook.com/v12.0/me")
                .userNameAttributeName("id")
                .clientName("")
                .build();

        return new InMemoryClientRegistrationRepository(googleClientRegistration,facebookClientRegistration);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests((authorize)->authorize
                .requestMatchers("/create_order").permitAll()
                .requestMatchers("/account_details", "/update_account_details", "/orders/**",
                        "/cart", "/address_book/**", "/checkout", "/place_order", "/reviews/**",
                        "/process_paypal_order", "/write_review/**", "/post_review").authenticated()
                .anyRequest().permitAll());

        http.formLogin(login->login.loginPage("/login").usernameParameter("email").successHandler(databaseLoginHandler).permitAll());

        http.oauth2Login(oauth-> oauth.loginPage("/login").failureUrl("/login?error").userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService)).successHandler(oAuth2LoginHandler));

        http.logout(LogoutConfigurer::permitAll);

//        http.rememberMe(rememberme->rememberme.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ").tokenValiditySeconds(14 * 24 * 60 * 60));


        return http.build();
    }
    public void configure(WebSecurity webSecurity){
        webSecurity.ignoring().requestMatchers("/images/**","/js/**","/webjars/**");
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}