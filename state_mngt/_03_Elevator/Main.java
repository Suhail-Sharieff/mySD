/*


- 10 floors
- 3 elevators
- externally(on wall) users clicks he wants to go up or down
- internally(inside lift) user selects floor he wants to go to
- HERE we will use SCAN algo to control how ligts work, he it traverses UP or DOWN, while its moving it serves requests
- But we will use configurable algorithm to choose which lift i need to choose like Nearest,FIFO,FCFS etc
- user request (dirHeWantsToGo,floorNumber,internla/external)
- user operates with only 1 controller, it decides which elevator to forward request to(first it maches if req.dir == lift.dir ie its mving in same dir as reqested by user, then cheks if it already crossed that point in that dir or not and then chooses)
- each lift is running in own thread(infinitely run move() method) that checks if any request is in its tree set or not
- to manage states use State pattrn
- to control elevators use Command Pattern
- Controller servers a Facade to controll elevators, so Facade pattern
*/


package state_mngt._03_Elevator;

import java.util.List;

import state_mngt._03_Elevator.Controller.ElevatorController;
import state_mngt._03_Elevator.Entities.Elevator;
import state_mngt._03_Elevator.SchedulingAlgorithms.NearestElevator;
import state_mngt._03_Elevator.SchedulingAlgorithms.SchedulingAlgorithm;

public class Main {

    

    public static void main(String[] args) throws InterruptedException {
        
        new Main().run();


    }


    public void run(){
        Elevator e1=new Elevator("Elevator1",0);
        Elevator e2=new Elevator( "Elevator 2",7);
        Elevator e3=new Elevator("Elevator 3",4);

        List<Elevator>list=List.of(e1,e2,e3);

        SchedulingAlgorithm algo=new NearestElevator();

        ElevatorController controller=new ElevatorController(list, algo);
        

        System.out.println(controller);
        //usage

    }

  

    



    
}