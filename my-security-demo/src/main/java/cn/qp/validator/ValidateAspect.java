/**
 * 
 */
package cn.qp.validator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.validation.BindingResult;

/**
 * @author Baozi
 *
 */
//@Aspect
//@Component
public class ValidateAspect {
	
	@Around("execution(* cn.qp.web.controller.UserController.*(..))")
	public Object handleValidateResult(ProceedingJoinPoint pjp) throws Throwable {
		
		System.out.println("进入切片");
		
		Object[] args = pjp.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult errors = (BindingResult)arg;
				if (errors.hasErrors()) {
					throw new ValidateException(errors.getAllErrors());
				}
			}
		}
		
		Object result = pjp.proceed();
		
		return result;
	}

}
