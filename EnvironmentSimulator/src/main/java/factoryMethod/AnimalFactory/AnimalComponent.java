package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;

import java.io.Serializable;
import java.util.List;

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
