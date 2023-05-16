package chemistry.death.race.repository;

import chemistry.death.race.collection.game.Answer;
import chemistry.death.race.collection.game.Game;
import chemistry.death.race.collection.question.Question;
import chemistry.death.race.collection.question.Stat;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class GameRepository implements PanacheMongoRepository<Game> {
    public void addGame(Game game) {
        if (game.getStartDate() == null) {
            game.setStartDate(new java.util.Date());
        }
        game.setAnswers(List.of());
        game.setEndDate(null);
        persist(game);
    }

    public void closeGame(ObjectId id) {
        Game game = findById(id);
        game.setEndDate(new java.util.Date());
        update(game);
    }

    public void addAnswer(ObjectId id, Answer answer) {
        Game game = findById(id);
        if (game == null) {
            throw new IllegalArgumentException("Game with id " + id + " does not exist");
        }
        game.getAnswers().add(answer);
        update(game);
    }

    public List<Stat> getPercentageByQuestion() {
        List<Stat> stats = new ArrayList<>();
        QuestionRepository questionRepository = new QuestionRepository();
        List<ObjectId> distinctQuestionIds = listAll()
                .stream()
                .flatMap(game -> game.getAnswers().stream())
                .map(Answer::getQuestionId)
                .distinct()
                .collect(toList());

        distinctQuestionIds.forEach(id -> {
            Stat stat = new Stat();
            Question question = questionRepository.findById(id);
            stat.setName(question.getName());
            stat.setDifficulty(question.getDifficulty());
            stat.setPercentage(getPercentageByQuestion(id));
            stats.add(stat);
        });

        return stats;
    }

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

    public double getPercentage() {
        List<Game> games = listAll();
        int correctAnswers = (int) games.stream()
                .flatMap(game -> game.getAnswers().stream())
                .filter(Answer::isCorrect)
                .count();
        int totalAnswers = (int) games.stream()
                .mapToLong(game -> game.getAnswers().size())
                .sum();
        System.out.println("toto :" + correctAnswers + " " + totalAnswers);
        return (double) correctAnswers / totalAnswers;
    }

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

    public double getAverageDiceSize() {
        return listAll()
                .stream()
                .mapToInt(Game::getDiceSize)
                .average()
                .orElse(0);
    }

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

    public double getAveragePlayersNumber() {
        return listAll()
                .stream()
                .mapToInt(Game::getNumberOfPlayers)
                .average()
                .orElse(0);
    }
}
