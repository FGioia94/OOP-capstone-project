package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;

import java.util.ArrayList;
import java.util.List;

public class AnimalPack implements AnimalComponent {
    private final String id;
    private final List<AnimalComponent> members = new ArrayList<>();
    final String animalType;

    public AnimalPack(String id) {
        this.id = id;
        this.animalType = "Pack";
    }


    public void add(AnimalComponent c) {
        members.add(c);
    }

    public void remove(AnimalComponent c) {
        members.remove(c);
    }

    public List<AnimalComponent> getMembers() {
        return members;
    }

    // ---------------------------
    // POSITION
    // ---------------------------
    @Override
    public Position getPosition() {
        int totalX = 0;
        int totalY = 0;
        for (AnimalComponent animal : members) {
            Position pos = animal.getPosition();
            totalX += pos.x();
            totalY += pos.y();
        }
        if (!members.isEmpty()) {
            return new Position((int) (totalX / members.size()), (int) (totalY / members.size()));
        }
        return new Position(0, 0); // Default position if no members
    }

    @Override
    public void setPosition(Position p) {
        Position pos = getPosition();
        int offsetX = p.x() - pos.x();
        int offsetY = p.y() - pos.y();
        for (AnimalComponent animal : members) {
            animal.setPosition(new Position(p.x() + offsetX, p.y() + offsetY));
        }
    }


    @Override
    public int getHp() {
        int totalHp = 0;
        for (AnimalComponent animal : members) {
            int hp = animal.getHp();
            totalHp += hp;
        }
        return (int) (totalHp / members.size());
    }

    @Override
    public void setHp(int hp) {
    }


    @Override
    public int getExp() {
        return members.stream().mapToInt(AnimalComponent::getExp).sum();
    }

    @Override
    public void setExp(int exp) {
    }


    @Override
    public int getLevel() {
        return members.stream().mapToInt(AnimalComponent::getLevel).sum();
    }

    @Override
    public void setLevel(int level) {
    }

    @Override
    public String getAnimalType() {
        return "Pack";
    }

    @Override
    public String getSex() {
        return "";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getRange() {
        if (members.isEmpty()) {
            return 0;
        }

        int totalRange = 0;
        for (
                AnimalComponent animal : members) {
            totalRange += animal.getRange();
        }
        return (int) (totalRange / members.size());
    }

    @Override
    public String getPack() {
        return null;
    }

    @Override
    public void setPack(String pack) {
    }
}

