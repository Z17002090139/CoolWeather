package com.example.admin.myapplication;

public class ABC {
    public  static  int count=0;
    public  static void main(String[] args){


            (new Thread(){
                @Override
                public void run() {
                    System.out.println("hello world 1" );
                    while (true){
                        try {
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        pritCar();

                    }
                }
        }).start();

        (new Thread(){
            @Override
            public void run() {
                while (true) {
                    countCar();
                }
            }
        }).start();

    }
    synchronized static void pritCar(){
        if (count!=0) {
            System.out.println("过去的车辆数=>" + count);
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            count = 0;
        }
    }
    synchronized static void countCar(){
    count++;
    System.out.println("又过了一辆车");
    }
}
