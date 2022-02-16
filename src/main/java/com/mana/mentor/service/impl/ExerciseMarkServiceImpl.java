package com.mana.mentor.service.impl;

import com.mana.mentor.domain.ExerciseMark;
import com.mana.mentor.repository.ExerciseMarkRepository;
import com.mana.mentor.service.ExerciseMarkService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExerciseMark}.
 */
@Service
@Transactional
public class ExerciseMarkServiceImpl implements ExerciseMarkService {

    private final Logger log = LoggerFactory.getLogger(ExerciseMarkServiceImpl.class);

    private final ExerciseMarkRepository exerciseMarkRepository;

    public ExerciseMarkServiceImpl(ExerciseMarkRepository exerciseMarkRepository) {
        this.exerciseMarkRepository = exerciseMarkRepository;
    }

    @Override
    public ExerciseMark save(ExerciseMark exerciseMark) {
        log.debug("Request to save ExerciseMark : {}", exerciseMark);
        return exerciseMarkRepository.save(exerciseMark);
    }

    @Override
    public Optional<ExerciseMark> partialUpdate(ExerciseMark exerciseMark) {
        log.debug("Request to partially update ExerciseMark : {}", exerciseMark);

        return exerciseMarkRepository
            .findById(exerciseMark.getId())
            .map(existingExerciseMark -> {
                if (exerciseMark.getTotal() != null) {
                    existingExerciseMark.setTotal(exerciseMark.getTotal());
                }
                if (exerciseMark.getMark() != null) {
                    existingExerciseMark.setMark(exerciseMark.getMark());
                }
                if (exerciseMark.getDateCreated() != null) {
                    existingExerciseMark.setDateCreated(exerciseMark.getDateCreated());
                }
                if (exerciseMark.getLastModifiedDate() != null) {
                    existingExerciseMark.setLastModifiedDate(exerciseMark.getLastModifiedDate());
                }

                return existingExerciseMark;
            })
            .map(exerciseMarkRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseMark> findAll(Pageable pageable) {
        log.debug("Request to get all ExerciseMarks");
        return exerciseMarkRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseMark> findOne(Long id) {
        log.debug("Request to get ExerciseMark : {}", id);
        return exerciseMarkRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseMark : {}", id);
        exerciseMarkRepository.deleteById(id);
    }
}
