package memento.GameSnapshot;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;

import java.io.Serializable;
import java.util.List;

/**
 * Memento representing the saved state of the game map.
 * <p>
 * Captures map dimensions and all resource positions (grass, water, obstacles),
 * allowing the map to be restored to a previous state. Part of the Memento pattern.
 * </p>
 */
public record MapState(int width,
                       int height,
                       List<Position> grassPositions,
                       List<Position> waterPositions,
                       List<Position> obstaclesPositions) implements Serializable {

    public MapState(MapBuilder builder) {
        this(builder.getWidth(),
                builder.getHeight(),
                builder.getGrassPositions(),
                builder.getWaterPositions(),
                builder.getObstaclesPositions());
    }
}
