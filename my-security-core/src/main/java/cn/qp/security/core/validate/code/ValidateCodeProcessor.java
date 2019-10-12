package cn.qp.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码处理器，封装不同校验码的所有处理逻辑
 * 
 * @author BaoZi
 */
public interface ValidateCodeProcessor {
	
	/**
	 * 验证码放入session时的前缀
	 */
	String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

	/**
	 * 创建校验码
	 *
	 * @param request spring的工具类，封装请求和响应
	 * @throws Exception e
	 */
	void create(ServletWebRequest request) throws Exception;

	/**
	 * 校验验证码
	 *
	 * @param servletWebRequest spring的工具类，封装请求和响应
	 * @throws Exception e
	 */
	void validate(ServletWebRequest servletWebRequest);

}
