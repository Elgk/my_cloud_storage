package model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Message implements Serializable {
    private Date sendAt;
    private String content;

    public Message(String content) {
        this.content = content;
        sendAt = new Date();
    }

}
