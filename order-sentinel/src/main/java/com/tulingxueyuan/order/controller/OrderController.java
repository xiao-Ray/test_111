package com.tulingxueyuan.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.tulingxueyuan.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@RestController
@RequestMapping("/order")
public class OrderController {



    @RequestMapping("/flow")
    //@SentinelResource(value = "flow",blockHandler = "flowBlockHandler")
    public String flow() throws InterruptedException {
        return "正常访问";
    }


    public String flowBlockHandler(BlockException e){
        return "流控";
    }

    @RequestMapping("/flowThread")
    //@SentinelResource(value = "flowThread",blockHandler = "flowBlockHandler")
    public String flowThread() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        System.out.println("正常访问");
        return "正常访问";
    }


    // 关联流控   访问/add 触发/get
    @RequestMapping("/add")
    public String add(){
        System.out.println("下单成功!");
        return "生成订单";
    }
    // 关联流控  访问/add 触发/get
    @RequestMapping("/get")
    public String get() throws InterruptedException {
        return "查询订单";
    }

    @Autowired
    IOrderService orderService;

    // 关联流控   访问/add 触发/get
    @RequestMapping("/test1")
    public String test1(){
        return orderService.getUser();
    }
    // 关联流控  访问/add 触发/get
    @RequestMapping("/test2")
    public String test2() throws InterruptedException {
        return orderService.getUser();
    }


    @RequestMapping("/err")
    public String err()  {
        int a=1/0;
        return "hello";
    }


    /**
     * 热点规则，必须使用@SentinelResource
     * @param id
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/get/{id}")
    @SentinelResource(value = "getById",blockHandler = "HotBlockHandler")
    public String getById(@PathVariable("id") Integer id) throws InterruptedException {

        System.out.println("正常访问");
        return "正常访问";
    }

    public String HotBlockHandler(@PathVariable("id") Integer id,BlockException e) throws InterruptedException {

        return "热点异常处理";
    }

}
