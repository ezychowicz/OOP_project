package agh.ics.oop.model;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int maxWidth;
    private final int maxHeight;
    private final int grassCount;
    private int count;
    private final List<Vector2d> posList;
    private final Random random;
    private final List<Integer> availableIndices;
    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.grassCount = grassCount;
        this.posList = new ArrayList<Vector2d>(); //niezmieniana lista, generowana w generatePoints
        this.availableIndices = generatePoints(); //przechowuje nieuzyte indeksy z posList
        this.count = 0;
        this.random = new Random();
    }

    private List<Integer> generatePoints() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < maxWidth; i++) {
            for (int j = 0; j < maxHeight; j++) {
                posList.add(new Vector2d(i,j));
                indices.add(i*maxHeight+j);
            }
        }
        return indices;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<Vector2d>(){
            @Override
            public boolean hasNext() {
                return count < grassCount;
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No grasses left to allocate");
                }
                count++;
                int randomInt = random.nextInt(availableIndices.size()); //losuje w zakresie dostepnych pozycji
                Vector2d pos = posList.get(availableIndices.get(randomInt));
                availableIndices.remove(randomInt);
                return pos;
            }
        };
    }


}
