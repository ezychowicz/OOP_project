package agh.ics.oop.model.exceptions;

public class IllegalMoveSpecificationException extends RuntimeException {
    public static final String ILLEGAL_MOVE_SPEC_MESSAGE = "move specification is illegal";
    public IllegalMoveSpecificationException(String letter) {
        super("%s: %s".formatted(letter, ILLEGAL_MOVE_SPEC_MESSAGE));
    }
}
