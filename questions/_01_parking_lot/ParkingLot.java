package questions._01_parking_lot;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

enum VehicleType{
    small,
    medium,
    large
}

class Vehicle{
    int id;
    VehicleType type;
    Vehicle(int id,VehicleType type){this.id=id;this.type=type;}
}
class Ticket{
    final int id;
    final Vehicle vehicle;
    final Instant entry;
    Instant exit;
    final ParkingFloor floor;
    final ParkingSlot slot;
    public Ticket(int id,Vehicle v,ParkingFloor floor,ParkingSlot slot){this.id=id;this.vehicle=v;this.entry=Instant.now();this.floor=floor;this.slot=slot;}
    void setExit(Instant exit){this.exit=exit;}
}
class ParkingSlot{
    final int id;
    Vehicle parkedVechicle;
    final VehicleType supportedSize;
    final int dis;
    ParkingSlot(int id,VehicleType supported,int dis){this.id=id;this.supportedSize=supported;this.dis=dis;}
    void park(Vehicle v){this.parkedVechicle=v;}
    Vehicle unpark(){Vehicle v=this.parkedVechicle;this.parkedVechicle=null;return v;}
    boolean isEmpty(){return this.parkedVechicle==null;}
}
class AllocationFactory{
    static AllocationStrategy getAllocationStrategy(Vehicle v){
        if(v.type.equals(VehicleType.small))  return new RandomAllocationStrategy();
        return new NearestAllocationStrategy();
    }
}
class ParkingFloor{
    final int floorNumber;
    final List<ParkingSlot>slots;
    ParkingFloor(int floorNumber,List<ParkingSlot>slots){this.floorNumber=floorNumber;this.slots=slots;}
    void park(Vehicle v,AllocationStrategy strategy){
        ParkingSlot freeSlot=strategy.getSlot(v, slots);
        freeSlot.park(v);
        System.out.println(v+" allocated for "+freeSlot);
    }
    Vehicle unpark(ParkingSlot slot){
        Vehicle v=slot.unpark();
        System.out.println(v+" unparked from "+slot);
        return v;
    }

}
interface AllocationStrategy{
    ParkingSlot getSlot(Vehicle v,List<ParkingSlot>slots);
}
class NearestAllocationStrategy implements AllocationStrategy{
    @Override
    public ParkingSlot getSlot(Vehicle v, List<ParkingSlot> slots) {
        int minDis=Integer.MAX_VALUE;
        ParkingSlot ans=null;
        for(ParkingSlot slot:slots){
            if(!slot.isEmpty()) continue;
            if(!slot.supportedSize.equals(v.type)) continue;
            if(slot.dis<minDis){
                minDis=slot.dis;
                ans=slot;
            }
        }
        return ans;
    }
}
class RandomAllocationStrategy implements AllocationStrategy{
    @Override
    public ParkingSlot getSlot(Vehicle v, List<ParkingSlot> slots) {
        List<Integer>freeSlots=new ArrayList<>();
        int i=0;
        for(ParkingSlot slot:slots){
            if(slot.isEmpty()) freeSlots.add(i);
            i++;
        }
        Collections.shuffle(freeSlots);
        int sz=freeSlots.size();
        return slots.get(freeSlots.get(new Random().nextInt(0,sz)));
    }
}

interface BillingStrategy{
    int getTotal(Ticket t);
}
class DefaultBillingStrategy implements BillingStrategy{
    @Override
    public int getTotal(Ticket t) {
        return 100;
    }
}
class HourlyBillingStrategy implements BillingStrategy{
    @Override
    public int getTotal(Ticket t) {
        Instant entry=t.entry;
        Instant exit=t.exit;
        Duration parked=Duration.between(entry, exit);
        return 10*(parked.toHoursPart());
    }
}
class BillingFactory{
    static BillingStrategy getTotal(Ticket t){
        if(t.vehicle.type.equals(VehicleType.small)) return new DefaultBillingStrategy();
        return new HourlyBillingStrategy();
    }
}
interface FloorSelectionStrategy{
    ParkingFloor selectFloor(Vehicle v,List<ParkingFloor>floors);
}
class RandomFloorStartegy implements FloorSelectionStrategy{
    @Override
    public ParkingFloor selectFloor(Vehicle v, List<ParkingFloor> floors) {
        return floors.get(0);
    }
}
class FloorSelectionFactory{
    static FloorSelectionStrategy getFloorStrategy(Vehicle v,List<ParkingFloor>floors){
        return new RandomFloorStartegy();
    }
}
public class ParkingLot{
    final List<ParkingFloor>floors;
    final BillingStrategy billingStrategy;
    final AllocationStrategy allocStrategy;
    final FloorSelectionStrategy floorStrategy;
    ParkingLot(List<ParkingFloor>floors,BillingStrategy billingStrategy,AllocationStrategy allocStrategy,FloorSelectionStrategy floorStrategy){this.floors=floors;this.billingStrategy=billingStrategy;this.allocStrategy=allocStrategy;this.floorStrategy=floorStrategy;}
    Ticket getTicket(Vehicle v){
        ParkingFloor floorAssigned=floorStrategy.selectFloor(v, floors);
        ParkingSlot slotAssigned=allocStrategy.getSlot(v, floorAssigned.slots);
        slotAssigned.park(v);
        return new Ticket(new Random().nextInt(100), v, floorAssigned, slotAssigned);
    }
    void checkout(Ticket t){
        int totalBill=billingStrategy.getTotal(t);
        t.setExit(Instant.now());
        t.floor.unpark(t.slot);
        System.out.println(t+" checked out with "+totalBill);
    }
}
