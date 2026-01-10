package builder.MapBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class MapIterator implements Iterator<Position> {

    private final int width;   // number of columns
    private final int height;  // number of rows
    private final Set<String> occupant;

    private int row = 0;       // was currentX
    private int col = 0;       // was currentY

    public MapIterator(int width, int height, List<Position> occupiedPositions) {
        this.width = width;
        this.height = height;

        // Convert obstacles to a fast lookup set
        this.occupant = new HashSet<>();
        for (Position pos : occupiedPositions) {
            // pos[0] = row, pos[1] = col
            occupant.add(pos.x() + "," + pos.y());
        }

        // Move to the first valid position
        moveToNextValid();
    }

    @Override
    public boolean hasNext() {
        return row < height;
    }

    @Override
    public Position next() {
        Position pos = new Position(row, col);
        moveToNextValid();
        return pos;
    }

    private void moveToNextValid() {
        do {
            col++;
            if (col >= width) {
                col = 0;
                row++;
            }
        } while (row < height && occupant.contains(row + "," + col));
    }
}