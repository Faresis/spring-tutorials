package hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.beans.factory.annotation.*;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/css/**").permitAll()
            .anyRequest().fullyAuthenticated()
            .and()
            .formLogin();
    }

    @Configuration
    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            if (LDAP.equals(authType)) {
                auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource().ldif("classpath:test-server.ldif");
            } else if (AD.equals(authType)) {
                ActiveDirectoryLdapAuthenticationProvider prov = new ActiveDirectoryLdapAuthenticationProvider(authDomain, authServer);
                auth.authenticationProvider(prov);
            }
        }

        @Value("${authentication.type}")
        private String authType;

        @Value("${authentication.server}")
        private String authServer;

        @Value("${authentication.domain}")
        private String authDomain;

        private final String LDAP = "LDAP";
        private final String AD = "AD";
    }
}
