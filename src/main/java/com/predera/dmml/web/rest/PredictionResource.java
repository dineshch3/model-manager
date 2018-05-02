package com.predera.dmml.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.predera.dmml.domain.Prediction;
import com.predera.dmml.service.PredictionQueryService;
import com.predera.dmml.service.PredictionService;
import com.predera.dmml.service.dto.PredictionCriteria;
import com.predera.dmml.web.rest.errors.BadRequestAlertException;
import com.predera.dmml.web.rest.util.HeaderUtil;
import com.predera.dmml.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Prediction.
 */
@RestController
@RequestMapping("/api")
public class PredictionResource {

	private final Logger log = LoggerFactory.getLogger(PredictionResource.class);

	private static final String ENTITY_NAME = "prediction";

	private final PredictionService predictionService;

	private final PredictionQueryService predictionQueryService;

	public PredictionResource(PredictionService predictionService, PredictionQueryService predictionQueryService) {
		this.predictionService = predictionService;
		this.predictionQueryService = predictionQueryService;
	}

	/**
	 * POST /predictions : Create a new prediction.
	 *
	 * @param prediction
	 *            the prediction to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         prediction, or with status 400 (Bad Request) if the prediction has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/predictions")
	@Timed
	public ResponseEntity<Prediction> createPrediction(@Valid @RequestBody Prediction prediction)
			throws URISyntaxException {
		log.debug("REST request to save Prediction : {}", prediction);
		if (prediction.getId() != null) {
			throw new BadRequestAlertException("A new prediction cannot already have an ID", ENTITY_NAME, "idexists");
		}
		prediction.setTimestamp(Instant.now());

		Prediction result = predictionService.save(prediction);

		return ResponseEntity.created(new URI("/api/predictions/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/*
	 * /** PUT /predictions : Updates an existing prediction.
	 *
	 * @param prediction the prediction to update
	 * 
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 * prediction, or with status 400 (Bad Request) if the prediction is not valid,
	 * or with status 500 (Internal Server Error) if the prediction couldn't be
	 * updated
	 * 
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 * 
	 * @PutMapping("/predictions")
	 * 
	 * @Timed public ResponseEntity<Prediction> updatePrediction(@Valid @RequestBody
	 * Prediction prediction) throws URISyntaxException {
	 * log.debug("REST request to update Prediction : {}", prediction); if
	 * (prediction.getId() == null) { return createPrediction(prediction); }
	 * Prediction result = predictionService.save(prediction); return
	 * ResponseEntity.ok() .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
	 * prediction.getId().toString())) .body(result); }
	 */

	/**
	 * GET /predictions : get all the predictions.
	 *
	 * @param pageable
	 *            the pagination information
	 * @param criteria
	 *            the criterias which the requested entities should match
	 * @return the ResponseEntity with status 200 (OK) and the list of predictions
	 *         in body
	 */
	@GetMapping("/predictions")
	@Timed
	public ResponseEntity<List<Prediction>> getAllPredictions(PredictionCriteria criteria, Pageable pageable) {
		log.debug("REST request to get Predictions by criteria: {}", criteria);
		Page<Prediction> page = predictionQueryService.findByCriteria(criteria, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/predictions");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /predictions/:id : get the "id" prediction.
	 *
	 * @param id
	 *            the id of the prediction to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the prediction,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/predictions/{id}")
	@Timed
	public ResponseEntity<Prediction> getPrediction(@PathVariable Long id) {
		log.debug("REST request to get Prediction : {}", id);
		Prediction prediction = predictionService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(prediction));
	}

	/*
	 * /** DELETE /predictions/:id : delete the "id" prediction.
	 *
	 * @param id the id of the prediction to delete
	 * 
	 * @return the ResponseEntity with status 200 (OK)
	 * 
	 * @DeleteMapping("/predictions/{id}")
	 * 
	 * @Timed public ResponseEntity<Void> deletePrediction(@PathVariable Long id) {
	 * log.debug("REST request to delete Prediction : {}", id);
	 * predictionService.delete(id); return
	 * ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME,
	 * id.toString())).build(); }
	 */
}
