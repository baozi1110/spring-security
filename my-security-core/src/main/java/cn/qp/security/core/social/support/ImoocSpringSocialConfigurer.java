package cn.qp.security.core.social.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 自定义的过滤器
 * <p>
 * 将{@link SocialAuthenticationFilter}添加到Spring Security的过滤器链的配置器
 *
 * @author BaoZi
 */
@Getter
@Setter
public class ImoocSpringSocialConfigurer extends SpringSocialConfigurer {
    private String filterProcessesUrl;

    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;


    public ImoocSpringSocialConfigurer(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        // 设置自定义的过滤器拦截路径
        filter.setFilterProcessesUrl(filterProcessesUrl);
        //浏览器环境下使用的默认的认证成功处理器进行跳转操作，是正确的
        //使用app时需要使用自定义的处理器返回令牌信息
        if (socialAuthenticationFilterPostProcessor != null) {
            socialAuthenticationFilterPostProcessor.process(filter);
        }
        return (T) filter;
    }
}
