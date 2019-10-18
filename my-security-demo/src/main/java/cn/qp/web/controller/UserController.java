package cn.qp.web.controller;

import cn.qp.dto.User;
import cn.qp.dto.UserQueryCondition;
import cn.qp.security.app.social.AppSingUpUtils;
import cn.qp.security.core.properties.SecurityProperties;
import com.fasterxml.jackson.annotation.JsonView;
import io.jsonwebtoken.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 访问接口
 *
 * @author BaoZi
 * @date 2019/9/27 11:24
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private AppSingUpUtils appSingUpUtils;

    @Autowired
    private SecurityProperties securityProperties;

    @PostMapping("/regist")
    public void regist(User user, HttpServletRequest request) {
        //不管是注册用户还是绑定用户，都会拿到一个用户唯一标识。
        String userId = user.getUsername();
        //将userId传递给springsocial，这个userId是应用自己维护的用户表，与spring无关
        // providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
        appSingUpUtils.doPostSignUp(new ServletWebRequest(request), userId);

    }

    /**
     * 获取认证信息的几种方式
     *  1.通过 SecurityContextHolder.getContext().getAuthentication() 返回所有信息
     *  2.方法参数使用 Authentication authentication,返回authentication对象，SpringSecurity的框架会自动赋值，返回所有信息
     *  3.使用注解 @AuthenticationPrincipal UserDetails userDetails，返回的是principal的信息，即只有登录时用户的验证信息
     */
//     @GetMapping("/me")
//     public Object getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
//         // return SecurityContextHolder.getContext().getAuthentication();
//
// //		String token = StringUtils.substringAfter(request.getHeader("Authorization"), "bearer ");
// //		Claims claims = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes("UTF-8"))
// //					.parseClaimsJws(token).getBody();
// //		String company = (String) claims.get("company");
// //		System.out.println(company);
//
//         return userDetails;
//     }

    /**
     * 使用jwtToken获取用户信息
     */
    @GetMapping("/me")
    public Object getCurrentUser(Authentication user, HttpServletRequest request) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {

        // String token = StringUtils.substringAfter(request.getHeader("Authorization"), "bearer ");
        String token = StringUtils.substringAfter(request.getHeader("Authorization"), "Bearer ");

        Claims claims = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody();
        //获取jwtToken中的信息
        String company = (String) claims.get("company");

        System.out.println(company);

        return user;
    }

    @PostMapping
    @ApiOperation(value = "创建用户")
    public User create(@Valid @RequestBody User user) {

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());

        user.setId("1");
        return user;
    }

    @PutMapping("/{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult errors) {

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());

        user.setId("1");
        return user;
    }

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id) {
        System.out.println(id);
    }

    @GetMapping
    @JsonView(User.UserSimpleView.class)
    @ApiOperation(value = "用户查询服务")
    public List<User> query(UserQueryCondition condition,
                            @PageableDefault(page = 2, size = 17, sort = "username,asc") Pageable pageable) {

        System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));

        System.out.println(pageable.getPageSize());
        System.out.println(pageable.getPageNumber());
        System.out.println(pageable.getSort());

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        return users;
    }

    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getInfo(@ApiParam("用户id") @PathVariable String id) {
        // throw new UserNotExistException(id);
        System.out.println("进入getInfo服务");
        User user = new User();
        user.setUsername("tom");
        return user;
    }
}
