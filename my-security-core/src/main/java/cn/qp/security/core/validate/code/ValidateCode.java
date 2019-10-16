package cn.qp.security.core.validate.code;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 验证码信息封装类
 * 需要进行序列化，因为验证码信息放入到了session中，session使用了redis，Redis中的数据都需要是可序列化的
 * @author BaoZi
 */
public class ValidateCode implements Serializable {
    private String code;

    private LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ValidateCode(String code, LocalDateTime expireTime){
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

}
