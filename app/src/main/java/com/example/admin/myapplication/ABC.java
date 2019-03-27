package com.example.admin.myapplication;

public class ABC {
    public  static void main(String[] args){
        System.out.println("This is thread one");

        Thread thread=new Thread(){
            @Override
            public void run() {
                System.out.println("This is thread two");
            }
        };
        thread.start();
    }
}
