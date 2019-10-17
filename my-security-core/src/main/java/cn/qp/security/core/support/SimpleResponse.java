/**
 * 
 */
package cn.qp.security.core.support;

import lombok.Data;

/**
 * @author BaoZi
 *
 */
@Data
public class SimpleResponse {
	
	public SimpleResponse(Object content){
		this.content = content;
	}
	
	private Object content;

}
