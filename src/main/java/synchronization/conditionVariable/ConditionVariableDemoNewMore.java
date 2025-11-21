package synchronization.conditionVariable;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class HungryPerson2 extends Thread {

    private int personID;
    private static Lock slowCookerLid = new ReentrantLock();
    private static int servings = 11;
    private static Condition soupTaken = slowCookerLid.newCondition();

    public HungryPerson2(int personID) {
        this.personID = personID;
    }

    public void run() {
        while (servings > 0) {
            slowCookerLid.lock();
            try {
                while ((personID != servings % 5) && servings > 0) { // check if it's not your turn
                    System.out.format("Person %d checked... then put the lid back.\n", personID);
                    soupTaken.await();
                }
                if (servings > 0) {
                    servings--; // it's your turn - take some soup!
                    System.out.format("Person %d took some soup! Servings left: %d\n", personID, servings);
                    soupTaken.signalAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                slowCookerLid.unlock();
            }
        }
    }
}

public class ConditionVariableDemoNewMore {
    public static void main(String args[]) {
        for (int i=0; i<5; i++)
            new HungryPerson2(i).start();
    }
}