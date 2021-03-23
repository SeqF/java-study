package com.study.thequibbler.controller;

import com.google.common.collect.Range;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @author paksu
 */
@RestController("/redis")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/stringAndHash")
    public Map<String, Object> testStringAndHash() {
        redisTemplate.opsForValue().set("key1", "value1");
        //这里使用了JDK的序列化器，所以Redis保存时不是整数，不能进行运算
        redisTemplate.opsForValue().set("int_key", "1");
        stringRedisTemplate.opsForValue().set("int", "1");
        //使用运算
        stringRedisTemplate.opsForValue().increment("int");
        //获取底层Jedis连接
        Jedis jedis = (Jedis) stringRedisTemplate.getConnectionFactory()
                .getConnection().getNativeConnection();
        //-1操作，这个命令RedisTemplate不支持，所以先获取底层的连接在操作
        jedis.decr("int");
        Map<String, String> hashMap = new HashMap<>(16);
        hashMap.put("field1", "value1");
        hashMap.put("field2", "value2");
        //存入一个散列数据类型
        stringRedisTemplate.opsForHash().putAll("hash", hashMap);
        //新增一个字段
        stringRedisTemplate.opsForHash().put("hash", "field3", "value3");
        //绑定散列操作的key,这样可以连续对同一个散列数据类型进行操作
        BoundHashOperations hashOps = stringRedisTemplate.boundHashOps("hash");
        //删除两个字段
        hashOps.delete("field1", "field2");
        //新增一个字段
        hashOps.put("filed4", "value5");
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("success", true);
        return map;
    }

    @GetMapping("/list")
    public Map<String, Object> testList() {
        //插入两个列表，注意它们在链表的顺序
        //链表1从左到右顺序为v10,v8,v6,v4,v2
        stringRedisTemplate.opsForList().leftPushAll("list1", "v2", "v4", "v6", "v8", "v10");
        //链表2从左到右顺序为v1,v2,v3,v4,v5,v6
        stringRedisTemplate.opsForList().rightPushAll("list2", "v1", "v2", "v3", "v4", "v5", "v6");
        //绑定list2链表操作
        BoundListOperations listOps = stringRedisTemplate.boundListOps("list2");
        //从右边弹出一个成员
        Object result1 = listOps.rightPop();
        //获取定位元素,Redis从0开始计算，这里值为v2
        Object result2 = listOps.index(1);
        //从左边插入链表
        listOps.leftPush("v0");
        //求链表长度
        Long size = listOps.size();
        //求链表下标区间成员,整个链表下标范围为0到size-1，这里不取最后一个元素
        List elements = listOps.range(0, size - 2);
        Map<String, Object> map = new HashMap<>(16);
        map.put("success", true);
        return map;
    }

    @GetMapping("/set")
    public Map<String, Object> testSet() {
        //注意：这里v1重复两次，因为集合不允许重复，所以只是出入量5个成员到集合中
        stringRedisTemplate.opsForSet().add("set1", "v1", "v1", "v2", "v3", "v4", "v5");
        stringRedisTemplate.opsForSet().add("ser2", "v2", "v4", "v6", "v8");
        //绑定set1集合操作
        BoundSetOperations setOps = stringRedisTemplate.boundSetOps("set1");
        //增加两个元素
        setOps.add("v6", "v7");
        //删除两个元素
        setOps.remove("v1", "v7");
        //返回所有元素
        Set set1 = setOps.members();
        //求成员数
        Long size = setOps.size();
        //求交集
        Set inter = setOps.intersect("set2");
        //求交集，并且用新集合inter保存
        setOps.intersectAndStore("set2", "inter");
        //求差集
        Set diff = setOps.diff("set2");
        //求并集
        Set union = setOps.union("set2");
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    @GetMapping("/zSet")
    public Map<String, Object> testZset() {
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            //分数
            double score = i * 0.1;
            //创建一个TypedTuple对象，存入值和分数
            ZSetOperations.TypedTuple<String> typedTuple = new DefaultTypedTuple<>("value" + i, score);
            typedTupleSet.add(typedTuple);
        }
        //往有序集合插入元素
        stringRedisTemplate.opsForZSet().add("zset1", typedTupleSet);
        //绑定zset1有序集合操作
        BoundZSetOperations<String, String> zSetOps = stringRedisTemplate.boundZSetOps("zset1");
        //增加一个元素
        zSetOps.add("value10", 0.26);
        Set<String> setRange = zSetOps.range(1, 6);
        //按分数排序获取有序集合
        Set<String> setScore = zSetOps.rangeByScore(0.2, 0.6);
        //定义值范围
        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
        range.gt("value3"); //大于value3
        //range.gte("value3"); //大于等于value3
        //range.lt("value8"); //小于value8
        range.lte("value8"); //小于等于value8
        //按值排序，请注意这个排序是按字符串排序
        Set<String> setLex = zSetOps.rangeByLex(range);
        //删除元素
        zSetOps.remove("value9", "value2");
        //求分数
        Double score = zSetOps.score("value8");
        //在下标区间下，按分数排序，同时返回value和score
        Set<ZSetOperations.TypedTuple<String>> scoreSet = zSetOps.rangeByScoreWithScores(1, 6);
        //按从小到大排序
        Set<String> reverseSet = zSetOps.reverseRange(2, 8);
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    @GetMapping("/multi")
    public Map<String, Object> testMulti() {
        redisTemplate.opsForValue().set("key1", "value1");
        List list = (List) redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //设置要监控key1
                operations.watch("key1");
                //开启事务，在exec命令执行前，全部都只是进入队列
                operations.multi();
                operations.opsForValue().set("key2", "value2");
                operations.opsForValue().increment("key1", 1);
                //获取值将为null,因为redis只是把命令放入队列
                Object value2 = operations.opsForValue().get("key2");
                System.out.println("命令在队列，所以value为null【" + value2 + "】");
                operations.opsForValue().set("key3", "value3");
                Object value3 = operations.opsForValue().get("key3");
                System.out.println("命令在队列，所以value为null【" + value3 + "】");
                //执行exec命令，将先判别key1是否在监控后被修改过，如果是则不执行事务，否则就执行事务
                return operations.exec();
            }
        });
        System.out.println(list);
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("success", true);
        return map;
    }

    @GetMapping("/pipeline")
    public Map<String, Object> testPipeLine() {
        long start = System.currentTimeMillis();
        List list = (List) redisTemplate.executePipelined(new SessionCallback(){
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (int i = 1; i <= 100000; i++) {
                    operations.opsForValue().set("pipeline_" + i, "value_" + i);
                    String value = (String) operations.opsForValue().get("pipeline_" + i);
                    if (i == 100000) {
                        System.out.println("命令只是进入队列，所以值为空【" + value + "】");
                    }
                }
                return null;
            }
        });

        Long end = System.currentTimeMillis();
        System.out.println("耗时："+(end-start)+"毫秒");
        Map<String, Object> map = new HashMap<>(16);
        map.put("success", true);
        return map;
    }


}
