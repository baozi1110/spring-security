/**
 * 
 */
package cn.qp.security.rbac.repository.support;

import org.springframework.core.convert.converter.Converter;

/**
 * @author zhailiang
 *
 */
public interface Domain2InfoConverter<T, I> extends Converter<T, I> {
	
}
