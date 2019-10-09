package cn.qp.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码校验异常处理类
 * @author BaoZi
 * @date 2019/10/8 17:40
 */
public class ValidateCodeException extends AuthenticationException {

    /**
     *
     */
    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException(String msg) {
        super(msg);
    }

}
