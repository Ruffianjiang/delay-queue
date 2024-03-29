package com.dawn.delayqueue.core.model;

import java.io.Serializable;

/**
 * @author Yang WenJie
 * @date 2018/1/27 上午1:23
 */
public class ScoredSortedItem implements Serializable {

    private static final long serialVersionUID = 7534486240102254832L;
    /**
     * 任务的执行时间
     */
    private long delayTime;

    /**
     * 延迟任务的唯一标识
     */
    private long delayQueueJobId;

    public ScoredSortedItem(long delayQueueJobId, long delayTime) {
        this.delayQueueJobId = delayQueueJobId;
        this.delayTime = delayTime;
    }

    public ScoredSortedItem() {

    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public long getDelayQueueJobId() {
        return delayQueueJobId;
    }

    public void setDelayQueueJobId(long delayQueueJobId) {
        this.delayQueueJobId = delayQueueJobId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScoredSortedItem{");
        sb.append("delayTime=").append(delayTime);
        sb.append(", delayQueueJobId=").append(delayQueueJobId);
        sb.append('}');
        return sb.toString();
    }
}
