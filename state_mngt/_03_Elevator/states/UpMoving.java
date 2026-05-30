package state_mngt._03_Elevator.states;

import java.net.Authenticator.RequestorType;

import state_mngt._03_Elevator.Entities.Elevator;
import state_mngt._03_Elevator.Entities.ElevatorRequest;
import state_mngt._03_Elevator.enums.Direction;
import state_mngt._03_Elevator.enums.ElevatorRequestType;

public class UpMoving implements ElevatorState{

    @Override
    public Direction getDir(Elevator e) {
        return Direction.UP;
    }

    @Override
    public void move(Elevator e) {
        if(e.getUpReq().isEmpty()){
            //no more up left
            e.setCurrState(new IdleState());
            return;
        }
        //just mov to next floor
        int actualTarFloor=e.getUpReq().getFirst().getDestFloor();
        int nextFloor=e.getCurrentFloorPosition()+1;

        e.setCurrentFloorPosition(nextFloor);//move to that fllor

        if(nextFloor==actualTarFloor){
            System.out.println(e+" stopeed @ "+actualTarFloor);
            //rm it from upReq
            e.getUpReq().pollFirst();
        }
        //else since we are looping move() ultimatly we will rezch target floor

    }

    @Override
    public void schedule(ElevatorRequest req, Elevator e) {
       int src=e.getCurrentFloorPosition();
       int dest=req.getDestFloor();

       //if external cares about ie UP or DOWN and where to go
       if(req.getType()==ElevatorRequestType.EXTERNAL){
            if(req.getDir()==Direction.UP && src<=dest) e.getUpReq().add(req);
            else if(req.getDir()==Direction.DOWN && src>dest)e.getDownReq().add(req);
       }else{
            //internal cares only about floor positions, not up or down
            if(src<=dest) e.getUpReq().add(req);
            else if(src>dest) e.getDownReq().add(req);
       }
    }
    
}
