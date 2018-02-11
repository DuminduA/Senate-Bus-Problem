import java.util.concurrent.Semaphore;

public class Rider extends Thread{

    private int RiderID;
    private static Semaphore MaxRiders = new Semaphore(50);
    private static Semaphore Mutex = new Semaphore(1);
    private static Semaphore boardToTheBus = new Semaphore(0);
    private static int waitingRiders = 0;
    private static int waitingRidersPerBus = 0;
    private static boolean boarding;

    public static int getwaitingRiders() {
        return waitingRiders;
    }

    public static Semaphore getBoardToTheBus() { return boardToTheBus;}


    public Rider(int RiderID){
        this.RiderID = RiderID;
    }

    @Override
    public void run() {
        try {
            System.out.println("The Rider " + this.RiderID + " Trying to get to the waiting area");
            //Allow riders to enter waiting area
            MaxRiders.acquire();

            //Enter a rider to the waiting area
            Mutex.acquireUninterruptibly();
            System.out.println("The Rider " + this.RiderID + " is in the waiting area");

            //when all the busses gone and riders came to the waiting area the process terminates
            if(Bus.getLastBus() && this.RiderID == Main.getNumberOfRiders()-1){
                System.out.println("All the busses left, Process terminating.....!!!!!!!!!!!!!!!!!!!!");
                System.exit(0);
            }

            synchronized ((Integer)waitingRiders){ waitingRiders +=1;}
            synchronized ((Integer)waitingRiders){ waitingRidersPerBus +=1;}
            Mutex.release();

            //allow rider to enter the bus
            boardToTheBus.acquireUninterruptibly();
            boardBus();


            waitingRiders -=1;

            synchronized ((Integer) waitingRiders){
                if(waitingRiders == 0){
                    Bus.getBusDeparture().release();
                    Bus.getBusArrival().release();

                    int temp = waitingRidersPerBus;
                    waitingRidersPerBus = 0;
                    //Releasing the Semaphore and increment the permits by 1
                    MaxRiders.release(temp);
                } else {
                    //release so other rider can board
                    boardToTheBus.release();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void boardBus(){
        synchronized (Bus.isBusArrived()){
            while(true){
                if(Bus.isBusArrived()){
                    break;
                }
            }
            boarding = true;
            if(Bus.isBusArrived()){
                System.out.println("The Rider " + this.RiderID + " is boarded");
            }
        }
    }
}
