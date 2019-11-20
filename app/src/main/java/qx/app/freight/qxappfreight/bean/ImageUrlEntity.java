package qx.app.freight.qxappfreight.bean;

import lombok.Data;

@Data
public class ImageUrlEntity {

    private String imageUrl;

    private boolean net;

    public ImageUrlEntity(String imageUrl, boolean net) {
        this.imageUrl = imageUrl;
        this.net = net;
    }
}
