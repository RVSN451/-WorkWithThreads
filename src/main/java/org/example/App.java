package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class App {

    public static List<Callable<Integer>> createCallableList() {
        List<Callable<Integer>> myCallableList = new ArrayList<>();


        myCallableList.add(new MyThread("MyCallable_1", 500L, 3));
        myCallableList.add(new MyThread("MyCallable_2", 1500L, 4));
        myCallableList.add(new MyThread("MyCallable_3", 150L, 5));
        myCallableList.add(new MyThread("MyCallable_4", 750L, 7));

        return myCallableList;
    }

    public static void counterInterThreadDialogueInvokeAll() throws ExecutionException, InterruptedException {
        System.out.println("\nМЕЖПОТОЧНЫЙ ДИАЛОГ СО СЧЕТЧИКОМ\n" +
                "\tПодсчёт суммы всех сообщений,выведенных в консоль.\n");

        Thread t = Thread.currentThread();
        t.setName("InterThreadDialogueCount_HeadThread");
        System.out.println(t.getId() + ": " + t.getName() + " started.");

        List<Callable<Integer>> myCallableList = createCallableList();


        final ExecutorService threadPool = Executors.newFixedThreadPool(4);
        final List<Future<Integer>> callTasks = threadPool.invokeAll(myCallableList);
        System.out.println(t.getId() + ": " + t.getName() + " completed.");

        int totalMessages = 0;
        for (Future<Integer> future : callTasks) {
            totalMessages = totalMessages + future.get();
        }
        threadPool.shutdown();

        System.out.println("Total amount of messages displayed - " + totalMessages);
    }

    public static void counterInterThreadDialogueInvokeAny() throws ExecutionException, InterruptedException {


        System.out.println("\nМЕЖПОТОЧНЫЙ ДИАЛОГ СО СЧЕТЧИКОМ\n" +
                "\tПодсчёт сообщений,выведенных в консоль первой успешно выполненной задачей.\n");

        Thread t = Thread.currentThread();
        t.setName("InterThreadDialogueCount_HeadThread");
        System.out.println(t.getId() + ": " + t.getName() + " started.");

        List<Callable<Integer>> myCallableList = createCallableList();


        final ExecutorService threadPool = Executors.newFixedThreadPool(4);
        final Integer callTask = threadPool.invokeAny(myCallableList);

        threadPool.shutdown();
        System.out.println(t.getId() + ": " + t.getName() + " completed.");

        System.out.println("Amount of messages displayed by the first successfully completed task - " + callTask);
    }

    public static void interThreadDialogue() {

        System.out.println("\nМЕЖПОТОЧНЫЙ ДИАЛОГ \n");

        Thread t = Thread.currentThread();
        t.setName("InterThreadDialogue_HeadThread");
        System.out.println(t.getId() + ": " + t.getName() + " started.");

        ThreadGroup interThreadDialogueGroup = new ThreadGroup("InterThreadDialogueGroup");

        new Thread(interThreadDialogueGroup, new MyThread("Thread_1", 700L)).start();
        new Thread(interThreadDialogueGroup, new MyThread("Thread_2", 580L)).start();
        new Thread(interThreadDialogueGroup, new MyThread("Thread_3", 620L)).start();
        MyThread peculiarThread = new MyThread("PeculiarThread", 400L);
        peculiarThread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            return;
        }

        int actThreads = interThreadDialogueGroup.activeCount();
        System.out.println("The number of active threads in this thread (estimate) - " + actThreads);


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            return;
        }

        interThreadDialogueGroup.interrupt();
        peculiarThread.interrupt();

        System.out.println(t.getId() + ": " + t.getName() + " completed.");

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        interThreadDialogue();
        counterInterThreadDialogueInvokeAll();
        counterInterThreadDialogueInvokeAny();
    }
}