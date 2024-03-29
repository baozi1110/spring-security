
package cn.qp.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 图片验证码配置项
 * 
 * @author BaoZi
 */
@Getter
@Setter
public class ImageCodeProperties extends SmsCodeProperties {

	/**
	 * 图片宽
	 */
	private int width = 67;
	/**
	 * 图片高
	 */
	private int height = 23;

	public ImageCodeProperties() {
		setLength(4);
	}

}
