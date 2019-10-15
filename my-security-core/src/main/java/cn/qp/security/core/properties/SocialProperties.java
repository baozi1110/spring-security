
package cn.qp.security.core.properties;

import lombok.Data;

/**
 * 社交登录配置项
 * @author BaoZi
 */
@Data
public class SocialProperties {
	
	/**
	 * 社交登录功能拦截的url，默认/auth，可以自己配置
	 */
	private String filterProcessesUrl = "/auth";

	private QQProperties qq = new QQProperties();
	
	private WeixinProperties weixin = new WeixinProperties();
	
}
