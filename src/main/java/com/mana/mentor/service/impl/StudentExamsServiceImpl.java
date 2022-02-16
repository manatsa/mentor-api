package com.mana.mentor.service.impl;

import com.mana.mentor.domain.StudentExams;
import com.mana.mentor.repository.StudentExamsRepository;
import com.mana.mentor.service.StudentExamsService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StudentExams}.
 */
@Service
@Transactional
public class StudentExamsServiceImpl implements StudentExamsService {

    private final Logger log = LoggerFactory.getLogger(StudentExamsServiceImpl.class);

    private final StudentExamsRepository studentExamsRepository;

    public StudentExamsServiceImpl(StudentExamsRepository studentExamsRepository) {
        this.studentExamsRepository = studentExamsRepository;
    }

    @Override
    public StudentExams save(StudentExams studentExams) {
        log.debug("Request to save StudentExams : {}", studentExams);
        return studentExamsRepository.save(studentExams);
    }

    @Override
    public Optional<StudentExams> partialUpdate(StudentExams studentExams) {
        log.debug("Request to partially update StudentExams : {}", studentExams);

        return studentExamsRepository
            .findById(studentExams.getId())
            .map(existingStudentExams -> {
                if (studentExams.getCompleted() != null) {
                    existingStudentExams.setCompleted(studentExams.getCompleted());
                }
                if (studentExams.getFinishDate() != null) {
                    existingStudentExams.setFinishDate(studentExams.getFinishDate());
                }
                if (studentExams.getMark() != null) {
                    existingStudentExams.setMark(studentExams.getMark());
                }
                if (studentExams.getTotal() != null) {
                    existingStudentExams.setTotal(studentExams.getTotal());
                }
                if (studentExams.getDateCreated() != null) {
                    existingStudentExams.setDateCreated(studentExams.getDateCreated());
                }
                if (studentExams.getLastModifiedDate() != null) {
                    existingStudentExams.setLastModifiedDate(studentExams.getLastModifiedDate());
                }

                return existingStudentExams;
            })
            .map(studentExamsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentExams> findAll(Pageable pageable) {
        log.debug("Request to get all StudentExams");
        return studentExamsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentExams> findOne(Long id) {
        log.debug("Request to get StudentExams : {}", id);
        return studentExamsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StudentExams : {}", id);
        studentExamsRepository.deleteById(id);
    }
}
