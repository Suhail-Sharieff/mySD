package state_mngt._03_Elevator.states;

import state_mngt._03_Elevator.Entities.Elevator;
import state_mngt._03_Elevator.Entities.ElevatorRequest;
import state_mngt._03_Elevator.enums.Direction;
import state_mngt._03_Elevator.enums.ElevatorRequestType;

public class DownMoving implements ElevatorState{

    @Override
    public Direction getDir(Elevator e) {
        return Direction.DOWN;
    }

    @Override
    public void move(Elevator e) {
        if(e.getDownReq().isEmpty()){//no where to move down
            e.setCurrState(new IdleState());
            return;
        }
        int actualTarFloor=e.getDownReq().getFirst().getDestFloor();
        int nextFloor=e.getCurrentFloorPosition()+1;

        e.setCurrentFloorPosition(nextFloor);

        if(actualTarFloor==nextFloor){
            System.out.println(e+" stopeed @ "+actualTarFloor);
            //rm it from downReq
            e.getDownReq().pollFirst();
        }
    }

    @Override
    public void schedule(ElevatorRequest req, Elevator e) {
        int src=e.getCurrentFloorPosition();
        int dest=req.getDestFloor();

        if(req.getType()==ElevatorRequestType.EXTERNAL){
            if(req.getDir()==Direction.DOWN && dest<=src) e.getDownReq().add(req);
            else if(req.getDir()==Direction.UP && dest>src) e.getUpReq().add(req);
        }else{
            if(src<=dest) e.getDownReq().add(req);
            else e.getUpReq().add(req);
        }
    }
    
}
