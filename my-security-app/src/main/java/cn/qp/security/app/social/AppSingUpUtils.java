package cn.qp.security.app.social;

import cn.qp.security.app.exception.AppSecretException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

/**
 * app环境下替换providerSignInUtils，避免由于没有session导致读不到社交用户信息的问题
 * @author Baozi
 *
 */
@Component
public class AppSingUpUtils {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	private UsersConnectionRepository usersConnectionRepository;
	
	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	/**
	 * 保存第三方用户连接信息到redis中
	 * @param connectionData 连接数据
	 */
	public void saveConnectionData(WebRequest request, ConnectionData connectionData) {
		redisTemplate.opsForValue().set(getKey(request), connectionData, 10, TimeUnit.MINUTES);
	}

	/**
	 * 用户注册完成后进行绑定的方法
	 * @param request 当前请求属性，用于从当前用户会话中提取登录尝试信息
	 * @param userId 本地应用程序的用户ID
	 */
	public void doPostSignUp(WebRequest request, String userId) {
		String key = getKey(request);
		if(!Boolean.TRUE.equals(redisTemplate.hasKey(key))){
			throw new AppSecretException("无法找到缓存的用户社交账号信息");
		}
		ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);
		Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId())
				.createConnection(connectionData);
		usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);
		redisTemplate.delete(key);
	}

	/**
	 * @param request 包含deviceId的请求信息，
	 * @return 根据设备id生成的key imooc:security:social.connect.xxx
	 */
	private  String getKey(WebRequest request) {
		String deviceId = request.getHeader("deviceId");
		if (StringUtils.isBlank(deviceId)) {
			throw new AppSecretException("设备id参数不能为空");
		}
		return "imooc:security:social.connect." + deviceId;
	}

}
