package cn.qp.security.core;

import cn.qp.security.core.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，使SecurityProperties生效
 * @author BaoZi
 * @date 2019/10/3 21:55
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {

}
