package cn.qp.web.async;


import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 模拟消息队列
 * @author BaoZi
 */
@Getter
@Setter
@Component
public class MockQueue {
    /**
     * 模拟下单消息
     */
    private String placeOrder;

    /**
     * 模拟订单完成消息
     */
    private String completeOrder;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public String getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(String placeOrder) throws Exception {
        new Thread(() -> {
            logger.info("接到下单请求, " + placeOrder);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.completeOrder = placeOrder;
            logger.info("下单请求处理完毕," + placeOrder);
        }).start();
    }
}
