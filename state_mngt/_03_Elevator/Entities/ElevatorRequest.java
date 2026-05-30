package state_mngt._03_Elevator.Entities;

import state_mngt._03_Elevator.enums.Direction;
import state_mngt._03_Elevator.enums.ElevatorRequestType;

public class ElevatorRequest {
    private final ElevatorRequestType type;//internal or external
    //if internal request then it will also have dest floor
    private final int destFloor;
    //if external request then it will hv only direction
    private final Direction dir;
    public ElevatorRequest(ElevatorRequestType type, int destFloor, Direction dir) {
        this.type = type;
        this.destFloor = destFloor;
        this.dir = dir;
    }
    @Override
    public String toString() {
        return "REQUEST [type=" + type + ", destFloor=" + destFloor + ", dir=" + dir + "]";
    }
    public int getDestFloor() {
        return destFloor;
    }
    public ElevatorRequestType getType() {
        return type;
    }
    public Direction getDir() {
        return dir;
    }
}
