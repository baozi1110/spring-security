package cn.qp.security.core.validate.code;

import cn.qp.security.core.properties.SecurityProperties;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 校验验证码的过滤器
 * @author BaoZi
 * @date 2019/10/8 17:35
 */
@Getter
@Setter
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    /**
     * 验证码校验失败处理器
     */
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    /**
     * 存放所有需要校验验证码的url
     */
    private Set<String> urls = new HashSet<>();

    private SecurityProperties securityProperties;

    /**
     * 校验url匹配的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 在初始化bean的时候会执行该方法,为某些不能使用注入的属性赋值
     */
    @Override
    public void afterPropertiesSet() throws ServletException{
        super.afterPropertiesSet();
        String[] configUrls = StringUtils.split(securityProperties.getCode().getImage().getUrl(),",");
        urls.addAll(Arrays.asList(configUrls));
        urls.add("/authentication/form");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //如果请求的url在集合中，那么执行校验逻辑
        boolean action = false;
        for (String url : urls) {
            if (pathMatcher.match(url, request.getRequestURI())){
                action =true;
            }
        }
        if (action){
            try {
                validate(new ServletWebRequest(request));
            }catch (ValidateCodeException e) {
                //将错误信息返回
                authenticationFailureHandler.onAuthenticationFailure(request,response, e);
                //抛出异常后不要继续向下执行了，直接返回，不写的话会继续执行该请求如获取用户信息等下面的操作
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(ServletWebRequest servletWebRequest) {
    }

}
