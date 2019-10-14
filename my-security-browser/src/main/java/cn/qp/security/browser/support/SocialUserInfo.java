package cn.qp.security.browser.support;


import lombok.Getter;
import lombok.Setter;

/**
 * @author BaoZi
 */
@Getter
@Setter
public class SocialUserInfo {
    private String providerId;

    private String providerUserId;

    private String nickname;

    private String headimg;
}
