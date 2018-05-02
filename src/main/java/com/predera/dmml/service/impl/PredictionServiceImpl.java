package com.predera.dmml.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.predera.dmml.domain.Prediction;
import com.predera.dmml.repository.PredictionRepository;
import com.predera.dmml.service.PredictionService;
import com.predera.dmml.service.dto.PredictionSubset;


/**
 * Service Implementation for managing Prediction.
 */
@Service
@Transactional
public class PredictionServiceImpl implements PredictionService{

    private final Logger log = LoggerFactory.getLogger(PredictionServiceImpl.class);

    private final PredictionRepository predictionRepository;

    public PredictionServiceImpl(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    /**
     * Save a prediction.
     *
     * @param prediction the entity to save
     * @return the persisted entity
     */
    @Override
    public Prediction save(Prediction prediction) {
        log.debug("Request to save Prediction : {}", prediction);
        return predictionRepository.save(prediction);
    }

    /**
     * Get all the predictions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Prediction> findAll(Pageable pageable) {
        log.debug("Request to get all Predictions");
        return predictionRepository.findAll(pageable);
    }

    /**
     * Get one prediction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Prediction findOne(Long id) {
        log.debug("Request to get Prediction : {}", id);
        return predictionRepository.findOne(id);
    }

    /**
     * Delete the prediction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Prediction : {}", id);
        predictionRepository.delete(id);
    }

	@Override
	public List<PredictionSubset> findAllByModelId(Long modelId) {
		return predictionRepository.findAllByModelId(modelId);
	}
}
