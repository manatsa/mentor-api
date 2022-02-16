package com.mana.mentor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mana.mentor.domain.enumeration.Level;
import com.mana.mentor.domain.enumeration.SubjectClass;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Subject.
 */
@Entity
@Table(name = "subject")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "classfication", nullable = false)
    private SubjectClass classfication;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne
    private User creator;

    @ManyToOne
    private User modifier;

    @ManyToMany(mappedBy = "subjects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "school", "region", "guardian", "agency", "creator", "modifier", "subjects", "lessons", "exercises", "exams" },
        allowSetters = true
    )
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "subjects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "school", "creator", "modfier", "subjects" }, allowSetters = true)
    private Set<Teacher> teachers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Subject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Subject name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubjectClass getClassfication() {
        return this.classfication;
    }

    public Subject classfication(SubjectClass classfication) {
        this.setClassfication(classfication);
        return this;
    }

    public void setClassfication(SubjectClass classfication) {
        this.classfication = classfication;
    }

    public Level getLevel() {
        return this.level;
    }

    public Subject level(Level level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Subject dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Subject lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public Subject creator(User user) {
        this.setCreator(user);
        return this;
    }

    public User getModifier() {
        return this.modifier;
    }

    public void setModifier(User user) {
        this.modifier = user;
    }

    public Subject modifier(User user) {
        this.setModifier(user);
        return this;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.removeSubject(this));
        }
        if (students != null) {
            students.forEach(i -> i.addSubject(this));
        }
        this.students = students;
    }

    public Subject students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Subject addStudent(Student student) {
        this.students.add(student);
        student.getSubjects().add(this);
        return this;
    }

    public Subject removeStudent(Student student) {
        this.students.remove(student);
        student.getSubjects().remove(this);
        return this;
    }

    public Set<Teacher> getTeachers() {
        return this.teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        if (this.teachers != null) {
            this.teachers.forEach(i -> i.removeSubject(this));
        }
        if (teachers != null) {
            teachers.forEach(i -> i.addSubject(this));
        }
        this.teachers = teachers;
    }

    public Subject teachers(Set<Teacher> teachers) {
        this.setTeachers(teachers);
        return this;
    }

    public Subject addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getSubjects().add(this);
        return this;
    }

    public Subject removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getSubjects().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }
        return id != null && id.equals(((Subject) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subject{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", classfication='" + getClassfication() + "'" +
            ", level='" + getLevel() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
