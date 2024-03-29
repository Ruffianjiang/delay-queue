package com.dawn.delayqueue.controller;

import com.dawn.delayqueue.core.DelayQueue;
import com.dawn.delayqueue.core.model.DelayQueueJob;
import com.dawn.delayqueue.dto.Result;
import com.dawn.delayqueue.util.SnowflakeIdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Ruffianjiang
 * @version V1.0
 * @Title: DelayQueueController
 * @Package delay-queue
 * @date 2019/11/19 23:29
 */
@Api("延迟队列相关接口")
@RestController
public class DelayQueueController {

    private static SnowflakeIdUtil idUtil = new SnowflakeIdUtil(1, 1);

    @Autowired
    private DelayQueue delayQueue;


    @ApiOperation("添加延迟任务")
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public Result push(@ApiParam(name = "topic", value = "任务类型", required = true, example = "ql")
                       @RequestParam("topic") String topic,
                       @ApiParam(name = "delayTime", value = "延迟任务执行时间yyyy-MM-dd HH:mm:ss（自动转换13位时间时间戳）",
                               required = true, example = "2022-06-30 10:08:00")
                       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",  timezone = "GMT-8")
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                       @RequestParam("delayTime") Date delayTime,
                       @ApiParam(name = "ttrTime", value = "延迟任务执行超时时间（单位：秒）", required = true, example = "5")
                       @RequestParam("ttrTime") Long ttrTime,
                       @ApiParam(name = "message", value = "消息内容", required = true)
                       @RequestParam("message") String message) {
        DelayQueueJob delayQueueJob = new DelayQueueJob();
        delayQueueJob.setTopic(topic);
        delayQueueJob.setDelayTime(delayTime.getTime());
        delayQueueJob.setMessage(message);
        delayQueueJob.setTtrTime(ttrTime);
        delayQueueJob.setId(idUtil.nextId());
        delayQueue.push(delayQueueJob);
        return Result.sucess();
    }

    @ApiOperation("轮询队列获取任务")
    @RequestMapping(value = "/pop/{topic}", method = RequestMethod.GET)
    public Result pop(@ApiParam(name = "topic", value = "topic", required = true, example = "ql")
                          @PathVariable("topic") String topic) {
        DelayQueueJob delayQueueJob = delayQueue.pop(topic);
        return Result.sucess().put("data", delayQueueJob);
    }

    @ApiOperation("完成任务")
    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    public Result finish(@ApiParam(name = "id", value = "任务id", required = true) @RequestParam("id") Long id) {
        delayQueue.finish(id);
        return Result.sucess();
    }


}
