package games._04_chess;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import games._04_chess.Game.Game;
import games._04_chess.components.Board;

public class Main {

    static  BufferedWriter writer;
    public static void main(String[] args) throws IOException {
        
        writer=new BufferedWriter(new FileWriter("output.txt"));



        Board board=new Board(8, 8);
        Game game=new Game(board);
        game.printBoard(writer);


        game.play(6, 0, 4, 0);
        game.printBoard(writer);



        writer.flush();
        writer.close();
        
        
    }


    static void println(Object o) throws IOException{
        writer.write(o.toString()+"\n");
    }
}