package com.example.admin.myapplication;

public class ABC {
    public  static  int count=0;
    public  static void main(String[] args){

        Thread thread=new Thread(){
            @Override
            public void run() {
                System.out.println(count);
                count=0;
            }
        };
        thread.start();
        System.out.println("hello world");

            (new Thread(){
                @Override
                public void run() {
                    for (int  i=0;i<5;i++)
                        count=count+1;
                    System.out.println("+1");
                }
        }).start();

    }
}
