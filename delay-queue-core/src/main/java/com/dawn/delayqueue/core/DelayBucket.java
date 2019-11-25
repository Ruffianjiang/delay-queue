package com.dawn.delayqueue.core;

import com.dawn.delayqueue.core.model.ScoredSortedItem;
import com.dawn.delayqueue.util.RedissonUtils;
import org.redisson.api.RScoredSortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一组以时间为维度的有序队列，用来存放所有需要延迟的DelayJob（这里只存放DelayJob Id）
 * @author Yang WenJie
 * @date 2018/1/27 上午12:41
 */
public class DelayBucket {

    private static final Logger logger = LoggerFactory.getLogger(DelayBucket.class);

    /**
     * 添加 DelayJob 到 延迟任务桶中
     * @param key
     * @param scoredSortedItem
     */
    public void addToBucket(String key, ScoredSortedItem scoredSortedItem) {
        RScoredSortedSet<ScoredSortedItem> scoredSortedSet = RedissonUtils.getScoredSorteSet(key);
        scoredSortedSet.add(scoredSortedItem.getDelayTime(),scoredSortedItem);
    }

    /**
     * 从延迟任务桶中获取延迟时间最小的 jodId
     * @param key
     * @return
     */
    public ScoredSortedItem getFromBucket(String key) {
        RScoredSortedSet<ScoredSortedItem> scoredSortedSet = RedissonUtils.getScoredSorteSet(key);
        if (scoredSortedSet.size() == 0) {
            return null;
        }
        return scoredSortedSet.first();
    }

    /**
     * 从延迟任务桶中删除 jodId
     * @param key
     * @param scoredSortedItem
     */
    public void deleteFormBucket(String key, ScoredSortedItem scoredSortedItem) {
        RScoredSortedSet<ScoredSortedItem> scoredSortedSet = RedissonUtils.getScoredSorteSet(key);
        scoredSortedSet.remove(scoredSortedItem);
    }
}
