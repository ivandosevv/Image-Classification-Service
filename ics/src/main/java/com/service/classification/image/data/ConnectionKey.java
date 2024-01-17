package com.service.classification.image.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ConnectionKey implements Serializable {
    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "tag_id")
    private Integer tagId;

    public ConnectionKey(Integer imageId, Integer tagId) {
        this.imageId = imageId;
        this.tagId = tagId;
    }

    public ConnectionKey() {

    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}
