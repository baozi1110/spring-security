package cn.qp.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码生成器
 * @author BaoZi
 * @date 2019/10/9 10:33
 */
public interface ValidateCodeGenerator {
    /**
     * 生成校验码
     * @param request
     * @return
     */
    ImageCode generate(ServletWebRequest request);
}
