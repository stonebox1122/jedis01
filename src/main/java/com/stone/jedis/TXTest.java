package com.stone.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TXTest {
    public boolean transMethord() throws InterruptedException {
        Jedis jedis = new Jedis("172.30.60.64", 6379);
        int balance; // 余额
        int debt; // 欠额
        int amtToSubtract = 10; // 实刷额度

        jedis.set("balance", "100");
        jedis.set("debt", "0");

        jedis.watch("balance");
        Thread.sleep(7000);
        balance = Integer.parseInt(jedis.get("balance"));
        if (balance < amtToSubtract) {
            jedis.unwatch();
            System.out.println("modify");
            return false;
        } else {
            System.out.println("*********transaction***********");
            Transaction transaction = jedis.multi();
            transaction.decrBy("balance", amtToSubtract);
            transaction.incrBy("debt", amtToSubtract);
            transaction.exec();
            balance = Integer.parseInt(jedis.get("balance"));
            debt = Integer.parseInt(jedis.get("debt"));
            System.out.println("*******************" + balance);
            System.out.println("*******************" + debt);
            return true;
        }
    }
    /**
     * 通俗的讲，watch命令就是标记一个键，如果标记了一个键，
     * 在提交事务前如果该键被别人修改过，那事务就会失败，这种情况通常可以在程序中重新再尝试一次。
     * 首先标记了键balance，然后检查余额是否足够，不足就取消标记，并不做扣减；
     * 足够的话，就启动事务进行更新操作，如果在此期间键balance被其他人修改，那在提交事务(执行exec)时就会报错，
     * 程序中通常可以捕获这类错误再重新执行一次，直到成功。
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        TXTest test = new TXTest();
        boolean retValue = test.transMethord();
        System.out.println("main retValue--------- " + retValue);
    }
}
