package state_mngt._03_Elevator.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import state_mngt._03_Elevator.enums.Direction;
import state_mngt._03_Elevator.observers.ElevatorObserver;
import state_mngt._03_Elevator.states.ElevatorState;
import state_mngt._03_Elevator.states.IdleState;
import utils.MyUtils;

//ignore max capacity of elevator for now
public class Elevator implements Runnable{

    private int currentFloorPosition;
    private final String id;
    private ElevatorState currState;
    private Direction dir;
    private final List<ElevatorObserver>myObservers;//to monitor me
    private final TreeSet<ElevatorRequest> upReq;
    private final TreeSet<ElevatorRequest> downReq;
    private volatile boolean isRunning=true;//to shut down elevator



    @Override
    public void run() {
        while(isRunning){
            move();
            MyUtils.sleep(2000);
        }
    }

    

    public Elevator(String id,int currentFloorPosition) {
        this.id = id;
        this.currentFloorPosition=currentFloorPosition;
        this.currState=new IdleState();
        this.dir=Direction.IDLE;
        this.myObservers=new ArrayList<>();
        this.upReq=new TreeSet<>((x,y)->x.getDestFloor()-y.getDestFloor());
        this.downReq=new TreeSet<>((x,y)->y.getDestFloor()-x.getDestFloor());
    }


    //-----for monitoring
    void addObserver(ElevatorObserver o){
        this.myObservers.add(o);
    }
    void removeObserver(ElevatorObserver o){
        this.myObservers.remove(o);
    }
    void notifyObserversAboutMe(){
        for(ElevatorObserver e:this.myObservers) e.getDetailsOf(this);
    }



    //-------actual funtionality
    void move(){
        this.currState.move(this);
    }
    synchronized void serveRequest(ElevatorRequest req){
        System.out.println(toString()+" serving "+req);
        this.currState.schedule(req, this);
    }
    


    

    public int getCurrentFloorPosition() {
        return currentFloorPosition;
    }



    public void setCurrentFloorPosition(int currentFloorPosition) {
        this.currentFloorPosition = currentFloorPosition;
    }



    public String getId() {
        return id;
    }

    public void shutDown() {
        this.isRunning = false;
    }

    public ElevatorState getCurrState() {
        return currState;
    }



    public void setCurrState(ElevatorState currState) {
        this.currState = currState;
        notifyObserversAboutMe();
    }



    public Direction getDir() {
        return dir;
    }

    public TreeSet<ElevatorRequest> getUpReq() {
        return upReq;
    }
    public TreeSet<ElevatorRequest> getDownReq() {
        return downReq;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
        notifyObserversAboutMe();
    }


    @Override
    public String toString() {
        return "Elevator [currentFloorPosition=" + currentFloorPosition + ", id=" + id + ", currState=" + currState
                + ", dir=" + dir + "]";
    }

    



    

}