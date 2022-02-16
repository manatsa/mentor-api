package com.mana.mentor.service.impl;

import com.mana.mentor.domain.Student;
import com.mana.mentor.repository.StudentRepository;
import com.mana.mentor.service.StudentService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Student}.
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student save(Student student) {
        log.debug("Request to save Student : {}", student);
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> partialUpdate(Student student) {
        log.debug("Request to partially update Student : {}", student);

        return studentRepository
            .findById(student.getId())
            .map(existingStudent -> {
                if (student.getFirstName() != null) {
                    existingStudent.setFirstName(student.getFirstName());
                }
                if (student.getLastName() != null) {
                    existingStudent.setLastName(student.getLastName());
                }
                if (student.getIdNumber() != null) {
                    existingStudent.setIdNumber(student.getIdNumber());
                }
                if (student.getAddress() != null) {
                    existingStudent.setAddress(student.getAddress());
                }
                if (student.getDob() != null) {
                    existingStudent.setDob(student.getDob());
                }
                if (student.getPhone() != null) {
                    existingStudent.setPhone(student.getPhone());
                }
                if (student.getEmail() != null) {
                    existingStudent.setEmail(student.getEmail());
                }
                if (student.getLevel() != null) {
                    existingStudent.setLevel(student.getLevel());
                }
                if (student.getDateCreated() != null) {
                    existingStudent.setDateCreated(student.getDateCreated());
                }
                if (student.getLastModifiedDate() != null) {
                    existingStudent.setLastModifiedDate(student.getLastModifiedDate());
                }

                return existingStudent;
            })
            .map(studentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Student> findAll(Pageable pageable) {
        log.debug("Request to get all Students");
        return studentRepository.findAll(pageable);
    }

    public Page<Student> findAllWithEagerRelationships(Pageable pageable) {
        return studentRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findOne(Long id) {
        log.debug("Request to get Student : {}", id);
        return studentRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Student : {}", id);
        studentRepository.deleteById(id);
    }
}
