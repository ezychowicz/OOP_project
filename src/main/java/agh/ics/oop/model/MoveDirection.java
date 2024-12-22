package agh.ics.oop.model;


public enum MoveDirection {
    FORWARD,
    BACKWARD,
    LEFT,
    RIGHT,
    FORWARD_RIGHT,
    FORWARD_LEFT,
    BACKWARD_LEFT,
    BACKWARD_RIGHT;
    public static MoveDirection geneToDirection (int gene) {
        return switch (gene) {
            case 0 -> FORWARD;
            case 1 -> FORWARD_RIGHT;
            case 2 -> RIGHT;
            case 3 -> BACKWARD_RIGHT;
            case 4 -> BACKWARD;
            case 5 -> BACKWARD_LEFT;
            case 6 -> LEFT;
            case 7 -> FORWARD_LEFT;
            default -> throw new IllegalArgumentException("Invalid gene: " + gene);
        };
    }

    public int directionToGene(MoveDirection direction) {
        return switch (direction) {
            case FORWARD -> 0;
            case BACKWARD -> 1;
            case LEFT -> 2;
            case RIGHT -> 3;
            case FORWARD_RIGHT -> 4;
            case FORWARD_LEFT -> 5;
            case BACKWARD_LEFT -> 6;
            case BACKWARD_RIGHT -> 7;
        };
    }
}

