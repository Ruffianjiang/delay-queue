package com.dawn.delayqueue.redisson.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RDelayedQueue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/rest/test")
public class TestController {

    @Resource
    private RDelayedQueue<String> delayedQueue;

    @ApiOperation("添加延迟任务")
    @GetMapping(value = "/push")
    public String push(@ApiParam(name = "ttrTime", value = "延迟任务执行超时时间（单位：秒）", required = true, example = "5")
                       @RequestParam("ttrTime") Long ttrTime,
                       @ApiParam(name = "message", value = "消息内容", required = true)
                       @RequestParam("message") String message) {
        log.info("now:{}, ttrTime:{}, message:{}", DateFormat.getDateTimeInstance().format(new Date()), ttrTime, message);
        delayedQueue.offer(message, ttrTime, TimeUnit.SECONDS);
        return "success";
    }


}
