package cn.qp.security.core.social.qq.connet;

import cn.qp.security.core.social.qq.api.QQ;
import cn.qp.security.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * QQ适配器
 *
 * 将不同格式的用户信息转化为固定格式的Connection对象(QQ对象)
 * 转化成功之后就将Connection中封装进去一个用户信息。
 * @author BaoZi
 */
public class QQAdapter implements ApiAdapter<QQ> {
    /**
     * 测试api是否可用，
     */
    @Override
    public boolean test(QQ api) {
        return true;
    }

    /**
     * 适配的方法
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        QQUserInfo userInfo = api.getUserInfo();
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        values.setProfileUrl(null);
        values.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }

    @Override
    public void updateStatus(QQ api, String message) {
        // 更新状态，如微博发布新微博，qq中没有这个功能
    }
}
