package com.mana.mentor.service.impl;

import com.mana.mentor.domain.ExamQuestion;
import com.mana.mentor.repository.ExamQuestionRepository;
import com.mana.mentor.service.ExamQuestionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExamQuestion}.
 */
@Service
@Transactional
public class ExamQuestionServiceImpl implements ExamQuestionService {

    private final Logger log = LoggerFactory.getLogger(ExamQuestionServiceImpl.class);

    private final ExamQuestionRepository examQuestionRepository;

    public ExamQuestionServiceImpl(ExamQuestionRepository examQuestionRepository) {
        this.examQuestionRepository = examQuestionRepository;
    }

    @Override
    public ExamQuestion save(ExamQuestion examQuestion) {
        log.debug("Request to save ExamQuestion : {}", examQuestion);
        return examQuestionRepository.save(examQuestion);
    }

    @Override
    public Optional<ExamQuestion> partialUpdate(ExamQuestion examQuestion) {
        log.debug("Request to partially update ExamQuestion : {}", examQuestion);

        return examQuestionRepository
            .findById(examQuestion.getId())
            .map(existingExamQuestion -> {
                if (examQuestion.getQuestion() != null) {
                    existingExamQuestion.setQuestion(examQuestion.getQuestion());
                }
                if (examQuestion.getType() != null) {
                    existingExamQuestion.setType(examQuestion.getType());
                }
                if (examQuestion.getAnswer() != null) {
                    existingExamQuestion.setAnswer(examQuestion.getAnswer());
                }
                if (examQuestion.getExplanation() != null) {
                    existingExamQuestion.setExplanation(examQuestion.getExplanation());
                }
                if (examQuestion.getLevel() != null) {
                    existingExamQuestion.setLevel(examQuestion.getLevel());
                }
                if (examQuestion.getDateCreated() != null) {
                    existingExamQuestion.setDateCreated(examQuestion.getDateCreated());
                }
                if (examQuestion.getLastModifiedDate() != null) {
                    existingExamQuestion.setLastModifiedDate(examQuestion.getLastModifiedDate());
                }

                return existingExamQuestion;
            })
            .map(examQuestionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamQuestion> findAll(Pageable pageable) {
        log.debug("Request to get all ExamQuestions");
        return examQuestionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExamQuestion> findOne(Long id) {
        log.debug("Request to get ExamQuestion : {}", id);
        return examQuestionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExamQuestion : {}", id);
        examQuestionRepository.deleteById(id);
    }
}
