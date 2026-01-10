package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Animal implements AnimalComponent {
    private int exp;
    protected final int range;
    protected int level;

    protected final String id;

    public final String sex;
    public Position position = new Position(0, 0);
    public int hp;
    public final String animalType;

    public Animal(
            String id,
            int range,
            Position position,
            String sex,
            int hp,
            int exp,
            int level,
            String animalType) {
        this.id = id;
        this.range = range;
        this.position = position;
        this.sex = sex;
        this.hp = hp;
        this.exp = exp;
        this.level = level;
        this.animalType = animalType;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public String getSex() {
        return sex;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setHp(int hp) {
        this.hp = hp;
    }


    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getExp() {
        return exp;
    }

    @Override
    public void setExp(int exp) {
        this.exp = exp;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String getAnimalType() {
        return animalType;
    }


    abstract Animal reproduce();
}
