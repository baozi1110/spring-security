/**
 * 
 */
package cn.qp.web.controller;

import cn.qp.exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 控制器异常处理程序，会修改调用者接收到的异常返回信息，
 * 例：500的错误信息返回
 * {
 * "id": "1",
 * "message": "user not exist"
 * }
 * @author BaoZi
 */
@ControllerAdvice
public class ControllerExceptionHandler {

	/**
	 * UserNotExistException的异常在这个类里拦截加工
	 * 它对异常的处理在interceptor之前，该方法存在的时候interceptor.afterCompletion()拦截到的异常为null，
	 * 除非抛出的异常异常处理器中没有处理
	 */
	@ExceptionHandler(UserNotExistException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> handleUserNotExistException(UserNotExistException ex) {
		Map<String, Object> result = new HashMap<>();
		result.put("id", ex.getId());
		result.put("message", ex.getMessage());
		return result;
	}

}
