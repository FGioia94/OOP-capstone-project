package builder.MapBuilder;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents an immutable environment map generated through a
 * {@link MapBuilder}.
 * <p>
 * The map stores its dimensions along with the positions of different terrain
 * types such as water, grass, and obstacles. All positional data is provided
 * by the builder, ensuring that {@code EnvironmentMap} acts purely as a
 * constructed data container without performing validation or generation logic.
 * </p>
 *
 */

public class EnvironmentMap {

    private static final Logger logger = LogManager.getLogger(EnvironmentMap.class);
    
    /** Height of the map grid. **/
    private final int height;
    /** Width of the map grid. **/
    private final int width;

    /** List of positions classified as water tiles. */
    public List<Position> waterPositions = new ArrayList<>();
    /** List of positions classified as grass tiles. */
    public List<Position> grassPositions = new ArrayList<>();
    /** List of positions classified as obstacle tiles. */
    public List<Position> obstaclesPositions = new ArrayList<>();

    /**
     * Constructs an {@code EnvironmentMap} using the data provided by the given
     * builder.
     *
     * @param builder the {@link MapBuilder} instance containing all configuration
     *                and positional data required to build the environment map
     */
    public EnvironmentMap(MapBuilder builder) {
        this.height = builder.getHeight();
        this.width = builder.getWidth();
        this.waterPositions = builder.getWaterPositions();
        this.grassPositions = builder.getGrassPositions();
        this.obstaclesPositions = builder.getObstaclesPositions();

        logger.debug(
                "EnvironmentMap created: {}x{} | water={} | grass={} | obstacles={}",
                width,
                height,
                waterPositions.size(),
                grassPositions.size(),
                obstaclesPositions.size());
    }
}