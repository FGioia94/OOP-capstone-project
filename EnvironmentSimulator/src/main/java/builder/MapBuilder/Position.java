package builder.MapBuilder;

import strategy.IO.SaveStrategy;

import java.io.Serializable;

public record Position(int x, int y) implements Serializable {
}