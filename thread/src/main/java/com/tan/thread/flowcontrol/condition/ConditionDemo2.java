package com.tan.thread.flowcontrol.condition;

import lombok.SneakyThrows;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tan.cs
 * @version 1.0
 * @description 用Condition实现生产者消费者模式
 * @date 2021/7/2 15:42
 **/
public class ConditionDemo2 {

    private int queueSize = 10;
    private PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);
    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public static void main(String[] args) {
        ConditionDemo2 conditionDemo2 = new ConditionDemo2();
        product product = conditionDemo2.new product();
        Consumer consumer = conditionDemo2.new Consumer();
        product.start();
        consumer.start();
    }
    
    class Consumer extends Thread{
        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true){
                lock.lock();
                try {
                    while (queue.size() == 0){
                        System.out.println("队列空，等待数据");
                        try {
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.poll();
                    notFull.signal();
                    System.out.println("从队列取走一个数据，队列剩余"+queue.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
    class product extends Thread{
        @Override
        public void run() {
            product();
        }

        private void product() {
            while (true){
                lock.lock();
                try {
                    while (queue.size() == queueSize){
                        System.out.println("队列已满，等消费数据");
                        try {
                            notFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(1);
                    notEmpty.signal();
                    System.out.println("从队列插入一个数据，队列剩余"+(queueSize-queue.size()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
