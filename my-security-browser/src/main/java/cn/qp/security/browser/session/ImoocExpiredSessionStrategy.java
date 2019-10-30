package cn.qp.security.browser.session;

import cn.qp.security.core.properties.SecurityProperties;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 并发登录导致session失效时，默认的处理策略
 * @author BaoZi
 */
public class ImoocExpiredSessionStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {

    /**
     * @param securityProperties 当session失效时跳转的地址
     */
    public ImoocExpiredSessionStrategy(SecurityProperties securityProperties) {
        super(securityProperties);
    }


    /**
     * 检测到过期的会话时的行为
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        onSessionInvalid(event.getRequest(), event.getResponse());
    }


    @Override
    protected boolean isConcurrency() {
        return true;
    }

}
