package org.example;

import java.util.concurrent.Callable;

class MyThread extends Thread implements Runnable, Callable<Integer> {
    String name;
    Long sleep;
    Integer count;
    final Thread thread;

    MyThread(String name, Long sleep) {
        this.name = name;
        this.sleep = sleep;
        thread = new Thread();
        thread.setName(name);
    }

    MyThread(String name, Long sleep, Integer count) {
        this.name = name;
        this.sleep = sleep;
        this.count = count;
        thread = new Thread();
        thread.setName(name);
    }

    @Override
    public Integer call() {
        Integer result = 0;
        int count = this.count;
        boolean interrupt = true;
        System.out.println(thread.getId() + ": " + thread.getName() + " started.");
        while (count > 0) {
            System.out.println(thread.getId() +
                    ": Hello from the " + thread.getName() + " count-" + count);
            result++;
            count--;

            try {
                Thread.sleep(this.sleep);
            } catch (InterruptedException e) {
                System.out.println(thread.getId() + ": " + thread.getName() + " interrupted.");
                interrupt = false;
                break;
            }
        }
        if (interrupt) System.out.println(thread.getId() + ": " + thread.getName() + " completed." +
                "\n\tAmount of messages displayed - " + result);
        return result;
    }

    @Override
    public void run() {
        System.out.println(thread.getId() + ": " + thread.getName() + " started.");
        while (!thread.isInterrupted()) {
            System.out.println(thread.getId() +
                    ": Hello from the " + thread.getName());
            try {
                Thread.sleep(this.sleep);
            } catch (InterruptedException e) {
                System.out.println(thread.getId() + ": " + thread.getName() + " interrupted.");
                return;
            }
        }
        System.out.println(thread.getId() + ": " + thread.getName() + " completed.");
    }
}