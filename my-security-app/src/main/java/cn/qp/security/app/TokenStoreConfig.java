package cn.qp.security.app;

import cn.qp.security.app.jwt.ImoocJwtTokenEnhancer;
import cn.qp.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 令牌存取的配置
 *
 * 令牌默认存储到内存中，一旦重启就丢失了，这里配置存储到redis中
 * @author Baozi
 *
 */
@Configuration
public class TokenStoreConfig {
	
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	/**
	 * 声明 redisTokenStore的bean
	 *
	 * 只有存在配置项 imooc.security.oauth.tokenStore且值为redis时才生效
	 */
	@Bean
	@ConditionalOnProperty(prefix = "imooc.security.oauth2", name = "tokenStore", havingValue = "redis")
	public TokenStore redisTokenStore() {
		return new RedisTokenStore(redisConnectionFactory);
	}

	/**
	 * Jwt替换默认令牌的配置
	 *
	 * ConditionalOnProperty注解：
	 * 	prefix+name：配置项的前缀+配置项最后一个值，组合起来就是检查的配置属性
	 * 	havingValue：当上面声明属性的值为 jwt时，类中的配置生效
	 * 	matchIfMissing：设置为true表示就算没有上面说的配置项这个类默认生效
	 */
	@Configuration
	@ConditionalOnProperty(prefix = "imooc.security.oauth2", name = "tokenStore", havingValue = "jwt", matchIfMissing = true)
	public static class JwtConfig {

		@Autowired
		private SecurityProperties securityProperties;

		@Bean
		public TokenStore jwtTokenStore() {
			return new JwtTokenStore(jwtAccessTokenConverter());
		}

		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter(){
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			// 密签：可以对秘钥进行签名，防止篡改，不是加密
			converter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
			return converter;
		}

		@Bean
		@ConditionalOnMissingBean(name ="jwtTokenEnhancer")
		public TokenEnhancer jwtTokenEnhancer(){
			return new ImoocJwtTokenEnhancer();
		}

	}

}
