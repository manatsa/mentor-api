package com.mana.mentor.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mana.mentor.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mana.mentor.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mana.mentor.domain.User.class.getName());
            createCache(cm, com.mana.mentor.domain.Authority.class.getName());
            createCache(cm, com.mana.mentor.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mana.mentor.domain.Region.class.getName());
            createCache(cm, com.mana.mentor.domain.Country.class.getName());
            createCache(cm, com.mana.mentor.domain.School.class.getName());
            createCache(cm, com.mana.mentor.domain.Student.class.getName());
            createCache(cm, com.mana.mentor.domain.Student.class.getName() + ".subjects");
            createCache(cm, com.mana.mentor.domain.Student.class.getName() + ".lessons");
            createCache(cm, com.mana.mentor.domain.Student.class.getName() + ".exercises");
            createCache(cm, com.mana.mentor.domain.Student.class.getName() + ".exams");
            createCache(cm, com.mana.mentor.domain.Teacher.class.getName());
            createCache(cm, com.mana.mentor.domain.Teacher.class.getName() + ".subjects");
            createCache(cm, com.mana.mentor.domain.Guardian.class.getName());
            createCache(cm, com.mana.mentor.domain.Agency.class.getName());
            createCache(cm, com.mana.mentor.domain.Subject.class.getName());
            createCache(cm, com.mana.mentor.domain.Subject.class.getName() + ".students");
            createCache(cm, com.mana.mentor.domain.Subject.class.getName() + ".teachers");
            createCache(cm, com.mana.mentor.domain.Lesson.class.getName());
            createCache(cm, com.mana.mentor.domain.Lesson.class.getName() + ".students");
            createCache(cm, com.mana.mentor.domain.Lecture.class.getName());
            createCache(cm, com.mana.mentor.domain.Example.class.getName());
            createCache(cm, com.mana.mentor.domain.Exercise.class.getName());
            createCache(cm, com.mana.mentor.domain.Exercise.class.getName() + ".students");
            createCache(cm, com.mana.mentor.domain.ExerciseQuestion.class.getName());
            createCache(cm, com.mana.mentor.domain.ExerciseAnswer.class.getName());
            createCache(cm, com.mana.mentor.domain.ExerciseMark.class.getName());
            createCache(cm, com.mana.mentor.domain.Exam.class.getName());
            createCache(cm, com.mana.mentor.domain.Exam.class.getName() + ".students");
            createCache(cm, com.mana.mentor.domain.ExamQuestion.class.getName());
            createCache(cm, com.mana.mentor.domain.ExamAnswer.class.getName());
            createCache(cm, com.mana.mentor.domain.ExamMark.class.getName());
            createCache(cm, com.mana.mentor.domain.StudentExams.class.getName());
            createCache(cm, com.mana.mentor.domain.Attachment.class.getName());
            createCache(cm, com.mana.mentor.domain.News.class.getName());
            createCache(cm, com.mana.mentor.domain.Calenda.class.getName());
            createCache(cm, com.mana.mentor.domain.Contact.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
