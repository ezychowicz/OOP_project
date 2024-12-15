package agh.ics.oop.model;

import java.util.Objects;

public class Grass implements WorldElement{
    private final Vector2d position;
    private static final String GRASS_STRING = "*";
    public Grass(Vector2d position){
        this.position = position;
    }
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString(){
        return GRASS_STRING;
    }

    @Override
    public Vector2d getPos() {
        return position;
    }

    @Override
    public boolean isAt(Vector2d pos) {
        return position.equals(pos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grass grass = (Grass) o;
        return Objects.equals(position, grass.position);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position);
    }
}
