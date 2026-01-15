package builder.MapBuilder;

import strategy.IO.SaveStrategy;

import java.io.Serializable;

/**
 * Immutable 2D coordinate used to represent a position on the map grid.
 * <p>
 * A {@code Position} stores an {@code x} (column) and {@code y} (row) value.
 * It is used throughout the map-building system to identify terrain tiles,
 * valid movement locations, and serialized map state.
 * </p>
 *
 * <p>
 * Being a {@link java.lang.Record record}, this class is inherently immutable,
 * provides value-based equality, and is safe to use as a key or element in
 * collections. It also implements {@link Serializable} to support persistence
 * through {@link SaveStrategy} and snapshot mechanisms.
 * </p>
 *
 * @param x the column index
 * @param y the row index
 */
public record Position(int x, int y) implements Serializable {
}