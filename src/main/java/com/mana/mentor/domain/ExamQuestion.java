package com.mana.mentor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mana.mentor.domain.enumeration.Level;
import com.mana.mentor.domain.enumeration.TestType;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExamQuestion.
 */
@Entity
@Table(name = "exam_question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExamQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TestType type;

    @NotNull
    @Column(name = "answer", nullable = false)
    private String answer;

    @Lob
    @Column(name = "explanation")
    private String explanation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "subject", "creator", "modifier", "students" }, allowSetters = true)
    private Exam exam;

    @ManyToOne
    private User creator;

    @ManyToOne
    private User modifier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExamQuestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public ExamQuestion question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public TestType getType() {
        return this.type;
    }

    public ExamQuestion type(TestType type) {
        this.setType(type);
        return this;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public String getAnswer() {
        return this.answer;
    }

    public ExamQuestion answer(String answer) {
        this.setAnswer(answer);
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public ExamQuestion explanation(String explanation) {
        this.setExplanation(explanation);
        return this;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Level getLevel() {
        return this.level;
    }

    public ExamQuestion level(Level level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public ExamQuestion dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ExamQuestion lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public ExamQuestion exam(Exam exam) {
        this.setExam(exam);
        return this;
    }

    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public ExamQuestion creator(User user) {
        this.setCreator(user);
        return this;
    }

    public User getModifier() {
        return this.modifier;
    }

    public void setModifier(User user) {
        this.modifier = user;
    }

    public ExamQuestion modifier(User user) {
        this.setModifier(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamQuestion)) {
            return false;
        }
        return id != null && id.equals(((ExamQuestion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamQuestion{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", type='" + getType() + "'" +
            ", answer='" + getAnswer() + "'" +
            ", explanation='" + getExplanation() + "'" +
            ", level='" + getLevel() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
