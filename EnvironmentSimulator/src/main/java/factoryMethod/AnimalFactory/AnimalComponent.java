package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;

public interface AnimalComponent {
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


}
