package cn.qp.web.async;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 持有DeferredResult对象的类
 * @author BaoZi
 */
@Component
@Getter
@Setter
public class DeferredResultHolder {
    /**
     * key模拟订单号，value模拟处理结果
     */
    private Map<String, DeferredResult<String>> map = new HashMap<>();
}
