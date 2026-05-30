package state_mngt._03_Elevator.SchedulingAlgorithms;


import java.util.List;
import java.util.Optional;

import state_mngt._03_Elevator.Entities.Elevator;
import state_mngt._03_Elevator.Entities.ElevatorRequest;


 public interface SchedulingAlgorithm{
        Optional<Elevator> schedule(List<Elevator>elevators,ElevatorRequest req);
    }
