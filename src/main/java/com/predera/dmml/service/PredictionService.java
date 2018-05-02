package com.predera.dmml.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.predera.dmml.domain.Prediction;
import com.predera.dmml.service.dto.PredictionSubset;

/**
 * Service Interface for managing Prediction.
 */
public interface PredictionService {

    /**
     * Save a prediction.
     *
     * @param prediction the entity to save
     * @return the persisted entity
     */
    Prediction save(Prediction prediction);

    /**
     * Get all the predictions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Prediction> findAll(Pageable pageable);

    /**
     * Get the "id" prediction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Prediction findOne(Long id);

    /**
     * Delete the "id" prediction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	List<PredictionSubset> findAllByModelId(Long modelId);
}
