package cn.qp.security.core.validate.code;

import cn.qp.security.core.properties.SecurityProperties;
import cn.qp.security.core.validate.code.image.ImageCodeGenerator;
import cn.qp.security.core.validate.code.sms.DefaultSmsCodeSender;
import cn.qp.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码相关的扩展点配置。
 * 配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全模块默认的配置。
 * @author BaoZi
 * @date 2019/10/9 10:40
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 图片验证码图片生成器
     *
     * 相当于在ImageCodeGenerator上用@Component注解，优势是可以使用ConditionalOnMissingBean注解，
     * 该注解作用是当项目中不存在其他的 imageValidateCodeGenerator Bean时才使用下面的配置，这样就支持自定义了
     * @return codeGenerator
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return codeGenerator;
    }

    /**
     * 短信验证码发送器
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
