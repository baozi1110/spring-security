/**
 * 
 */
package cn.qp.security.app.jwt;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken的增强
 * @author Baozi
 *
 */
public class ImoocJwtTokenEnhancer implements TokenEnhancer {

	/**
	 * 将自定义的信息加入到accessToken中
	 */
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<>();
		info.put("company", "imooc");
		((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);
		
		return accessToken;
	}

}
