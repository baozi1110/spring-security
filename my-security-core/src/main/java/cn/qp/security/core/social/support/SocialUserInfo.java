/**
 * 
 */
package cn.qp.security.core.social.support;

import lombok.Data;

/**
 * @author Baizi
 *
 */
@Data
public class SocialUserInfo {
	
	private String providerId;
	
	private String providerUserId;
	
	private String nickname;
	
	private String headimg;

	public String getProviderId() {
		return providerId;
	}

}
