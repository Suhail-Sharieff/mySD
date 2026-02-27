package games._02_snake_ladder;


interface Win{
    boolean isWin(Board b,int newPos);
}

public class WinningStrategy implements Win{

    @Override
    public boolean isWin(Board b, int newPos) {
        return (newPos==b.getSize()-1)  ;
    }
    
}