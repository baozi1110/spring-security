package cn.qp.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码生成器
 *  封装了校验码生成的逻辑
 * @author BaoZi
 * @date 2019/10/9 10:33
 */
public interface ValidateCodeGenerator {
    /**
     * 生成校验码
     * @param request 请求
     * @return 校验码
     */
    ValidateCode generate(ServletWebRequest request);
}
