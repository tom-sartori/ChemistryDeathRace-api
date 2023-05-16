package chemistry.death.race.repository;

import chemistry.death.race.collection.question.Question;
import chemistry.death.race.service.QuestionService;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.runtime.JavaMongoOperations;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class QuestionRepository implements PanacheMongoRepository<Question> {

    @Inject
    QuestionService questionService;


    /**
     * Persists the question if it is valid.
     *
     * @param question the question to persist
     */
    @Override
    public void persist(Question question) {
        if (questionService.isValid(question)) {
            JavaMongoOperations.INSTANCE.persist(question);
        }
        else {
            throw new BadRequestException("Question is not valid. ");
        }
    }

    /**
     * Updates the question if it is valid.
     *
     * @param question the question to update
     */
    @Override
    public void update(Question question) {
        if (questionService.isValid(question)) {
            JavaMongoOperations.INSTANCE.update(question);
        }
        else {
            throw new BadRequestException("Question is not valid. ");
        }
    }

    /**
     * Get all categories of questions. They are computed from the category field of the Question class.
     *
     * @return a list of all categories.
     */
    public List<String> findAllCategories() {
        return streamAll()
                .map(Question::getCategory)
                .distinct()
                .collect(toList());
    }

    /**
     * Get all categories of questions with a given difficulty. They are computed from the category field of the Question class.
     *
     * @param difficulty the difficulty of the questions.
     * @return a list of all categories.
     */
    public List<String> findAllCategoriesByDifficulty(String difficulty) {
        return streamAll()
                .filter(question -> question.getDifficulty().equals(difficulty))
                .map(Question::getCategory)
                .distinct()
                .collect(toList());
    }

    /**
     * Update all question's categories for a given difficulty.
     *
     * @param difficulty the difficulty of the category to update.
     * @param oldCategoryValue the old category value.
     * @param newCategoryValue the new category value.
     */
    public void updateAllCategories(String difficulty, String oldCategoryValue, String newCategoryValue) {
        if (oldCategoryValue.equals(newCategoryValue)) {
            throw new BadRequestException("Old and new category values are the same. ");
        }
        else if (update("category", newCategoryValue).where("difficulty = ?1 and category = ?2", difficulty, oldCategoryValue) == 0) {
            throw new NotFoundException("No question with category " + oldCategoryValue + " found. ");
        }
    }

    /**
     * Get all difficulties of questions. They are computed from the difficulty field of the Question class.
     *
     * @return a list of all difficulties.
     */
    public List<String> findAllDifficulties() {
        return streamAll()
                .map(Question::getDifficulty)
                .distinct()
                .collect(toList());
    }

    /**
     * Get all available difficulties of questions with all categories. They are computed from the difficulty field of the Question class.
     * A difficulty is available if it has 6 categories.
     *
     * @return a list of all difficulties.
     */
    public List<String> findAvailableDifficulties() {
        return streamAll()
                .map(Question::getDifficulty)
                .distinct()
                .filter(difficulty -> findAllCategoriesByDifficulty(difficulty).size() == 6)
                .collect(toList());
    }

    /**
     * Get all questions with a given difficulty.
     *
     * @param difficulty the difficulty of the questions.
     * @return a list of all questions with the given difficulty.
     */
    public List<Question> findByDifficulty(String difficulty) {
        return list("difficulty", Sort.ascending("name"), difficulty);
    }

    /**
     * Update all question's difficulties for a given difficulty.
     *
     * @param oldDifficultyValue the old difficulty value.
     * @param newDifficultyValue the new difficulty value.
     */
    public void updateAllDifficulties(String oldDifficultyValue, String newDifficultyValue) {
        if (oldDifficultyValue.equals(newDifficultyValue)) {
            throw new BadRequestException("Old and new difficulty values are the same. ");
        }
        else if (update("difficulty", newDifficultyValue).where("difficulty", oldDifficultyValue) == 0) {
            throw new NotFoundException("No question with difficulty " + oldDifficultyValue + " found. ");
        }
    }

    /**
     * Get all questions with a given difficulty and category.
     *
     * @param difficulty the difficulty of the questions.
     * @param category the category of the questions.
     * @return a list of all questions with the given category.
     */
    public List<Question> listAll(String difficulty, String category) {
        return streamAll(Sort.ascending("name"))
                .filter(question -> question.getDifficulty().equals(difficulty))
                .filter(question -> question.getCategory().equals(category))
                .collect(toList());
    }
}
