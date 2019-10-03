package cn.qp.security.browser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author BaoZi
 * @date 2019/10/3 9:42
 */
@Component
public class MyUserDetailsService implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查找用户信息
        //根据查找到的用户信息判断用户是否被冻结
        //密码加密后返回，实际情况应该在注册时就进行加密存储，直接返回加密后的数据
        logger.info("登录用户名{}",username);
        String encode = passwordEncoder.encode("123456");
        logger.info("加密后的密码{}",encode);
        return new User(username,encode,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
