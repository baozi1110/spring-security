package cn.qp.web.async;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 队列的监听器
 *
 * 模拟一个后台线程，循环监听队列MockQueue中是否有新的订单消息(新的或完成的)，
 * 一旦有消息就根据订单号进行订单处理，并将结果放入到 deferredResult中，该对象可以在线程间传递，从而将子线程的处理结果通过子线程返回
 *  ApplicationListener：应用监听器
 *  ContextRefreshedEvent：应用环境初始化或刷新时引发的事件
 * @author BaoZi
 */
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MockQueue mockQueue;

    @Autowired
    private DeferredResultHolder deferredResultHolder;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理应用程序事件
     * @param event 事件响应
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        new Thread(() -> {
            while (true) {

                if (StringUtils.isNotBlank(mockQueue.getCompleteOrder())) {

                    String orderNumber = mockQueue.getCompleteOrder();
                    logger.info("返回订单处理结果:"+orderNumber);
                    //模拟根据订单号获取deferredResult后将处理结果放入result中
                    deferredResultHolder.getMap().get(orderNumber).setResult("place order success");
                    mockQueue.setCompleteOrder(null);
                }else{
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
