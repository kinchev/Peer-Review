package com.telerik.peer.models;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "images")
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long id;

    @Lob
    @Column(name = "image")
    private byte[] image;

    public void setId(long id) {
        this.id = id;
    }

    public Image() {
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}


