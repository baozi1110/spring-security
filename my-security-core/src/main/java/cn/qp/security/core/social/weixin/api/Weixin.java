
package cn.qp.security.core.social.weixin.api;

/**
 * 微信API调用接口
 * 
 * @author BaoZi
 *
 */
public interface Weixin {
	/**
	 * 微信与qq不同的地方是微信在完成OAuth流程后，获取的不止accessToken，还有openId，
	 */
	WeixinUserInfo getUserInfo(String openId);
	
}
