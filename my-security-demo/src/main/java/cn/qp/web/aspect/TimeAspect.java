package cn.qp.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 切片类
 *
 * @author BaoZi
 * @date 2019/9/29 10:05
 */
@Aspect
@Component
public class TimeAspect {

    /**
     * Around注解可以覆盖其他三个注解，before，after，AfterThrowing
     * execution表达式含义
     * * 任何返回值
     * 在UserController类中
     * * 任何方法
     * (..)任何参数 [针对重载的情况]
     *
     * @param pjp:包含拦截住方法的信息的对象
     * @return
     */
    @Around("execution(* cn.qp.web.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("time aspect start");

        //被拦截住的方法的参数数组
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            System.out.println("arg is " + arg);
        }

        long start = System.currentTimeMillis();
        //调用被拦截住的方法
        Object object = pjp.proceed();

        System.out.println("time aspect 耗时:" + (System.currentTimeMillis() - start));

        System.out.println("time aspect end");

        return object;
    }
}
