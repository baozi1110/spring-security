package cn.qp.security.core.validate.code;
import lombok.Getter;
import lombok.Setter;

import	java.time.LocalDateTime;

import java.awt.image.BufferedImage;

/**
 * 封装的图形验证码的信息
 * @author BaoZi
 * @date 2019/10/8 17:11
 */
@Getter
@Setter
public class ImageCode {
    private BufferedImage image;
    private String code;
    private LocalDateTime expireTime;

    public ImageCode(BufferedImage image, String code, int expireIn){
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        this.image = image;
        this.code = code;
        this.expireTime = expireTime;
    }
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

}
