package cn.wentiehu.limiter.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import redis.clients.jedis.Jedis;

/**
 * @author wentiehu
 * @email tiehuwen@163.com
 * @date 2019/2/18 22:28
 */
public class Isacquire {
    //引入redis执行lua的脚本支持
    private DefaultRedisScript<Long> getRedisScript;

    //判断lua脚本是否成功，成功---没有限制，false---限制
    public boolean acquire(){
        //获取jedis连接
        Jedis jedis = new Jedis("120.78.161.231", 6379);
        jedis.auth("Xieying888");
        getRedisScript= new DefaultRedisScript<>();
        //设置Lua脚本的返回值类型Long
        getRedisScript.setResultType(Long.class);
        //加载Lua脚本
        getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimiter.lua")));
        String limitKey = "limit-key";
        //执行Lua脚本(这里可以保证执行脚本的原子性) ，60秒 限制访问10次
        Long eval = (Long) jedis.eval(getRedisScript.getScriptAsString(), 1, limitKey, "10","60");
        if (eval==0) {//被限流了，改变方法的处理结果
            System.out.println("被分布式系统限流了");
            return false;
        }
        //返回为1，不为0的话，正常调用原有的方法
        return true;
    }
}
