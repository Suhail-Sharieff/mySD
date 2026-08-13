package questions._07_movie_book;

/*
User searches movies by location.

Location
    -> Theatres
        -> Screens
            -> Shows
               -> Seats

Movie runs as Shows.
A Show is a movie running on a screen at a particular time.

Seats are booked per Show, not globally(means NOT in screen).

Bad design i did earlier: booked seats at screen level
Good design: booked seats at show level, coz shows are temporary, screens are permanent


*/

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class User {
    int id;
    String name;
}

class Movie {
    int id;
    String name;
    Duration duration;
}
enum SeatStatus{
    LOCKED,//not yet reserved, but selected by user for reservation 
    AVAILABLE,
    RESERVED
}
class Seat {
    int id;
    int row;
    int col;
}
class ShowSeat{//cause if we had SeatStatus in Seat, its a problem since same seat can be resued in many shows
    Seat seat;
    SeatStatus status; 
}
class Show{
    int id;
    Screen screen;
    Movie movie;
    LocalDateTime start;
    LocalDateTime end;
    Map<ShowSeat,SeatStatus>statusMap;
    synchronized void lockSeats(List<ShowSeat> seats) {}
    synchronized void unlockSeats(List<ShowSeat> seats) {}
    synchronized void bookSeats(List<ShowSeat> seats) {}
}

interface PricingStrategy {
    double getPrice(Seat seat);
}

class Screen {
    int id;
    List<Seat> seats;
    PricingStrategy pricingStrategy;
    double getPriceFor(Seat seat){return pricingStrategy.getPrice(seat);}
}

interface ShowAllotmentStrategy{//allocates show to some free screen
    Screen getScreen(Movie movie,LocalDateTime time,List<Screen>screens);
}
class Theatre {
    int id;
    ShowAllotmentStrategy showAllotmentStrategy;
    List<Screen>screens;
    List<Show>shows;
    synchronized void addShow(Show show) {}
    synchronized void removeShow(Show show) {}
    List<Show> getShowsFor(Movie m){return null;}
}

class Location {
    int id;
    String address;
    List<Theatre> theatres;
    synchronized void addTheatre(Theatre t){};
    synchronized void removeTheatre(Theatre t){}
}

class Ticket {
    User user;
    Show show;
    List<Seat> seatsReserved;
    double amt;
}

class Payment {
    int id;
    Ticket ticket;
    Instant paidAt;
    double amount;
    synchronized void pay(){}
}

public class MovieBookingSystem {
    List<Location>locations;
    List<Movie>searchMovies(Location loc){return null;}
    List<Show>searchShows(Movie m,Location loc){return null;}
    synchronized Ticket book(User u,Show show,List<Seat>seats){return null;}
    synchronized void cancel(Ticket t){}
    synchronized Payment pay(Ticket t){return null;}
}