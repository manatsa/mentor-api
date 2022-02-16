package com.mana.mentor.service.impl;

import com.mana.mentor.domain.ExamMark;
import com.mana.mentor.repository.ExamMarkRepository;
import com.mana.mentor.service.ExamMarkService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExamMark}.
 */
@Service
@Transactional
public class ExamMarkServiceImpl implements ExamMarkService {

    private final Logger log = LoggerFactory.getLogger(ExamMarkServiceImpl.class);

    private final ExamMarkRepository examMarkRepository;

    public ExamMarkServiceImpl(ExamMarkRepository examMarkRepository) {
        this.examMarkRepository = examMarkRepository;
    }

    @Override
    public ExamMark save(ExamMark examMark) {
        log.debug("Request to save ExamMark : {}", examMark);
        return examMarkRepository.save(examMark);
    }

    @Override
    public Optional<ExamMark> partialUpdate(ExamMark examMark) {
        log.debug("Request to partially update ExamMark : {}", examMark);

        return examMarkRepository
            .findById(examMark.getId())
            .map(existingExamMark -> {
                if (examMark.getTotal() != null) {
                    existingExamMark.setTotal(examMark.getTotal());
                }
                if (examMark.getMark() != null) {
                    existingExamMark.setMark(examMark.getMark());
                }
                if (examMark.getDateCreated() != null) {
                    existingExamMark.setDateCreated(examMark.getDateCreated());
                }
                if (examMark.getLastModifiedDate() != null) {
                    existingExamMark.setLastModifiedDate(examMark.getLastModifiedDate());
                }

                return existingExamMark;
            })
            .map(examMarkRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamMark> findAll(Pageable pageable) {
        log.debug("Request to get all ExamMarks");
        return examMarkRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExamMark> findOne(Long id) {
        log.debug("Request to get ExamMark : {}", id);
        return examMarkRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExamMark : {}", id);
        examMarkRepository.deleteById(id);
    }
}
