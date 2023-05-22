package chemistry.death.race.repository;

import chemistry.death.race.collection.game.Answer;
import chemistry.death.race.collection.game.Game;
import chemistry.death.race.collection.question.Question;
import chemistry.death.race.collection.question.Stat;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Repository for the Game collection. Used only for statistics.
 */
@ApplicationScoped
public class GameRepository implements PanacheMongoRepository<Game> {

    @Inject
    QuestionRepository questionRepository;

    /**
     * Add a new game to the database.
     *
     * @param game the game to add.
     */
    public void addGame(Game game) {
        if (game.getStartDate() == null) {
            game.setStartDate(new java.util.Date());
        }
        game.setAnswers(List.of());
        game.setEndDate(null);
        persist(game);
    }

    /**
     * Close a game by setting the end date and updating the database.
     *
     * @param id the id of the game to close.
     */
    public void closeGame(ObjectId id) {
        Game game = findById(id);
        game.setEndDate(new java.util.Date());
        update(game);
    }

    /**
     * Add an answer to a game.
     *
     * @param id the id of the game to add the answer to.
     * @param answer the answer to add.
     */
    public void addAnswer(ObjectId id, Answer answer) {
        Game game = findById(id);
        if (game == null) {
            throw new IllegalArgumentException("Game with id " + id + " does not exist");
        }
        game.getAnswers().add(answer);
        update(game);
    }

    /**
     * Get statistics about all questions in the database.
     *
     * @return a list of statistics.
     */
    public List<Stat> getPercentageByQuestion() {
        List<Stat> stats = new ArrayList<>();
        List<ObjectId> distinctQuestionIds = listAll()
                .stream()
                .flatMap(game -> game.getAnswers().stream())
                .map(Answer::getQuestionId)
                .distinct()
                .collect(toList());
        distinctQuestionIds.forEach(id -> {
            Stat stat = new Stat();
            Question question = questionRepository.findById(id);
            if (question == null) {
                throw new IllegalArgumentException("Question with id " + id + " does not exist");
            }
            stat.setName(question.getName());
            stat.setDifficulty(question.getDifficulty());
            stat.setPercentage(getPercentageByQuestion(id));
            stats.add(stat);
        });

        return stats;
    }

    /**
     * Get the percentage of correct answers for a question.
     *
     * @param questionId the id of the question.
     * @return the percentage of correct answers.
     */
    private double getPercentageByQuestion(ObjectId questionId) {
        long correctAnswers = listAll().stream()
                .flatMap(game -> game.getAnswers().stream())
                .filter(answer -> answer.getQuestionId().equals(questionId))
                .filter(Answer::isCorrect)
                .count();
        long totalAnswers = listAll().stream()
                .flatMap(game -> game.getAnswers().stream())
                .filter(answer -> answer.getQuestionId().equals(questionId))
                .count();
        return (double) correctAnswers / totalAnswers;
    }

    /**
     * Get the percentage of correct answers for all questions.
     *
     * @return the percentage of correct answers.
     */
    public double getPercentage() {
        List<Game> games = listAll();
        int correctAnswers = (int) games.stream()
                .flatMap(game -> game.getAnswers().stream())
                .filter(Answer::isCorrect)
                .count();
        int totalAnswers = (int) games.stream()
                .mapToLong(game -> game.getAnswers().size())
                .sum();
        return (double) correctAnswers / totalAnswers;
    }

    /**
     * Get the average duration of a game.
     *
     * @return the average duration of a game.
     */
    public double getAverageTime() {
        List<Game> games = listAll()
                .stream()
                .filter(game -> game.getEndDate() != null)
                .collect(toList());

        long totalTime = games.stream()
                .mapToLong(game -> game.getEndDate().getTime() - game.getStartDate().getTime())
                .sum();

        return (double) (games.size() != 0 ? totalTime / games.size() : 0);
    }

    /**
     * Get the average dice size.
     *
     * @return the average dice size.
     */
    public double getAverageDiceSize() {
        return listAll()
                .stream()
                .mapToInt(Game::getDiceSize)
                .average()
                .orElse(0);
    }

    /**
     * Get the most popular difficulty level chosen by players.
     *
     * @return the most popular difficulty level.
     */
    public String getMostPlayedDifficulty() {
        return listAll()
                .stream()
                .map(Game::getDifficulty)
                .max((o1, o2) -> {
                    int count1 = 0;
                    int count2 = 0;
                    for (Game game : listAll()) {
                        if (game.getDifficulty().equals(o1)) {
                            count1++;
                        }
                        if (game.getDifficulty().equals(o2)) {
                            count2++;
                        }
                    }
                    return count1 - count2;
                })
                .orElse("");
    }

    /**
     * Get the average number of players per game.
     *
     * @return the average number of players per game.
     */
    public double getAveragePlayersNumber() {
        return listAll()
                .stream()
                .mapToInt(Game::getNumberOfPlayers)
                .average()
                .orElse(0);
    }

    /**
     * Remove a question from all games played.
     */
    public void deleteQuestionFromAllGames(ObjectId id) {
        listAll().forEach(game -> {
            game.getAnswers().removeIf(answer -> answer.getQuestionId().equals(id));
            update(game);
        });
    }
}
