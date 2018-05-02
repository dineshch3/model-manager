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

import com.predera.dmml.domain.Model;
import com.predera.dmml.domain.*; // for static metamodels
import com.predera.dmml.repository.ModelRepository;
import com.predera.dmml.service.dto.ModelCriteria;


/**
 * Service for executing complex queries for Model entities in the database.
 * The main input is a {@link ModelCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Model} or a {@link Page} of {@link Model} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ModelQueryService extends QueryService<Model> {

    private final Logger log = LoggerFactory.getLogger(ModelQueryService.class);


    private final ModelRepository modelRepository;

    public ModelQueryService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    /**
     * Return a {@link List} of {@link Model} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Model> findByCriteria(ModelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Model> specification = createSpecification(criteria);
        return modelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Model} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Model> findByCriteria(ModelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Model> specification = createSpecification(criteria);
        return modelRepository.findAll(specification, page);
    }

    /**
     * Function to convert ModelCriteria to a {@link Specifications}
     */
    private Specifications<Model> createSpecification(ModelCriteria criteria) {
        Specifications<Model> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Model_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Model_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Model_.type));
            }
            if (criteria.getAlgorithm() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAlgorithm(), Model_.algorithm));
            }
            if (criteria.getLibrary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibrary(), Model_.library));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Model_.status));
            }
            if (criteria.getOwner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOwner(), Model_.owner));
            }
            if (criteria.getModelLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelLocation(), Model_.modelLocation));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Model_.createdDate));
            }
            if (criteria.getDeployedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeployedDate(), Model_.deployedDate));
            }
            if (criteria.getTrainingDataset() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrainingDataset(), Model_.trainingDataset));
            }
            if (criteria.getPerformanceMetrics() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPerformanceMetrics(), Model_.performanceMetrics));
            }
            if (criteria.getFeatureSignificance() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFeatureSignificance(), Model_.featureSignificance));
            }
            if (criteria.getBuilderConfig() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBuilderConfig(), Model_.builderConfig));
            }
            if (criteria.getPredictionUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPredictionUrl(), Model_.predictionUrl));
            }
            if (criteria.getProject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProject(), Model_.project));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), Model_.version));
            }
            if (criteria.getReplicationFactor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReplicationFactor(), Model_.replicationFactor));
            }
        }
        return specification;
    }

}
