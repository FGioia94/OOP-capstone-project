package builder.MapBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Iterator that traverses a 2D map grid and yields only the positions that are not
 * occupied. The iteration proceeds in row-major order (left-to-right, top-to-bottom),
 * skipping any coordinates that appear in the provided list of occupied positions.
 *
 * <p>
 * This iterator is typically used by {@link MapBuilder} to compute all valid
 * (non-occupied) positions on the map. It performs no mutation and does not modify
 * the underlying data structures.
 * </p>
 *
 * <p>
 * Occupied positions are stored internally as string keys in the form
 * {@code "row,col"} for fast lookup. Logging at TRACE level provides detailed
 * traversal information useful for debugging.
 * </p>
 */
public class MapIterator implements Iterator<Position> {

    private static final Logger logger = LogManager.getLogger(MapIterator.class);

    /** Number of columns in the map. */
    private final int width;

    /** Number of rows in the map. */
    private final int height;

    /** Set of occupied coordinates encoded as "row,col". */
    private final Set<String> occupant;

    /** Current row index during iteration. */
    private int row = 0;

    /** Current column index during iteration. */
    private int col = 0;

    /**
     * Creates a new iterator over a 2D grid of the given dimensions, skipping
     * any positions contained in {@code occupiedPositions}.
     *
     * @param width             number of columns in the grid
     * @param height            number of rows in the grid
     * @param occupiedPositions list of positions that should be excluded
     */
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

    /**
     * Indicates whether there are more valid (non-occupied) positions to iterate over.
     *
     * @return {@code true} if another valid position exists, {@code false} otherwise
     */
    @Override
    public boolean hasNext() {
        boolean has = row < height;
        logger.trace("hasNext() -> {}", has);
        return has;
    }

    /**
     * Returns the next valid position in row-major order.
     *
     * @return the next available {@link Position}
     */
    @Override
    public Position next() {
        Position pos = new Position(row, col);
        logger.trace("Returning next position: {}", pos);
        moveToNextValid();
        return pos;
    }

    /**
     * Advances the internal cursor to the next non-occupied position.
     * <p>
     * This method increments the column index, wrapping to the next row when
     * necessary, and continues until a free coordinate is found or the grid
     * bounds are exceeded.
     * </p>
     */
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