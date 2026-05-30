package state_mngt._03_Elevator.Controller;

import java.util.List;

import state_mngt._03_Elevator.Entities.Elevator;
import state_mngt._03_Elevator.Entities.ElevatorRequest;
import state_mngt._03_Elevator.SchedulingAlgorithms.SchedulingAlgorithm;

public class ElevatorController{
        private final List<Elevator>elevators;
        private final SchedulingAlgorithm algorithm;
        public ElevatorController(List<Elevator> elevators, SchedulingAlgorithm algorithm) {
            this.elevators = elevators;
            this.algorithm=algorithm;
        }
        public void scheduleElevator(ElevatorRequest req){
            this.algorithm.schedule(elevators, req);
        }
        void shutDown(Elevator e){
            e.shutDown();
        }
    }
