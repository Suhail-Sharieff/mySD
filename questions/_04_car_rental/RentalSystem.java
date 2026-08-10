package questions._04_car_rental;

import java.time.Instant;
import java.util.List;
/*
locations have many cars of many types
admin can add locations, cars in them
user reserves a car
user pickups the car
user uses vehicle, vehicle is observed by admin for tracking
user submits vehicle
he is given bill
he pays for that bill
*/
enum VehicleType{
    SUV,
    LUXURY,
    VAN,
    NORMAL
}

enum VehicleStatus{
    AVAILABLE,
    RESERVED,
    UNDER_MAINTENANCE
}

enum ReservationStatus{
    INITIATED,
    ACTIVE,
    COMPLETED,
    CANCELLED
}

enum PaymentStatus{
    PROCESSING,
    PAID,
    FAILED
}

enum AddOn{
    BABY_SEAT(300),
    AC(400);

    private final int cost;

    AddOn(int cost){
        this.cost=cost;
    }

    public int getCost(){
        return cost;
    }
}

class User{
    int id;
    String name;
    String licenseNo;
}

class Admin extends User{
    void addVehicle(Location loc,Vehicle v){}
    void removeVehicle(Location loc,Vehicle v){}
}

class Location{
    int id;
    String address;
    double lat;
    double lon;

    List<Vehicle> vehicles;
}

abstract class Vehicle{
    int id;
    String name;
    VehicleType type;
    VehicleStatus status;
}

class SUV extends Vehicle{}
class LuxuryCar extends Vehicle{}
class Van extends Vehicle{}
class NormalCar extends Vehicle{}

class Reservation{
    int id;

    User user;
    Vehicle vehicle;

    Location pickupLocation;
    Location dropLocation;

    Instant startTime;
    Instant endTime;

    List<AddOn> addOns;

    ReservationStatus status;
}

class Bill{
    Reservation reservation;

    int baseCost;
    int addOnCost;
    int totalCost;
}

class Payment{
    int id;

    User user;
    Bill bill;

    Instant timestamp;

    PaymentStatus status;
}

interface AllocationStrategy{
    Vehicle allocateVehicle(
            List<Vehicle> vehicles,
            VehicleType type,
            List<AddOn> addOns
    );
}

class DefaultAllocationStrategy implements AllocationStrategy{
    @Override
    public Vehicle allocateVehicle(
            List<Vehicle> vehicles,
            VehicleType type,
            List<AddOn> addOns
    ){
        return null;
    }
}

interface BillingStrategy{
    Bill generateBill(Reservation reservation);
}

class HourlyBillingStrategy implements BillingStrategy{
    @Override
    public Bill generateBill(Reservation reservation){
        return null;
    }
}

class AssetManager{
    List<Location> locations;

    void addVehicle(Location location,Vehicle vehicle){}

    void removeVehicle(Location location,Vehicle vehicle){}

    void addLocation(Location location){}
}

public class RentalSystem{
    AssetManager assetManager;

    AllocationStrategy allocationStrategy;
    BillingStrategy billingStrategy;

    Reservation reserveVehicle(
            User user,
            VehicleType type,
            Location pickup,
            Location drop,
            List<AddOn> addOns
    ){
        return null;
    }

    Bill generateBill(Reservation reservation){
        return billingStrategy.generateBill(reservation);
    }

    Payment makePayment(Bill bill){
        return null;
    }
}