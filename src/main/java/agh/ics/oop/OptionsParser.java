package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.exceptions.IllegalMoveSpecificationException;

import java.util.List;
import java.util.ArrayList;


public class OptionsParser {
    public static List<MoveDirection> parse(String[] input) {
        List <MoveDirection> moves = new ArrayList<>();
        for (String letter : input){
            switch (letter) {
                case "f" -> moves.add(MoveDirection.FORWARD);
                case "b" -> moves.add(MoveDirection.BACKWARD);
                case "r" -> moves.add(MoveDirection.RIGHT);
                case "l" -> moves.add(MoveDirection.LEFT);
                default -> throw new IllegalMoveSpecificationException(letter);
            }
        }
        return moves;
    }
}
