package chemistry.death.race.collection.game;

import org.bson.types.ObjectId;

import java.util.Objects;

public class Answer {

    private boolean correct;
    private ObjectId questionId;

    public Answer() {
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public ObjectId getQuestionId() {
        return questionId;
    }

    public void setQuestionId(ObjectId questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return correct == answer.correct && Objects.equals(questionId, answer.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correct, questionId);
    }
}
