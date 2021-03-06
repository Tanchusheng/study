package com.tan.thread.flowcontrol.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tan.cs
 * @version 1.0
 * @description 模拟100跑步，5名选手都准备好了，只等裁判一声令下，所有人同时开始跑步 当所有人都到达终点之后比赛结束
 * @date 2021/7/2 14:30
 **/
public class CountDownLatchDemo1And2 {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i +1;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("No" + no + "准备完毕，等待发令抢");
                    try {

                        System.out.println("No" + no+"开始跑步");
                        Thread.sleep((long) (Math.random()*10000));
                        System.out.println("No" + no+"跑到终点");
                        begin.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        end.countDown();
                    }
                }
            };
            executorService.submit(runnable);
        }
        // 裁判员检查发令枪
        Thread.sleep(5000);
        System.out.println("发令枪响，比赛开始");
        begin.countDown();

        end.await();
        System.out.println("所有人到达终点，比赛结束");

    }
}
