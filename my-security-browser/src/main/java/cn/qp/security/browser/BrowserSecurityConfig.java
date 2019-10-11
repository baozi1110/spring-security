package cn.qp.security.browser;

import cn.qp.security.core.properties.SecurityProperties;
import cn.qp.security.core.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 浏览器环境下安全配置主类
 *
 * @author BaoZi
 * @date 2019/9/30 10:23
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SpringSocialConfigurer imoocSocialConfigurer;

    /**
     * 密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 永久令牌存储库
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //创建存储token的数据库，只能用一次，否则报错
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    /**
     * 重写此方法以配置{@link HttpSecurity}
     *
     * @param http the HttpSecurity to modify
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //对validateCodeFilter进行配置和初始化
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();

        // 执行框架默认的过滤器之前先执行指定的过滤器 validateCodeFilter
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/authentication/require")
                // 相当于springSecurity的默认配置
                // http.formLogin()// 指定通过表单登录，这种方式会先跳转到表单页，认证通过后才会跳转会原页面
                // http.httpBasic() 在访问的页面上弹出认证窗口，不会跳转
                // loginPage中自定义的登录URL，指定当访问该URL时，使用UsernamePasswordAuthenticationFilter来处理登录请求
                .loginProcessingUrl("/authentication/form")
                //指向自定义的登录成功处理器
                .successHandler(imoocAuthenticationSuccessHandler)
                //自定义的登录失败处理器
                .failureHandler(imoocAuthenticationFailureHandler)
                //配置rememberMe功能
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                //表示以下都是对请求授权的配置
                .authorizeRequests()
                //指定当访问该页面时不需要身份认证，如果不加该选项会反复在访问的页面和自定义登录页面间重定向而报错
                .antMatchers("/authentication/require",
                        securityProperties.getBrowser().getSignInPage(),
                        "/code/image").permitAll()
                //任何请求
                .anyRequest()
                //都需要身份认证
                .authenticated()
                .and()
                .csrf().disable();

    }
}
