import java.util.Random;

public class Main {

    private static Random random = new Random();
    private static Integer numberOfBusses;
    private static Integer numberOfRiders;

    public static Integer getNumberOfRiders() {
        return numberOfRiders;
    }

    public static Integer getNumberOfBusses() {
        return numberOfBusses;
    }

    public static void main(String[] args) throws InterruptedException {

        int min =0;

        Random randomNumber = new Random();
        numberOfRiders = min + randomNumber.nextInt(1000);
        numberOfBusses = 0;
        if(numberOfRiders%Bus.getCapacity()==0){
            numberOfBusses = numberOfRiders/Bus.getCapacity();
        } else {
            numberOfBusses = numberOfRiders/Bus.getCapacity() + 1;
        }
        System.out.println(" number of Riders Created : " + numberOfRiders);
        System.out.println(" number of Busses Created : " + numberOfBusses);
        for(int i=0; i< numberOfBusses; i++){
            //Creating Busses. Enough to carry all the passengers
            Bus bus = new Bus(i);
            bus.start();
            int ridersCreated = 0;
            if(numberOfRiders < 60){
                for(int j=0; j< numberOfRiders; j++){
                    //Creating Riders
                    Rider rider = new Rider(j);
                    rider.start();
                    Thread.sleep(Main.getRiderInterArrivalTime());
                }
            } else {
                for(int j=0; j< 60; j++){
                    if(ridersCreated < numberOfRiders){
                        //Creating Riders
                        Rider rider = new Rider(j + 60*i);
                        rider.start();
                        ridersCreated+=1;
                        Thread.sleep(Main.getRiderInterArrivalTime());
                    } else {break;}
                }
            }
        }
    }

    //Control the rider arrival time
    public static long getRiderInterArrivalTime() {
        float  riderArrivalTime = 30f * 25;
        float lambda = 1 / riderArrivalTime;
        return Math.round(-Math.log(1 - random.nextFloat()) / lambda);
    }

    //Control the bus arrival time
    public static long getBusInterArrivalTime() {
        float busArrivalTime = 2 * 30f * 25 ;
        float lambda = 1 / busArrivalTime;
        return Math.round(-Math.log(1 - random.nextFloat()) / lambda);
    }
}
