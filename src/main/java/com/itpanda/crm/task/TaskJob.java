package com.itpanda.crm.task;

import com.itpanda.crm.services.CustomerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TaskJob {
    @Resource
    //注入定时任务所在类
    private CustomerService customerService;
    @Scheduled(cron = "0/10 * * * * ?")
    public void job(){
        System.out.println("客户流失定时任务执行---》"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        customerService.updateCustomerState();
    }
}
