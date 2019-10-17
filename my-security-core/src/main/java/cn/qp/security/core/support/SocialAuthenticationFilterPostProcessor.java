package cn.qp.security.core.support;

import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * SocialAuthenticationFilter后处理器，用于在不同环境下个性化社交登录的配置
 * @author BaoZi
 */
public interface SocialAuthenticationFilterPostProcessor {
    /**
     * 处理在不同环境下个性化社交登录的配置
     * @param socialAuthenticationFilter socialAuthenticationFilter
     */
    void process(SocialAuthenticationFilter socialAuthenticationFilter);
}
