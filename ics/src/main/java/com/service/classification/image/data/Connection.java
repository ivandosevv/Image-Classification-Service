package com.service.classification.image.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Connection {
    @EmbeddedId
    private ConnectionKey id;

    @ManyToOne
    @MapsId("imageId")
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    @JsonProperty("en")
    private Tag tag;

    @Column(name = "confidence")
    @JsonProperty("confidence")
    private double confidence;

    @Column
    private String name;

    public Connection() {
    }

    public Connection(Image image, Tag tag, int confidence) {
        this.image = image;
        this.tag = tag;
        this.confidence = confidence;
        this.name = this.tag.getName();
    }

    public Connection(int imageId, int tagId, Image image, Tag tag, double confidence) {
        this.id = new ConnectionKey(imageId, tagId);
        //this.id = id;
        this.image = image;
        this.tag = tag;
        this.confidence = confidence;
    }

    public ConnectionKey getId() {
        return id;
    }

    public void setId(ConnectionKey id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Connection{" +
            "image=" + image +
            ", name=" + tag +
            ", confidence=" + confidence +
            '}';
    }
}
