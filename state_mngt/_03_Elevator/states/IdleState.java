package state_mngt._03_Elevator.states;

import state_mngt._03_Elevator.Entities.Elevator;
import state_mngt._03_Elevator.Entities.ElevatorRequest;
import state_mngt._03_Elevator.enums.Direction;

public class IdleState implements ElevatorState{

    @Override
    public Direction getDir(Elevator e) {
        return Direction.IDLE;
    }

    @Override
    public void move(Elevator e) {
        if(!e.getUpReq().isEmpty()) e.setCurrState(new UpMoving());
        else if(!e.getDownReq().isEmpty()) e.setCurrState(new DownMoving());
    }

    @Override
    public void schedule(ElevatorRequest req, Elevator e) {
        int tarFloor=req.getDestFloor();
        int currFloor=e.getCurrentFloorPosition();
        if(tarFloor==currFloor) return;
        if(tarFloor>currFloor) e.getUpReq().add(req);
        else e.getDownReq().add(req);        
    }

    
}