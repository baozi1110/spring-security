package cn.qp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理用户信息获取逻辑
 * 根据用户名去数据库校验，返回user对象，与传入的用户名和密码做比对
 *
 * @author BaoZi
 * @date 2019/10/3 9:42
 */
@Component
@Transactional
@Primary
public class DemoUserDetailsService implements UserDetailsService, SocialUserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据用户在页面表单填写的用户名登录
     *
     * @param username 用户在页面表单填写的用户名
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查找用户信息
        //根据查找到的用户信息判断用户是否被冻结
        //密码加密后返回，实际情况应该在注册时就进行加密存储，直接返回加密后的数据
        logger.info("登录用户名{}", username);
        return buildUser(username);
    }


    @Override
        public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        //根据用户id查找用户信息
        logger.info("设计登录用户Id:{}" , userId);
        return buildUser(userId);
    }

    private SocialUserDetails buildUser(String userId) {
        // 注意：实际上应该各自根据实际情况实现在数据库中查找用户的功能
        // 根据用户名查找用户信息
        // 根据查找到的用户信息判断用户是否被冻结
        String password = passwordEncoder.encode("123456");
        logger.info("数据库密码是:{}" , password);
        return new SocialUser(userId, password,
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_USER"));
    }
}

