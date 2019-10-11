package cn.qp.security.core.social.qq.connet;

import cn.qp.security.core.social.qq.api.QQ;
import cn.qp.security.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * qq服务提供商
 *
 * @author BaoZi
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;
    /**
     * 用户登录第三方应用后，应用导向的服务提供商地址，获取授权码
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * 获取token地址的url
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    /**
     *
     * @param appId 第三方应用注册到qq后得到的appId
     * @param appSecret  第三方应用注册到qq后得到
     */
    public QQServiceProvider(String appId, String appSecret) {
        super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));

    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }
}
