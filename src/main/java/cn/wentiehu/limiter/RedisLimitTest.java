package cn.wentiehu.limiter;

import redis.clients.jedis.Jedis;

/**
 * @author wentiehu
 * @email tiehuwen@163.com
 * @date 2019/2/18 22:16
 */
public class RedisLimitTest {


    public static void main(String[] args) {
        boolean result = true;//判断是否限流

        //获取jedis连接
        Jedis jedis = new Jedis("120.78.161.231", 6379);
        jedis.auth("Xieying888");
        //限流的key
        String key = "limit-key";
        if (jedis.exists(key)) {
            Long aLong = jedis.incr(key);//对key的value值进行+1操作
            if (aLong>10) {
                result = false;
            }
        }else{
            //如果不存在
            jedis.incr(key);//对key的value值进行+1操作
            jedis.expire(key,60);
        }
        System.out.println("限流结果："+result);
    }

}
