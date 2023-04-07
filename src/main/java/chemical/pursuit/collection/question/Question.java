package chemical.pursuit.collection.question;

import chemical.pursuit.constant.CollectionNames;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

@MongoEntity(collection = CollectionNames.QUESTION)
public class Question {

    private ObjectId id;
    private String name;
    private List<Proposition> propositions;
    private String category;
    private String difficulty;
    private String image;

    public Question() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id) && Objects.equals(name, question.name) && Objects.equals(propositions, question.propositions) && Objects.equals(category, question.category) && Objects.equals(difficulty, question.difficulty) && Objects.equals(image, question.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, propositions, category, difficulty, image);
    }
}
