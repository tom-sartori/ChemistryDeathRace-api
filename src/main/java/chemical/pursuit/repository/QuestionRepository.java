package chemical.pursuit.repository;

import chemical.pursuit.collection.question.Question;
import chemical.pursuit.service.QuestionService;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.runtime.JavaMongoOperations;

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


    @Override
    public void persist(Question question) {
        if (questionService.isValid(question)) {
            JavaMongoOperations.INSTANCE.persist(question);
        }
        else {
            throw new BadRequestException("Question is not valid. ");
        }
    }

    @Override
    public void update(Question question) {
        if (questionService.isValid(question)) {
            JavaMongoOperations.INSTANCE.update(question);
        }
        else {
            throw new BadRequestException("Question is not valid. ");
        }
    }

    public List<String> findAllCategories() {
        return streamAll()
                .map(Question::getCategory)
                .distinct()
                .collect(toList());
    }

    public void updateAllCategories(String oldCategoryValue, String newCategoryValue) {
        if (oldCategoryValue.equals(newCategoryValue)) {
            throw new BadRequestException("Old and new category values are the same. ");
        }
        else if (update("category", newCategoryValue).where("category", oldCategoryValue) == 0) {
            throw new NotFoundException("No question with category " + oldCategoryValue + " found. ");
        }
    }

    public List<Question> findByCategory(String category) {
        return list("category", category);
    }
}
