package cn.qp.security.core.social;

import cn.qp.security.core.properties.SecurityProperties;
import cn.qp.security.core.social.support.ImoocSpringSocialConfigurer;
import cn.qp.security.core.social.support.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 社交登录配置主类
 *
 * 需要添加@Order(1)注解，使SocialConfig先注册，否则会默认使用InMemoryUsersConnectionRepository 导致ConnectionSignUp无法使用
 * @author BaoZi
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    @Autowired(required = false)
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    // @Autowired(required = false)
    // private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        // 三个参数：1.数据源；2.获取connectionFactory的定位器；3.加解密形式，现在选择的不加密
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
        // 如果创建的表格名称有要求，如需要加imooc_的前缀，那么需要配置此项，_后面的名字不能变
        repository.setTablePrefix("imooc_");
        // 如果用户配置了ConnectionSignUp的实现则使用该形式
        // 在用户第三方登录完成后，在demo的数据库中查询用户，当用户不存在时直接用户信息直接进行注册
        if(connectionSignUp != null) {
            repository.setConnectionSignUp(connectionSignUp);
        }
        return repository;
    }

    /**
     * 社交登录配置类，供浏览器或app模块引入设计登录配置用。
     * @return
     */
    @Bean
    public SpringSocialConfigurer imoocSocialSecurityConfig() {
        // 社交登录功能拦截的url，默认/auth，可以自己配置
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        ImoocSpringSocialConfigurer configurer = new ImoocSpringSocialConfigurer(filterProcessesUrl);
        // 指定当第三方登录完成后找不到用户时跳转的注册页面
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        // 设定SocialAuthenticationFilter后处理器，用于在不同环境下个性化社交登录的配置
        configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
        return configurer;
    }

    /**
     * 用来处理注册流程的工具类
     *
     * ProviderSignInUtils有两个作用：
     * （1）从session里获取封装了第三方用户信息的Connection对象
     * （2）将注册的用户信息与第三方用户信息（QQ信息）建立关系并将该关系插入到userconnection表里
     * <p>
     * 需要的两个参数：
     * （1）ConnectionFactoryLocator 可以直接注册进来，用来定位ConnectionFactory
     * （2）UsersConnectionRepository----》getUsersConnectionRepository(connectionFactoryLocator)
     * 即我们配置的用来处理userconnection表的bean
     *
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator)) {
        };
    }
}
