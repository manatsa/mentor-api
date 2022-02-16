package com.mana.mentor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExerciseMark.
 */
@Entity
@Table(name = "exercise_mark")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExerciseMark implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total")
    private Integer total;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "school", "region", "guardian", "agency", "creator", "modifier", "subjects", "lessons", "exercises", "exams" },
        allowSetters = true
    )
    private Student student;

    @ManyToOne
    @JsonIgnoreProperties(value = { "lesson", "creator", "modifier", "students" }, allowSetters = true)
    private Exercise exercise;

    @ManyToOne
    private User creator;

    @ManyToOne
    private User modifier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExerciseMark id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return this.total;
    }

    public ExerciseMark total(Integer total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getMark() {
        return this.mark;
    }

    public ExerciseMark mark(Integer mark) {
        this.setMark(mark);
        return this;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public ExerciseMark dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ExerciseMark lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ExerciseMark student(Student student) {
        this.setStudent(student);
        return this;
    }

    public Exercise getExercise() {
        return this.exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public ExerciseMark exercise(Exercise exercise) {
        this.setExercise(exercise);
        return this;
    }

    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public ExerciseMark creator(User user) {
        this.setCreator(user);
        return this;
    }

    public User getModifier() {
        return this.modifier;
    }

    public void setModifier(User user) {
        this.modifier = user;
    }

    public ExerciseMark modifier(User user) {
        this.setModifier(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExerciseMark)) {
            return false;
        }
        return id != null && id.equals(((ExerciseMark) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseMark{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            ", mark=" + getMark() +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
