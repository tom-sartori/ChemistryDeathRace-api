package chemical.pursuit.repository;

import chemical.pursuit.collection.question.Question;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class QuestionRepository implements PanacheMongoRepository<Question> {

    public List<String> findAllCategories() {
        return streamAll()
                .map(Question::getCategory)
                .distinct()
                .collect(toList());
    }

    public void updateAllCategories(String oldCategoryValue, String newCategoryValue) {
        update("category", newCategoryValue).where("category", oldCategoryValue);
    }
}
