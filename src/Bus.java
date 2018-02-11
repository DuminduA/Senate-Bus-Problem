import java.util.concurrent.Semaphore;

public class Bus extends Thread{

    private static int capacity = 50;
    private static Semaphore busArrival = new Semaphore(1);
    private static Semaphore GetOnBus = Rider.getBoardToTheBus();
    private static Semaphore busDeparture = new Semaphore(0);
    private static Semaphore Mutex = new Semaphore(1);

    public static Boolean isBusArrived() {
        return busArrived;
    }

    public static void setBusArrived(Boolean busArrived) {
        Bus.busArrived = busArrived;
    }

    private static Boolean busArrived = new Boolean(false);

    public static Boolean getLastBus() {
        return lastBus;
    }

    private static Boolean lastBus = new Boolean(false);
    private int BusID;

    public static int getCapacity() {
        return Bus.capacity;
    }

    public static Semaphore getBusDeparture() {
        return busDeparture;
    }

    public static Semaphore getBusArrival() {
        return busArrival;
    }

    public Bus(int busID){
        this.BusID = busID;
    }

    @Override
    public void run(){
        //let bus to arrive. only one at a time
        Mutex.acquireUninterruptibly();
        System.out.println("Waiting for a bus to arrive...............");
        try {
            //bus arrival
            arrive();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //if waiting riders are more than 0 bus let them to board
        if(Rider.getwaitingRiders() >0){
            //Semaphore to let get to the bus. Same semaphore boardToBus in Riders class
            GetOnBus.release();
            busDeparture.acquireUninterruptibly();
        }
        Mutex.release();

        try {
            depart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void arrive() throws InterruptedException {
        Thread.sleep(Main.getBusInterArrivalTime());
        busArrival.acquire();
        busArrived = true;
//        synchronized (busArrived){busArrived = true;}
        System.out.println("The Bus with ID " + this.BusID + " has Arrived..!!" );
    }

    public void depart() throws InterruptedException {
        System.out.println("The Bus with ID " + this.BusID + " has Departed..!!" );
//        synchronized (busArrived){ busArrived = false;}
        busArrived = false;
//        busArrival.release();
        lastBus = Main.getNumberOfBusses() == this.BusID + 1;    }
}
