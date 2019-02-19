package cn.wentiehu.limiter.controller;

import cn.wentiehu.limiter.utils.Isacquire;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wentiehu
 * @email tiehuwen@163.com
 * @date 2019/2/18 21:52
 */
@RestController
public class limitingController {

    //guava 令牌桶 创建令牌桶，并在桶中生成2个令牌,
    // 第一次执行的时候会从令牌桶中获取4个令牌，2个是缓冲区的令牌，
    // 在后期会逐渐恢复一秒限制两个请求。
    final RateLimiter rateLimiter = RateLimiter.create(2);

    @RequestMapping("limiting")
    public String limiting(@RequestParam String str){

        //限流方法的判断 ，为true没有限制
        if (rateLimiter.tryAcquire(1)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            System.out.println("处理时间："+format.format(new Date())+",业务处理-------"+str);
            return "操作成功";
        }else {
            System.out.println("对不起，你的请求被限制了！");
            return "对不起，你的请求被限制了！";
        }
    }


    @RequestMapping("redisLimiter")
    public String redisLimiter(@RequestParam String str){

        //实例化分布式限流的类
        Isacquire isacquire = new Isacquire();
        //限流方法的判断为true时，没有限制
        if (isacquire.acquire()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            System.out.println("处理时间："+format.format(new Date())+",业务处理-------"+str);
            return "分布式请求操作成功";
        }else {
            System.out.println("对不起，你的分布式请求被限制了！");
            return "对不起，你的分布式请求被限制了！";
        }
    }
}
