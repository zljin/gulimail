package com.zljin.gulimall.common.to.mq;

public class StockLockedTo {

    private Long id; //库存工作单的id
    private StockDetailTo detail;//工作详情的所有id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockDetailTo getDetail() {
        return detail;
    }

    public void setDetail(StockDetailTo detail) {
        this.detail = detail;
    }
}
