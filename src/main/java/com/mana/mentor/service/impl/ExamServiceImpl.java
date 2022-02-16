package com.mana.mentor.service.impl;

import com.mana.mentor.domain.Exam;
import com.mana.mentor.repository.ExamRepository;
import com.mana.mentor.service.ExamService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Exam}.
 */
@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private final Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);

    private final ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public Exam save(Exam exam) {
        log.debug("Request to save Exam : {}", exam);
        return examRepository.save(exam);
    }

    @Override
    public Optional<Exam> partialUpdate(Exam exam) {
        log.debug("Request to partially update Exam : {}", exam);

        return examRepository
            .findById(exam.getId())
            .map(existingExam -> {
                if (exam.getName() != null) {
                    existingExam.setName(exam.getName());
                }
                if (exam.getDescription() != null) {
                    existingExam.setDescription(exam.getDescription());
                }
                if (exam.getCompleted() != null) {
                    existingExam.setCompleted(exam.getCompleted());
                }
                if (exam.getStartDate() != null) {
                    existingExam.setStartDate(exam.getStartDate());
                }
                if (exam.getDueDate() != null) {
                    existingExam.setDueDate(exam.getDueDate());
                }
                if (exam.getDateCreated() != null) {
                    existingExam.setDateCreated(exam.getDateCreated());
                }
                if (exam.getLastModifiedDate() != null) {
                    existingExam.setLastModifiedDate(exam.getLastModifiedDate());
                }

                return existingExam;
            })
            .map(examRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Exam> findAll(Pageable pageable) {
        log.debug("Request to get all Exams");
        return examRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Exam> findOne(Long id) {
        log.debug("Request to get Exam : {}", id);
        return examRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exam : {}", id);
        examRepository.deleteById(id);
    }
}
