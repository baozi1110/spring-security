package cn.qp.security.browser.support;

/**
 * @author BaoZi
 * @date 2019/10/3 21:41
 */
public class SimpleResponse {
    public SimpleResponse(Object content) {
        this.content = content;
    }

    private Object content;

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
