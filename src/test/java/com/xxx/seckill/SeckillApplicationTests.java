package com.xxx.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillApplicationTests {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RedisScript<Boolean> script;

    @Test
    void testLock01() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        if(isLock){
            valueOperations.set("name","zs");
            String name = (String) valueOperations.get("name");
            System.out.println("name = "+name);
            int i =0/0;
            //操作结束,删除锁
            redisTemplate.delete("k1");
        }else{
            System.out.println("有线程在使用,请稍后");
        }
    }
    @Test
    public void testLock02(){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //给锁添加过期时间,防止应用运行期间抛出异常导致锁无法正常释放
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1", 5, TimeUnit.SECONDS);
        if(isLock){
            valueOperations.set("name","zs");
            String name = (String) valueOperations.get("name");
            System.out.println("name = "+name);
            Integer.parseInt("xxx");
            redisTemplate.delete("k1");
        }else {
            System.out.println("有线程在使用,请稍后再试");
        }
    }

    @Test
    public void testLock03(){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String value = UUID.randomUUID().toString();
        Boolean isLock = valueOperations.setIfAbsent("k1", value, 5, TimeUnit.SECONDS);
        if(isLock){
            valueOperations.set("name","zs");
            System.out.println("name="+ valueOperations.get("name"));
            Integer.parseInt("xxx");
            //操作结束,删除锁
            Boolean res = redisTemplate.execute(script, Collections.singletonList("k1"),value);
            System.out.println(res  );
        }else{
            System.out.println("有线程在使用,请稍后再试");
        }
    }

}
