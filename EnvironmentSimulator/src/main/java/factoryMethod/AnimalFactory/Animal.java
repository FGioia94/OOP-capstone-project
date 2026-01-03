package factoryMethod.AnimalFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class Animal {
    protected final String id;
    protected final int range;
    public String sex;
    public List<Integer> positions = new ArrayList<>(2);
    public int hp;

    public Animal(String id,
                  int range,
                  List<Integer> positions,
                  String sex,
                  int hp) {
        this.id = id;
        this.range = range;
        this.positions = positions;
        this.sex = sex;
        this.hp = hp;
    }

    void move() {
        // add movement process here
    }

    abstract Animal reproduce();
}
