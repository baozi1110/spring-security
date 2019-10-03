package cn.qp.security.browser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 浏览器环境下安全配置主类
 *
 * @author BaoZi
 * @date 2019/9/30 10:23
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 重写此方法以配置{@link HttpSecurity}
     *
     * @param http the {@link HttpSecurity} to modify
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //相当于springSecurity的默认配置
        // http.formLogin()// 指定通过表单登录，这种方式会先跳转到表单页，认证通过后才会跳转会原页面
        //http.httpBasic() 在访问的页面上弹出认证窗口，不会跳转
        http.formLogin()
                .loginPage("/imooc-signIn.html")
                //loginPage中自定义的登录URL，指定当访问该URL时，使用UsernamePasswordAuthenticationFilter来处理登录请求
                .loginProcessingUrl("/authentication/form")
                .and()
                //表示以下都是对请求授权的配置
                .authorizeRequests()
                //指定当访问该页面时不需要身份认证，如果不加该选项会反复在访问的页面和自定义登录页面间重定向而报错
                .antMatchers("/imooc-signIn.html").permitAll()
                //任何请求
                .anyRequest()
                //都需要身份认证
                .authenticated()
                .and()
                .csrf().disable();

    }
}
