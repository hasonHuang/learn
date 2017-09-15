package com.hason.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 配置 Spring Security，解决每一次请求 Spring Session 造成的 session id 刷新问题。
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/9/15
 */
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    // 不想大费周章写一个登录页面，于是开启了http basic认证
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()//<1>
                .and()
                .logout().permitAll();
    }

    // 配置了security config之后，springboot的autoConfig就会失效，于是需要手动配置用户
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password("admin").roles("USER");//<2>
    }
}
