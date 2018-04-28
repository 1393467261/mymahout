package com.hzw.mymahout.entity;

/**
 * @Author: Hzw
 * @Time: 2018/4/21 15:28
 * @Description:
 */
public class Message {

    //用户id
    private Integer userId;

    //消息类型:点击click,付款pay,下单order,打折discount,用户申请客服applyservice,注册register,确认收货confirmreceipt
    private String messageType;

    //商品id
    private Integer itemId;

    //页面停留时间
    private Integer standingTime;

    @Override
    public String toString() {
        return "Message{" +
                "userId=" + userId +
                ", messageType='" + messageType + '\'' +
                ", itemId=" + itemId +
                ", standingTime=" + standingTime +
                '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getStandingTime() {
        return standingTime;
    }

    public void setStandingTime(Integer standingTime) {
        this.standingTime = standingTime;
    }

    public Message(Integer userId, String messageType, Integer itemId, Integer standingTime) {
        this.userId = userId;
        this.messageType = messageType;
        this.itemId = itemId;
        this.standingTime = standingTime;
    }
}
