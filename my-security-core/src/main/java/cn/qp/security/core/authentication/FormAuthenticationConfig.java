/**
 *
 */
package cn.qp.security.core.authentication;


import cn.qp.security.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 表单登录配置
 *	抽象出的安全配置，配置了通用项
 * @author Baozi
 */
@Component
public class FormAuthenticationConfig {

    @Autowired
    protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    public void configure(HttpSecurity http) throws Exception {
        // 相当于springSecurity的默认配置
        // http.formLogin()// 指定通过表单登录，这种方式会先跳转到表单页，认证通过后才会跳转会原页面
        // http.httpBasic() 在访问的页面上弹出认证窗口，不会跳转
        // loginPage中自定义的登录URL，指定当访问该URL时，使用UsernamePasswordAuthenticationFilter来处理登录请求
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
                //指向自定义的登录成功处理器
                .successHandler(imoocAuthenticationSuccessHandler)
                .failureHandler(imoocAuthenticationFailureHandler);
    }

}
