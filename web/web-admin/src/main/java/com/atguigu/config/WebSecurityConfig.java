package com.atguigu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableWebSecurity //@EnableWebSecurity是开启SpringSecurity的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("lucy")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("");

    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //调父类的方法
        //super.configure(http);

        //自定义登录页面
        //设置允许页面嵌套
        http.headers().frameOptions().disable();
        http
                .authorizeRequests()
                .antMatchers("/static/**","/login").permitAll()  //允许匿名用户访问的路径
                .anyRequest().authenticated()    // 其它页面全部需要验证
                .and()
                .formLogin()
                .loginPage("/login")    //用户未登录时，访问任何需要权限的资源都转跳到该路径，即登录页面，此时登陆成功后会继续跳转到第一次访问的资源页面（相当于被过滤了一下）
                .defaultSuccessUrl("/") //登录认证成功后默认转跳的路径
                .and()
                .logout()
                .logoutUrl("/logout")   //退出登陆的路径，指定spring security拦截的注销url,退出功能是security提供的
                .logoutSuccessUrl("/login");//用户退出后要被重定向的url
        //关闭跨域请求伪造
        http.csrf().disable();
        //指定自定义的访问拒绝处理器
        http.exceptionHandling().accessDeniedHandler(new AtguiguAccessDeniedHandler());
    }

    @Bean
    public  BCryptPasswordEncoder  createBCryptPasswordEncoder(){
         return new BCryptPasswordEncoder();
    }
   /* @ComponentScan("com.atguigu")  包扫描
    @EnableAspectJAutoProxy        Aop
    @EnableTransactionManagement   事务    */

}