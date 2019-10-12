package cn.qp.code;

import cn.qp.security.core.validate.code.image.ImageCode;
import cn.qp.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 自定义的验证码生成器
 * 指定bean名字为imageCodeGenerator后，就可以覆盖默认的生成器了
 * @author BaoZi
 * @date 2019/10/9 10:50
 */
// @Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {
    @Override
    public ImageCode generate(ServletWebRequest request) {
        System.out.println("更高级的图形验证码生成代码");
        return null;
    }
}
