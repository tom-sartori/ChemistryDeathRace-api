package chemical.pursuit.collection.game;

import chemical.pursuit.constant.CollectionNames;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@MongoEntity(collection = CollectionNames.GAME)
public class Game {

    private ObjectId id;
    private Date startDate;
    private Date endDate;
    private String difficulty;
    private List<Answer> answers;
    private int numberOfPlayers;
    private int diceSize;

    public Game() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getDiceSize() {
        return diceSize;
    }

    public void setDiceSize(int diceSize) {
        this.diceSize = diceSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return numberOfPlayers == game.numberOfPlayers && diceSize == game.diceSize && Objects.equals(id, game.id) && Objects.equals(startDate, game.startDate) && Objects.equals(endDate, game.endDate) && Objects.equals(difficulty, game.difficulty) && Objects.equals(answers, game.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, difficulty, answers, numberOfPlayers, diceSize);
    }
}
