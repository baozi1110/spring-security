
package cn.qp.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 认证服务器注册的第三方应用配置项
 * 
 * @author BaoZi
 */
@Getter
@Setter
public class OAuth2ClientProperties {
	
	/**
	 * 第三方应用appId
	 */
	private String clientId;
	/**
	 * 第三方应用appSecret
	 */
	private String clientSecret;
	/**
	 * 针对此应用发出的token的有效时间
	 * 如果不配置使用spring默认配置0.表示不会过期
	 */
	private int accessTokenValidateSeconds = 7200;
	
}
