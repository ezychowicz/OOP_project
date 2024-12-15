package agh.ics.oop.model.exceptions;

import agh.ics.oop.model.Vector2d;

public class IncorrectPositionException extends Exception {
    public IncorrectPositionException(Vector2d position) {
      super("Position %s is not correct".formatted(position));
    }
}
