package com.company;

import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        MyBuffer buffer = new MyBuffer();
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        }catch(Exception e){}
    }
}

class MyBuffer{
    private final int LIMIT = 3;
    LinkedList<Integer> list = new LinkedList<>();


    public synchronized int consume(){
        while(list.size() == 0){
            try {
                wait();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        notify();
        return list.removeFirst();
    }
    public synchronized void produce(int data){
        while(list.size() == this.LIMIT){
            try {
                wait();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        notify();
        list.add(data);
    }
}

class Consumer extends Thread{
    private MyBuffer buffer;
    private final int ROUNDS = 10;

    Consumer(MyBuffer buffer){
        this.buffer = buffer;
    }

    @Override
    public void run(){
        int returnValue;
        for(int i = 0 ; i < ROUNDS; i++){
            try {
                Thread.sleep((int)(2000 * Math.random()));
            }catch(Exception e){}

            System.out.println(System.currentTimeMillis() +" "+" <---");
            returnValue = this.buffer.consume();
            System.out.println(System.currentTimeMillis() +" "+ returnValue +" <--- gelesen");
        }
    }
}

class Producer extends Thread{
    private MyBuffer buffer;
    private final int ROUNDS = 10;

    Producer(MyBuffer buffer){
        this.buffer = buffer;
    }

    @Override
    public void run(){
        for(int i = 0 ; i < ROUNDS; i++){
            try {
                Thread.sleep((int)(2000 * Math.random()));
            }catch(Exception e){}

            System.out.println(System.currentTimeMillis() +" "+i+" --->");
            this.buffer.produce(i);
            System.out.println(System.currentTimeMillis() +" "+i+" ---> geschrieben");
        }
    }
}