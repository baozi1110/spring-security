package cn.qp.security.core.validate.code.image;
import cn.qp.security.core.validate.code.ValidateCode;
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
public class ImageCode extends ValidateCode {
    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, int expireIn){
        super(code, expireIn);
        this.image = image;
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
        super(code, expireTime);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }


}
