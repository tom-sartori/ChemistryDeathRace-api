package chemical.pursuit.collection.question;

import java.util.Objects;

public class Stat {
    private String name;
    private String difficulty;
    private double percentage;

    public Stat() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stat stat = (Stat) o;
        return Double.compare(stat.percentage, percentage) == 0 && Objects.equals(name, stat.name) && Objects.equals(difficulty, stat.difficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, difficulty, percentage);
    }
}
