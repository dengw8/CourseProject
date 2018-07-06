package com.example.duang1996.mvp_puzzle.eventBus.event;

/**
 * Created by duang1996 on 2018/4/16.
 */

public class MessageEvent{
    private String message;

    public MessageEvent() {}
    public  MessageEvent(String message){
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
