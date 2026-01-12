package builder.MapBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapIterator implements Iterator<Position> {

    private static final Logger logger = LogManager.getLogger(MapIterator.class);

    private final int width;   // number of columns
    private final int height;  // number of rows
    private final Set<String> occupant;

    private int row = 0;
    private int col = 0;

    public MapIterator(int width, int height, List<Position> occupiedPositions) {
        this.width = width;
        this.height = height;

        this.occupant = new HashSet<>();
        for (Position pos : occupiedPositions) {
            occupant.add(pos.x() + "," + pos.y());
        }

        logger.debug("MapIterator created: width={}, height={}, occupiedCount={}",
                width, height, occupant.size());

        moveToNextValid();
    }

    @Override
    public boolean hasNext() {
        boolean has = row < height;
        logger.trace("hasNext() -> {}", has);
        return has;
    }

    @Override
    public Position next() {
        Position pos = new Position(row, col);
        logger.trace("Returning next position: {}", pos);
        moveToNextValid();
        return pos;
    }

    private void moveToNextValid() {
        logger.trace("Searching for next valid position starting from row={}, col={}", row, col);

        do {
            col++;
            if (col >= width) {
                col = 0;
                row++;
            }
        } while (row < height && occupant.contains(row + "," + col));

        if (row < height) {
            logger.trace("Next valid position found at row={}, col={}", row, col);
        } else {
            logger.trace("No more valid positions available");
        }
    }
}