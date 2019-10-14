package cn.qp.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 自定义的过滤器
 *
 * 将{@link SocialAuthenticationFilter}添加到Spring Security的过滤器链的配置器
 * @author BaoZi
 */
    public class ImoocSpringSocialConfigurer extends SpringSocialConfigurer {
    private String filterProcessesUrl;

    public ImoocSpringSocialConfigurer(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        // 设置自定义的过滤器拦截路径
        filter.setFilterProcessesUrl(filterProcessesUrl);
        return (T) filter;
    }
}
