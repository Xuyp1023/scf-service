package com.betterjr.modules.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class JedisUtils {
    
    
    private static  JedisPool defaultJedisPool;
    
    
    
    public  JedisPool getDefaultJedisPool() {
        return defaultJedisPool;
    }

    public  void setDefaultJedisPool(JedisPool anDefaultJedisPool) {
        defaultJedisPool = anDefaultJedisPool;
    }

    private static Jedis getJedis() {
        return defaultJedisPool.getResource();
    }
    
    /***
     * 上锁
     * @param lock
     * @param seconds
     * @return
     */
    public synchronized static boolean acquireLock(String lock,int expired){
        Jedis jedis = null;
        boolean success = false;
        try{
            jedis = getJedis();
            long acquired = jedis.setnx(lock, "");
            success = acquired > 0 ? true : false;
            if(success){
                jedis.expire(lock, expired);
            }else{
                Thread.currentThread().sleep(expired*1000);
                acquireLock(lock, expired);
            }

        }catch(Exception e){
            success = false;
            e.printStackTrace();
        }finally {
            if(null !=jedis){
                jedis.close();
            }
        }
        return success;
    }
    
    /**
     * 释放锁
     * @param lock
     */
    public synchronized static void releaseLock(String lock) {
        Jedis jedis = null;
        try{
            jedis = getJedis();
            jedis.del(lock);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(null !=jedis){
                jedis.close();
            }
        }
    }  
    

}
