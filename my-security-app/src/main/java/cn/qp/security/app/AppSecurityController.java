/**
 * 
 */
package cn.qp.security.app;


import cn.qp.security.app.social.AppSingUpUtils;
import cn.qp.security.core.properties.SecurityConstants;
import cn.qp.security.core.social.SocialController;
import cn.qp.security.core.social.support.SocialUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Baozi
 *
 */
@RestController
public class AppSecurityController extends SocialController {
	
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	
	@Autowired
	private AppSingUpUtils appSingUpUtils;

	/**
	 * 需要注册时跳到这里，返回401和用户信息给前端
	 *
	 * app访问每次都会创建一个session，所以可以通过 {@link ProviderSignInUtils} 获取请求信息
	 * 但是第二次访问session是新的，之前的信息不存在了，所以需要使用 {@link AppSingUpUtils}
	 * 将请求信息转存到redis中
	 */
	// @GetMapping("/social/signUp")
	@GetMapping(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
		appSingUpUtils.saveConnectionData(new ServletWebRequest(request), connection.createData());
		return buildSocialUserInfo(connection);

		// SocialUserInfo userInfo = new SocialUserInfo();
		// Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
		// userInfo.setProviderId(connection.getKey().getProviderId());
		// userInfo.setProviderUserId(connection.getKey().getProviderUserId());
		// userInfo.setNickname(connection.getDisplayName());
		// userInfo.setHeadimg(connection.getImageUrl());
		// appSingUpUtils.saveConnectionData(new ServletWebRequest(request), connection.createData());
		// return userInfo;
	}

}
