package cn.qp.security.browser;


import cn.qp.security.core.properties.SecurityConstants;
import cn.qp.security.core.properties.SecurityProperties;
import cn.qp.security.core.social.SocialController;
import cn.qp.security.core.social.support.SocialUserInfo;
import cn.qp.security.core.support.SimpleResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理不同类型的请求的controller类
 * @author BaoZi
 * @date 2019/10/3 21:25
 */
@RestController
public class BrowserSecurityController extends SocialController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    // RequestCache将 SavedRequest存储在HttpSession中
    private RequestCache requestCache = new HttpSessionRequestCache();

    //执行重定向到提供的URL
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    /**
     * 当需要身份认证时跳转到这里
     */
    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //封装了缓存请求所需的功能，以实现身份验证机制（通常是基于表单的登录）以重定向到原始URL，并使用RequestCache构建包装的请求，从而再现原始请求数据。
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        //判断引发跳转的请求是否为html
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("引发跳转的请求 {}",targetUrl);
            //如果是html请求就跳转到指定页面，不是则返回错误状态码
            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                //跳转的页面第三个参数，如果用户配置了该值就用用的值，没有配置则用默认值
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getSignInPage());
            }
        }
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
    }

    /**
     * 用户第一次社交登录时，会引导用户进行用户注册或绑定，此服务用于在注册或绑定页面获取社交网站用户信息
     * /social/user接口
     * @param request
     * @return
     */
    @GetMapping(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL)
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
        // 获取与客户端尝试登录的提供者用户的连接。
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        return buildSocialUserInfo(connection);
    }

}
