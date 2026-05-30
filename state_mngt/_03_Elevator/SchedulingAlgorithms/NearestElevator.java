package state_mngt._03_Elevator.SchedulingAlgorithms;

import java.util.List;
import java.util.Optional;

import state_mngt._03_Elevator.Entities.Elevator;
import state_mngt._03_Elevator.Entities.ElevatorRequest;
import state_mngt._03_Elevator.enums.Direction;

public class NearestElevator implements SchedulingAlgorithm{

        @Override//returns the nearest available elevator
        public Optional<Elevator> schedule(List<Elevator> elevators, ElevatorRequest req) {
            int tar=req.getDestFloor();
            Elevator best=null;
            int minDis=Integer.MAX_VALUE;
            for(Elevator o:elevators){
                if(canSatisfyThisRequest(o, req)){
                    int itsPos=o.getCurrentFloorPosition();
                    if(Math.abs(itsPos-tar)<minDis){
                        minDis=Math.abs(itsPos-tar);
                        best=o;
                    }
                }
            }
            return Optional.of(best);
        }
        private boolean canSatisfyThisRequest(Elevator elevator,ElevatorRequest request){
            if (elevator.getDir() == Direction.IDLE)
            return true;
        //choose the elevator whichs already moving in desired direction
            if (elevator.getDir() == request.getDir()) {
                if (request.getDir() == Direction.UP && elevator.getCurrentFloorPosition() <= request.getDestFloor())
                    return true;//still it havent reached me
                if (request.getDir() == Direction.DOWN && elevator.getCurrentFloorPosition() >= request.getDestFloor())
                    return true;
            }
            return false;
        }
    }