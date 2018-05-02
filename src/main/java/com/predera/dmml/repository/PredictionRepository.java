package com.predera.dmml.repository;

import com.predera.dmml.domain.Prediction;
import com.predera.dmml.service.dto.PredictionSubset;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Prediction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long>, JpaSpecificationExecutor<Prediction> {

	List<PredictionSubset> findAllByModelId(Long ModelId);
	
}
