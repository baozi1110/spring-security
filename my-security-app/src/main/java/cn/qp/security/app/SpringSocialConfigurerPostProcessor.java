package cn.qp.security.app;

import cn.qp.security.core.social.ImoocSpringSocialConfigurer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 实现BeanPostProcessor的作用是spring容器中所有bean初始化前和后都要经过实现的两个方法
 * @author Baozi
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

    /**
     * spring中bean初始化之前的操作，原样返回
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 在app环境下当imoocSocialSecurityConfig的bean初始化好后修改其中的配置
     * 指定当第三方登录完成后找不到用户时跳转的注册页面
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StringUtils.equals(beanName, "imoocSocialSecurityConfig")) {
            ImoocSpringSocialConfigurer configurer = (ImoocSpringSocialConfigurer)bean;
            configurer.signupUrl("/social/signUp");
            return configurer;
        }
        return bean;
    }

}
