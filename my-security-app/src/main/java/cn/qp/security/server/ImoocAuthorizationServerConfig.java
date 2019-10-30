package cn.qp.security.server;

import cn.qp.security.core.properties.OAuth2ClientProperties;
import cn.qp.security.core.properties.SecurityProperties;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务器配置
 * OAUth2授权服务器的配置，实现AuthorizationServerConfigurerAdapter类后只有该实现类生效
 * @author BaoZi
 */
@Configuration
@EnableAuthorizationServer
public class ImoocAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;



    /**
     * 针对端点的配置
     *
     * 如果没有类继承AuthorizationServerConfigurerAdapter，spring会使用默认的AuthenticationManager和UserDetailsService，
     * 一旦继承AuthorizationServerConfigurerAdapter类后，这两个bean都需要显式配置
     * @param endpoints 入口点
     * @throws Exception Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
        //增强器修改accessToken中的内容
        if(jwtAccessTokenConverter != null && jwtTokenEnhancer != null){
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            // 添加信息的增强器
            enhancers.add(jwtTokenEnhancer);
            // 添加密签签名
            enhancers.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancers);
            endpoints.tokenEnhancer(enhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    /**
     * 客户端相关的配置
     *
     * 配置了这个方法后，配置文件中指定的security.oauth2.client.clientId = imooc和clientSecret都不起作用了，
     * 由该方法指定
     * @param clients 客户端(第三方应用)
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 指定clients信息存到内存中 clients.inJdbc()则存到数据库中
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
            for (OAuth2ClientProperties client : securityProperties.getOauth2().getClients()) {
                // security.oauth2.client.clientId
                builder.withClient(client.getClientId())
                        .secret(client.getClientSecret())
                        // 指定的应用支持的授权模式
                        .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                        // 令牌有效时间
                        .accessTokenValiditySeconds(client.getAccessTokenValidateSeconds())
                        .refreshTokenValiditySeconds(2592000)
                        //spring中的权限 read write
                        .scopes("all");
            }
        }
    }
}
