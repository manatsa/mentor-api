package com.mana.mentor.service.impl;

import com.mana.mentor.domain.Lecture;
import com.mana.mentor.repository.LectureRepository;
import com.mana.mentor.service.LectureService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Lecture}.
 */
@Service
@Transactional
public class LectureServiceImpl implements LectureService {

    private final Logger log = LoggerFactory.getLogger(LectureServiceImpl.class);

    private final LectureRepository lectureRepository;

    public LectureServiceImpl(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public Lecture save(Lecture lecture) {
        log.debug("Request to save Lecture : {}", lecture);
        return lectureRepository.save(lecture);
    }

    @Override
    public Optional<Lecture> partialUpdate(Lecture lecture) {
        log.debug("Request to partially update Lecture : {}", lecture);

        return lectureRepository
            .findById(lecture.getId())
            .map(existingLecture -> {
                if (lecture.getName() != null) {
                    existingLecture.setName(lecture.getName());
                }
                if (lecture.getContent() != null) {
                    existingLecture.setContent(lecture.getContent());
                }
                if (lecture.getPicture() != null) {
                    existingLecture.setPicture(lecture.getPicture());
                }
                if (lecture.getPictureContentType() != null) {
                    existingLecture.setPictureContentType(lecture.getPictureContentType());
                }
                if (lecture.getDateCreated() != null) {
                    existingLecture.setDateCreated(lecture.getDateCreated());
                }
                if (lecture.getLastModifiedDate() != null) {
                    existingLecture.setLastModifiedDate(lecture.getLastModifiedDate());
                }

                return existingLecture;
            })
            .map(lectureRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Lecture> findAll(Pageable pageable) {
        log.debug("Request to get all Lectures");
        return lectureRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lecture> findOne(Long id) {
        log.debug("Request to get Lecture : {}", id);
        return lectureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lecture : {}", id);
        lectureRepository.deleteById(id);
    }
}
