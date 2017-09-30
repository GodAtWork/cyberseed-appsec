package edu.syr.cyberseed.sage.server.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import edu.syr.cyberseed.sage.server.services.UsersService;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    // sample users pre-users in DB
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
        authenticationMgr.inMemoryAuthentication()
                .withUser("sampleuser").password("123").authorities("ROLE_USER")
                .and()
                .withUser("admin").password("123").authorities("ROLE_USER","ROLE_ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // allow stateless authorized requests
        http.httpBasic().and().sessionManagement()
                .sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                .and().authorizeRequests()
                .antMatchers("/findAll").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/findbyrecordID").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/findbypatient").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/findbyowner").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/save").access("hasRole('ROLE_ADMIN')");

    }
}
