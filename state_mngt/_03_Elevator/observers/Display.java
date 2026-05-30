package state_mngt._03_Elevator.observers;

import state_mngt._03_Elevator.Entities.Elevator;

public class Display implements ElevatorObserver {
    @Override
    public void getDetailsOf(Elevator elevator) {
        System.out.println("[DISPLAY] Elevator " + elevator.getId() +
                " | Current Floor: " + elevator.getCurrentFloorPosition() +
                " | Direction: " + elevator.getDir());
    }
}