package com.shf.yygh.service.task.scheduled;

import com.shf.yygh.common.rabbit.constant.MqConst;
import com.shf.yygh.common.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTask {
    @Autowired
    private RabbitService rabbitService;

    /**
      * 每天8点执行 提醒就诊
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0/30 * * * * ?")
    public void task1() {
        rabbitService.sendMessage(
                MqConst.EXCHANGE_DIRECT_TASK,
                MqConst.ROUTING_TASK_8,
                "");
    }

}
