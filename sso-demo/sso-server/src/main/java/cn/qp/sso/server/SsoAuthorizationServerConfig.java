/**
 * 
 */
package cn.qp.sso.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 将该类设置为认证服务器并配置
 * @author Baozi
 *
 */
@Configuration
@EnableAuthorizationServer
public class SsoAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				// 设置该认证服务器能给哪些应用发令牌
				.withClient("imooc1")
				.secret("imoocsecrect1")
				.authorizedGrantTypes("authorization_code", "refresh_token")
				.scopes("all")
				.and()
				.withClient("imooc2")
				.secret("imoocsecrect2")
				.authorizedGrantTypes("authorization_code", "refresh_token")
				.scopes("all");
	}

	/**
	 * 发送令牌类型
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
	}

	/**
	 * 认证服务器的安全配置
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//授权表达式，表示要访问认证服务器的tokenKey时需要身份认证
		//tokenKey就是下面配置的 converter.setSigningKey("imooc") 签名
		security.tokenKeyAccess("isAuthenticated()");
	}

	/**
	 * 增强器
	 * @return 增强器
	 */
	@Bean
	public TokenStore jwtTokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter(){
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		//签名
        converter.setSigningKey("imooc");
        return converter;
	}

}
