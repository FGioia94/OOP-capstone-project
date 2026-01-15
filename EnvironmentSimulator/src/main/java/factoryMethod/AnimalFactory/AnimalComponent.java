package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;

import java.io.Serializable;
import java.util.List;

/**
 * Component interface for the Composite pattern.
 * <p>
 * Defines common operations for both individual animals and animal packs,
 * allowing them to be treated uniformly. This is the base interface for
 * all animal entities in the system.
 * </p>
 */
public interface AnimalComponent extends Serializable {


    Position getPosition();

    void setPosition(Position position);

    int getHp();

    void setHp(int hp);

    int getExp();

    void setExp(int exp);

    int getLevel();

    void setLevel(int level);

    String getAnimalType();

    String getSex();

    String getId();

    int getRange();

    String getPack();

    void setPack(String pack);


    List<AnimalComponent> getMembers();
}
