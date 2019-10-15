
package cn.qp.security.core.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * QQ登录配置项
 *
 * @author BaoZi
 */
public class QQProperties extends SocialProperties {
	
	/**
	 * 第三方id，用来决定发起第三方登录的url，默认是 qq。
	 * 可以自己在配置中指定，配置项该项需要与页面上qq登录的请求最后一级地址相同
	 * <h3>社交登录</h3>
	 * <a href="/auth/qq">QQ登录</a>
	 */
	private String providerId = "qq";

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
}
