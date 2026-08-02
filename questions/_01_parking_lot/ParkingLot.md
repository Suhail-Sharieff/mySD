classDiagram
direction BT
class AllocationFactory {
  ~ getAllocationStrategy(Vehicle) AllocationStrategy
}
class AllocationStrategy {
<<Interface>>
  + getSlot(Vehicle, List~ParkingSlot~) ParkingSlot
}
class BillingFactory {
  ~ getTotal(Ticket) BillingStrategy
}
class BillingStrategy {
<<Interface>>
  + getTotal(Ticket) int
}
class DefaultBillingStrategy {
  + getTotal(Ticket) int
}
class FloorSelectionFactory {
  ~ getFloorStrategy(Vehicle, List~ParkingFloor~) FloorSelectionStrategy
}
class FloorSelectionStrategy {
<<Interface>>
  + selectFloor(Vehicle, List~ParkingFloor~) ParkingFloor
}
class HourlyBillingStrategy {
  + getTotal(Ticket) int
}
class NearestAllocationStrategy {
  + getSlot(Vehicle, List~ParkingSlot~) ParkingSlot
}
class ParkingFloor {
  ~ int floorNumber
  ~ List~ParkingSlot~ slots
  ~ unpark(ParkingSlot) Vehicle
  ~ park(Vehicle, AllocationStrategy) void
}
class ParkingLot {
  ~ List~ParkingFloor~ floors
  ~ BillingStrategy billingStrategy
  ~ FloorSelectionStrategy floorStrategy
  ~ AllocationStrategy allocStrategy
  ~ getTicket(Vehicle) Ticket
  ~ checkout(Ticket) void
}
class ParkingSlot {
  ~ int id
  ~ int dis
  ~ VehicleType supportedSize
  ~ Vehicle parkedVechicle
  ~ isEmpty() boolean
  ~ park(Vehicle) void
  ~ unpark() Vehicle
}
class RandomAllocationStrategy {
  + getSlot(Vehicle, List~ParkingSlot~) ParkingSlot
}
class RandomFloorStartegy {
  + selectFloor(Vehicle, List~ParkingFloor~) ParkingFloor
}
class Ticket {
  ~ Instant exit
  ~ ParkingSlot slot
  ~ int id
  ~ Instant entry
  ~ ParkingFloor floor
  ~ Vehicle vehicle
  ~ setExit(Instant) void
}
class Vehicle {
  ~ int id
  ~ VehicleType type
}
class VehicleType {
<<enumeration>>
  +  small
  +  large
  +  medium
  + values() VehicleType[]
  + valueOf(String) VehicleType
}

DefaultBillingStrategy  ..>  BillingStrategy 
HourlyBillingStrategy  ..>  BillingStrategy 
NearestAllocationStrategy  ..>  AllocationStrategy 
RandomAllocationStrategy  ..>  AllocationStrategy 
RandomFloorStartegy  ..>  FloorSelectionStrategy 
