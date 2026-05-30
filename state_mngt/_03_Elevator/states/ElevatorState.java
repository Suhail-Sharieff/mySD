package state_mngt._03_Elevator.states;

import state_mngt._03_Elevator.Entities.Elevator;
import state_mngt._03_Elevator.Entities.ElevatorRequest;
import state_mngt._03_Elevator.enums.Direction;

public interface ElevatorState {
    void move(Elevator e);
    void schedule(ElevatorRequest req,Elevator e);
    Direction getDir(Elevator e);
}
