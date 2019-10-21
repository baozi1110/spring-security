package cn.qp.sso.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 处理用户信息获取逻辑
 * 根据用户名去数据库校验，返回user对象，与传入的用户名和密码做比对
 *
 * @author Baozi
 *
 */
@Component
public class SsoUserDetailsService implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new User(username, passwordEncoder.encode("123456"), 
				AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
	}

}
