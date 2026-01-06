package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class Animal {
    private int exp;
    protected final int range;
    protected int level;

    protected final String id;

    public final String sex;
    public Position position = new Position(0, 0);
    public int hp;
    public String animalType;

    public Animal(
            int range,
            Position position,
            String sex,
            int hp,
            int exp,
            int level,
            String animalType) {
        this.id = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("ssmmHHddMMyy"));
        this.range = range;
        this.position = position;
        this.sex = sex;
        this.hp = hp;
        this.exp = exp;
        this.level = level;
        this.animalType = animalType;
    }

    public String getId() {
        return id;
    }

    public int getRange() {
        return range;
    }

    public String getSex() {
        return sex;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    void move() {
        // add movement process here
    }

    public int getLevel() {
        return this.level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    abstract Animal reproduce();
}
