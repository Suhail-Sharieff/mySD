package games._01_tic_tac_toe._02_.strategies;

import games._01_tic_tac_toe._02_.components.Board;

public 
interface PostionStrategy{
    boolean valid(Board board,int i,int j);
}
