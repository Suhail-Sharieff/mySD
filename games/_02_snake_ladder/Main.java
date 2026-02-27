package games._02_snake_ladder;

import java.util.List;
import java.util.Map;

/*

mistake: i divided board into cells like how v did in tic tac toe making uncessarily complex

always ask interviewer the fetures u want to build, dont assume ur own since it will make logic very complex

the curr version is one time and 1 winner game

*/


public class Main {

    public static void main(String[] args) {
        List<Win>winStrategy=List.of(new WinningStrategy());
        Map<Integer, Integer> snakes = Map.of(
                14, 7,
                22, 3
        );

        Map<Integer, Integer> ladders = Map.of(
                2, 15,
                8, 20
        );
        List<Player>players=List.of(new Player('A'),new Player('B'));
        int size=25;
        Dice dice=new Dice(1, 6);
        Board b=new Board(snakes, ladders, size);
        
        Game game=new Game(players, b, winStrategy, dice);
        game.play();//A's turn
        game.play();//B's turn
        ///....and so on
    }   
}




