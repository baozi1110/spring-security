package cn.qp.security.browser;

import cn.qp.security.core.authentication.AbstractChannelSecurityConfig;
import cn.qp.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import cn.qp.security.core.properties.SecurityConstants;
import cn.qp.security.core.properties.SecurityProperties;
import cn.qp.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 浏览器环境下安全配置主类
 *
 * @author BaoZi
 * @date 2019/9/30 10:23
 */
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SpringSocialConfigurer imoocSocialSecurityConfig;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;


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
        // 密码认证相关的配置
        applyPasswordAuthenticationConfig(http);

        // http.apply()来添加验证码相关或者手机短信相关的配置
        http.apply(validateCodeSecurityConfig)
                    .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                    .and()
                .apply(imoocSocialSecurityConfig)
                    .and()
                // 浏览器特有的配置
                //配置rememberMe功能
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                    .userDetailsService(userDetailsService)
                    .and()
                //session相关的配置
                .sessionManagement()
                    //session失效策略
                    .invalidSessionStrategy(invalidSessionStrategy)
                    //设置为1则用户只能在一个地方登陆，在其他地方会被踢掉
                    .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                    //设置为true则达到登陆最大数量后不让其他登陆，false是踢掉前面的人，默认false
                    .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                    //超时处理策略
                    .expiredSessionStrategy(sessionInformationExpiredStrategy)
                    .and()
                    .and()
                //退出登录
                .logout()
                    //退出登录的url，默认是logout，根据页面上的连接指定
                    .logoutUrl("/signOut")
                    //退出成功后跳转的html页面
                    // .logoutSuccessUrl("/imooc-logout.html")
                    //与logoutSuccessUrl配置互斥，logoutSuccessHandler优先级更高
                    .logoutSuccessHandler(logoutSuccessHandler)
                    //指定成功注销后将删除的cookie名称
                    .deleteCookies("JSESSIONID")
                .and()
                //表示以下都是对请求授权的配置
                .authorizeRequests()
                //指定当访问该页面时不需要身份认证，如果不加该选项会反复在访问的页面和自定义登录页面间重定向而报错
                .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                        securityProperties.getBrowser().getSignUpUrl(),
                        securityProperties.getBrowser().getSession().getSessionInvalidUrl(),
                        securityProperties.getBrowser().getSignOutUrl(),
                        "/user/regist")
                .permitAll()
                //访问该接口需要的权限
                .antMatchers(HttpMethod.GET,"/user/*").hasRole("ADMIN")
                //任何请求
                .anyRequest()
                //都需要身份认证
                .authenticated()
                    .and()
                .csrf().disable();
    }
}
