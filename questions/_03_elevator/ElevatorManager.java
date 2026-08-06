package questions._03_elevator;

import java.util.*;

enum Direction {
    UP, DOWN, IDLE
}

enum DoorState {
    OPEN, CLOSED
}

/* ---------------- REQUESTS ---------------- */

abstract class Request {}

class ExternalRequest extends Request {
    int floor;
    Direction direction;

    public ExternalRequest(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }
}

class InternalRequest extends Request {
    int destinationFloor;

    public InternalRequest(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }
}

/* ---------------- STATE ---------------- */

class ElevatorState {
    int currentFloor;
    Direction direction;
    DoorState doorState;

    ElevatorState(int floor) {
        this.currentFloor = floor;
        this.direction = Direction.IDLE;
        this.doorState = DoorState.CLOSED;
    }
}

/* ---------------- ELEVATOR ---------------- */

class Elevator {

    private final ElevatorState state;
    private final ElevatorController controller;

    public Elevator(int startFloor) {
        this.state = new ElevatorState(startFloor);
        this.controller = new ElevatorController(this);
    }

    public ElevatorState getState() {
        return state;
    }

    public ElevatorController getController() {
        return controller;
    }

    public void moveToFloor(int targetFloor) {

        if (targetFloor > state.currentFloor) {
            state.direction = Direction.UP;
        } else if (targetFloor < state.currentFloor) {
            state.direction = Direction.DOWN;
        }

        state.currentFloor = targetFloor;

        openDoor();
        closeDoor();

        if (!controller.hasPendingRequests()) {
            state.direction = Direction.IDLE;
        }
    }

    private void openDoor() {
        state.doorState = DoorState.OPEN;
    }

    private void closeDoor() {
        state.doorState = DoorState.CLOSED;
    }
}

/* ---------------- CONTROLLER ---------------- */

class ElevatorController {

    private final Elevator elevator;

    // SCAN scheduling
    private final TreeSet<Integer> upRequests = new TreeSet<>();
    private final TreeSet<Integer> downRequests =
            new TreeSet<>(Collections.reverseOrder());

    public ElevatorController(Elevator elevator) {
        this.elevator = elevator;
    }

    public void addExternalRequest(ExternalRequest request) {

        if (request.floor >= elevator.getState().currentFloor) {
            upRequests.add(request.floor);
        } else {
            downRequests.add(request.floor);
        }
    }

    public void addInternalRequest(InternalRequest request) {

        if (request.destinationFloor >= elevator.getState().currentFloor) {
            upRequests.add(request.destinationFloor);
        } else {
            downRequests.add(request.destinationFloor);
        }
    }

    public boolean hasPendingRequests() {
        return !upRequests.isEmpty() || !downRequests.isEmpty();
    }

    public void processNextRequest() {

        ElevatorState state = elevator.getState();

        if (state.direction == Direction.UP) {

            if (!upRequests.isEmpty()) {
                elevator.moveToFloor(upRequests.pollFirst());
                return;
            }

            state.direction = Direction.DOWN;
        }

        if (state.direction == Direction.DOWN) {

            if (!downRequests.isEmpty()) {
                elevator.moveToFloor(downRequests.pollFirst());
                return;
            }

            state.direction = Direction.UP;
        }

        if (state.direction == Direction.IDLE) {

            if (!upRequests.isEmpty()) {
                state.direction = Direction.UP;
                elevator.moveToFloor(upRequests.pollFirst());
                return;
            }

            if (!downRequests.isEmpty()) {
                state.direction = Direction.DOWN;
                elevator.moveToFloor(downRequests.pollFirst());
            }
        }
    }
}

/* ---------------- ASSIGNMENT STRATEGY ---------------- */

interface ElevatorAssignmentStrategy {
    Elevator assignElevator(List<Elevator> elevators,
                            ExternalRequest request);
}

class NearestElevatorStrategy
        implements ElevatorAssignmentStrategy {

    @Override
    public Elevator assignElevator(List<Elevator> elevators,
                                   ExternalRequest request) {

        Elevator best = null;
        int bestDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {

            ElevatorState state = elevator.getState();

            // idle elevator gets highest priority
            if (state.direction == Direction.IDLE) {

                int distance =
                        Math.abs(state.currentFloor - request.floor);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = elevator;
                }

                continue;
            }

            boolean movingTowardsRequest =
                    (state.direction == Direction.UP
                            && request.direction == Direction.UP
                            && state.currentFloor <= request.floor)

                    ||

                    (state.direction == Direction.DOWN
                            && request.direction == Direction.DOWN
                            && state.currentFloor >= request.floor);

            if (movingTowardsRequest) {

                int distance =
                        Math.abs(state.currentFloor - request.floor);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = elevator;
                }
            }
        }

        if (best != null) {
            return best;
        }

        // fallback nearest elevator
        for (Elevator elevator : elevators) {

            int distance =
                    Math.abs(elevator.getState().currentFloor
                            - request.floor);

            if (distance < bestDistance) {
                bestDistance = distance;
                best = elevator;
            }
        }

        return best;
    }
}

/* ---------------- MANAGER ---------------- */

public class ElevatorManager {

    private final List<Elevator> elevators;
    private final ElevatorAssignmentStrategy strategy;

    public ElevatorManager(List<Elevator> elevators,
                           ElevatorAssignmentStrategy strategy) {
        this.elevators = elevators;
        this.strategy = strategy;
    }

    public void submitRequest(ExternalRequest request) {

        Elevator elevator =
                strategy.assignElevator(elevators, request);

        elevator.getController()
                .addExternalRequest(request);
    }
}