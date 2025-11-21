package locks.tryLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShopperFaster extends Thread {

    private int itemsToAdd = 0; // items this shopper is waiting to add
    private static int itemsOnNotepad = 0; // total items on shared notepad
    private static Lock pencil = new ReentrantLock();

    public ShopperFaster(String name) {
        this.setName(name);
    }

    public void run() {
        while (itemsOnNotepad <= 20){
            if ((itemsToAdd > 0) && pencil.tryLock()) { // add item(s) to shared notepad
                try {
                    itemsOnNotepad += itemsToAdd;
                    System.out.println(this.getName() + " added " + itemsToAdd + " item(s) to notepad.");
                    itemsToAdd = 0;
                    Thread.sleep(300); // time spent writing
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    pencil.unlock();
                }
            } else { // look for other things to buy
                try {
                    Thread.sleep(100); // time spent searching
                    itemsToAdd++;
                    System.out.println(this.getName() + " found something to buy.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class TryLockDemoFaster {
    public static void main(String[] args) throws InterruptedException {
        Thread barron = new ShopperFaster("Barron");
        Thread olivia = new ShopperFaster("Olivia");
        long start = System.currentTimeMillis();
        barron.start();
        olivia.start();
        barron.join();
        olivia.join();
        long finish = System.currentTimeMillis();
        System.out.println("Elapsed Time: " + (float)(finish - start)/1000 + " seconds");
    }
}
