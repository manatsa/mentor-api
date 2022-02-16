package com.mana.mentor.service.impl;

import com.mana.mentor.domain.ExamAnswer;
import com.mana.mentor.repository.ExamAnswerRepository;
import com.mana.mentor.service.ExamAnswerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExamAnswer}.
 */
@Service
@Transactional
public class ExamAnswerServiceImpl implements ExamAnswerService {

    private final Logger log = LoggerFactory.getLogger(ExamAnswerServiceImpl.class);

    private final ExamAnswerRepository examAnswerRepository;

    public ExamAnswerServiceImpl(ExamAnswerRepository examAnswerRepository) {
        this.examAnswerRepository = examAnswerRepository;
    }

    @Override
    public ExamAnswer save(ExamAnswer examAnswer) {
        log.debug("Request to save ExamAnswer : {}", examAnswer);
        return examAnswerRepository.save(examAnswer);
    }

    @Override
    public Optional<ExamAnswer> partialUpdate(ExamAnswer examAnswer) {
        log.debug("Request to partially update ExamAnswer : {}", examAnswer);

        return examAnswerRepository
            .findById(examAnswer.getId())
            .map(existingExamAnswer -> {
                if (examAnswer.getAnswer() != null) {
                    existingExamAnswer.setAnswer(examAnswer.getAnswer());
                }
                if (examAnswer.getDateCreated() != null) {
                    existingExamAnswer.setDateCreated(examAnswer.getDateCreated());
                }
                if (examAnswer.getLastModifiedDate() != null) {
                    existingExamAnswer.setLastModifiedDate(examAnswer.getLastModifiedDate());
                }

                return existingExamAnswer;
            })
            .map(examAnswerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamAnswer> findAll(Pageable pageable) {
        log.debug("Request to get all ExamAnswers");
        return examAnswerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExamAnswer> findOne(Long id) {
        log.debug("Request to get ExamAnswer : {}", id);
        return examAnswerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExamAnswer : {}", id);
        examAnswerRepository.deleteById(id);
    }
}
