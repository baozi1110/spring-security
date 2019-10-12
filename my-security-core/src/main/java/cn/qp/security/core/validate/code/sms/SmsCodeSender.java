package cn.qp.security.core.validate.code.sms;

/**
 * @author BaoZi
 */
public interface SmsCodeSender {
    void send(String mobile, String code);

}
