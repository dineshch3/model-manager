package com.predera.dmml.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.predera.dmml.domain.Prediction;
import com.predera.dmml.domain.*; // for static metamodels
import com.predera.dmml.repository.PredictionRepository;
import com.predera.dmml.service.dto.PredictionCriteria;


/**
 * Service for executing complex queries for Prediction entities in the database.
 * The main input is a {@link PredictionCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Prediction} or a {@link Page} of {@link Prediction} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PredictionQueryService extends QueryService<Prediction> {

    private final Logger log = LoggerFactory.getLogger(PredictionQueryService.class);


    private final PredictionRepository predictionRepository;

    public PredictionQueryService(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    /**
     * Return a {@link List} of {@link Prediction} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Prediction> findByCriteria(PredictionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Prediction> specification = createSpecification(criteria);
        return predictionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Prediction} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Prediction> findByCriteria(PredictionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Prediction> specification = createSpecification(criteria);
        return predictionRepository.findAll(specification, page);
    }

    /**
     * Function to convert PredictionCriteria to a {@link Specifications}
     */
    private Specifications<Prediction> createSpecification(PredictionCriteria criteria) {
        Specifications<Prediction> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Prediction_.id));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), Prediction_.timestamp));
            }
            if (criteria.getQueryId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQueryId(), Prediction_.queryId));
            }
            if (criteria.getOutputClass() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutputClass(), Prediction_.outputClass));
            }
            if (criteria.getFeedbackFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getFeedbackFlag(), Prediction_.feedbackFlag));
            }
            if (criteria.getFeedbackClass() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFeedbackClass(), Prediction_.feedbackClass));
            }
            if (criteria.getFeedbackValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeedbackValue(), Prediction_.feedbackValue));
            }
            if (criteria.getFeedbackTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeedbackTimestamp(), Prediction_.feedbackTimestamp));
            }
            if (criteria.getFeedbackUser() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFeedbackUser(), Prediction_.feedbackUser));
            }
            if (criteria.getDefaultDesc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDefaultDesc(), Prediction_.defaultDesc));
            }
            if (criteria.getErrorFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getErrorFlag(), Prediction_.errorFlag));
            }
            if (criteria.getErrorDesc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getErrorDesc(), Prediction_.errorDesc));
            }
            if (criteria.getErrorCause() != null) {
                specification = specification.and(buildStringSpecification(criteria.getErrorCause(), Prediction_.errorCause));
            }
            if (criteria.getDefaultFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getDefaultFlag(), Prediction_.defaultFlag));
            }
            if (criteria.getOutputValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOutputValue(), Prediction_.outputValue));
            }
            if (criteria.getModelId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getModelId(), Prediction_.model, Model_.id));
            }
        }
        return specification;
    }

}
