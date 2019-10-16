package cn.qp.security.browser.session;

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
     * @param invalidSessionUrl 当session失效时跳转的地址
     */
    public ImoocExpiredSessionStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
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
