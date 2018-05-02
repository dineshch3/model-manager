package com.predera.dmml.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.predera.dmml.config.ApplicationProperties;
import com.predera.dmml.domain.Model;
import com.predera.dmml.domain.Prediction;
import com.predera.dmml.security.SecurityUtils;
import com.predera.dmml.service.DataService;
import com.predera.dmml.service.ModelQueryService;
import com.predera.dmml.service.ModelService;
import com.predera.dmml.service.ModelUploadService;
import com.predera.dmml.service.PredictionService;
import com.predera.dmml.service.dto.CreateModelDTO;
import com.predera.dmml.service.dto.FeedbackResponseDTO;
import com.predera.dmml.service.dto.ModelSubset;
import com.predera.dmml.service.dto.PredictionResponseDTO;
import com.predera.dmml.service.dto.PredictionSubset;
import com.predera.dmml.web.rest.errors.BadRequestAlertException;
import com.predera.dmml.web.rest.errors.InternalServerErrorException;
import com.predera.dmml.web.rest.util.HeaderUtil;
import com.predera.dmml.web.rest.vm.ReplicationFactorDTO;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Model.
 */
@SuppressWarnings("deprecation")
@RestController
@RequestMapping("/api")
public class ModelResource {

	private final Logger log = LoggerFactory.getLogger(ModelResource.class);

	private static final String ENTITY_NAME = "model";

	private final ModelService modelService;
	private final ModelQueryService modelQueryService;
	private final String modelDeployUrl;
	private final String modelUndeployUrl;
	private final String mmPredictionUrl;
	private final String replicationFactorUrl;
	private final String pymmPredictionUrl;
	private final ObjectMapper jsonMapper;
	private final RestTemplate restTemplate;
	private final DataService dataService;

	@Autowired
	private ModelUploadService modelUploadService;

	private final PredictionService predictionService;

	public ModelResource(ModelService modelService, ModelQueryService modelQueryService,
                         DataService dataService, ModelUploadService modelUploadService,
                         ApplicationProperties applicationProperties, PredictionService predictionService) {
		this.modelService = modelService;
		this.modelQueryService = modelQueryService;
        this.dataService = dataService;
        this.modelUploadService = modelUploadService;
		this.predictionService = predictionService;

		this.mmPredictionUrl = applicationProperties.getModelManager().getPredictionUrl();
		this.pymmPredictionUrl = applicationProperties.getPyModelManager().getPredictionUrl();
		this.modelDeployUrl = applicationProperties.getPyModelManager().getModelDeployUrl();
		this.modelUndeployUrl = applicationProperties.getPyModelManager().getModelUndeployUrl();
		this.replicationFactorUrl = applicationProperties.getPyModelManager().getReplicationFactorUrl();

		this.jsonMapper = new ObjectMapper();
		this.restTemplate = new RestTemplate();
	}

    @Async
    private void asyncSavePredictionToPostgres(Prediction prediction) {
        predictionService.save(prediction);
    }
    
	/**
	 * POST /models : Create a new model.
	 *
	 * @param inputModel
	 *            the model to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         model, or with status 400 (Bad Request) if the model has already an
	 *         ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@SuppressWarnings("static-access")
	@PostMapping("/models")
	@Timed
	public ResponseEntity<Model> createModel(@Valid @RequestBody CreateModelDTO inputModel) throws URISyntaxException {
		log.debug("REST request to save Model : {}");

		Model model = new Model();
		model.setName(inputModel.getName());
		model.setType(inputModel.getType());
		model.setAlgorithm(inputModel.getAlgorithm());
		model.setPerformanceMetrics(inputModel.getPerformanceMetrics());
		model.setBuilderConfig(inputModel.getBuilderConfig());
		model.setFeatureSignificance(inputModel.getFeatureSignificance());
		model.setTrainingDataset(inputModel.getTrainingDataset());
		model.setStatus("Experiment");
		model.setOwner(SecurityUtils.getCurrentUserLogin().get());
		model.setLibrary(inputModel.getLibrary());
		model.setCreatedDate(Instant.now());
		model.setDeployedDate(Instant.now());
		model.setProject(inputModel.getProject());
		model.setVersion(inputModel.getVersion());
		Model result = modelService.save(model);

		return ResponseEntity.created(new URI("/api/models/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

    /*
	/**
	 * PUT /models : Updates an existing model.
	 *
	 * @param updateModel
	 *            the model to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         model, or with status 400 (Bad Request) if the model is not valid, or
	 *         with status 500 (Internal Server Error) if the model couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	@SuppressWarnings("static-access")
	@PutMapping("/models")
	@Timed
	public ResponseEntity<Model> updateModel(@Valid @RequestBody UpdateModelDto updateModel) throws URISyntaxException {
		log.debug("REST request to update Model : {}");

		Model model = new Model();
		model.setName(updateModel.getName());
		model.setType(updateModel.getType());
		model.setAlgorithm(updateModel.getAlgorithm());
		model.setModelLocation(updateModel.getModelLocation());
		model.setPerformanceMetrics(updateModel.getPerformanceMetrics());
		model.setBuilderConfig(updateModel.getBuilderConfig());
		model.setFeatureSignificance(updateModel.getFeatureSignificance());
		model.setTrainingDataset(updateModel.getTrainingDataset());

		Model result = modelService.save(model);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, model.getId().toString()))
				.body(result);
	}
	*/

	/**
	 * GET /models : get all the models.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of models in body
	 */
	@GetMapping("/models")
	@Timed
	public ResponseEntity<List<ModelSubset>> getAllModels(){
		String owner = SecurityUtils.getCurrentUserLogin().get();
		List<ModelSubset> list = modelService.findAllByOwner(owner);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	/**
	 * GET :models/count : get number of models for current user
	 *
	 * @return number of models for current user
	 */
	@GetMapping("/models/count")
	@Timed
	public Long getCountOfModels() {
		String owner = SecurityUtils.getCurrentUserLogin().get();
		log.debug("REST request to get count of models by owner: {}", owner);
		return modelService.countAllByOwner(owner);
	}

	/**
	 * GET /models/:id : get the "id" model.
	 *
	 * @param id
	 *            the id of the model to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the model, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/models/{id}")
	@Timed
	public ResponseEntity<Model> getModel(@PathVariable Long id) {
		log.debug("REST request to get Model : {}", id);

		Model model = modelService.findOneByOwner(id, SecurityUtils.getCurrentUserLogin().get());
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(model));
	}

	/**
	 * DELETE /models/:id : delete the "id" model.
	 *
	 * @param id
	 *            the id of the model to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/models/{id}")
	@Timed
	public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
		log.debug("REST request to delete Model : {}", id);

		Model model = modelService.findOneByOwner(id, SecurityUtils.getCurrentUserLogin().get());
		if (model == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else if (model.getStatus().equals("Deployed") || model.getStatus().equals("deployed")) {
			throw new BadRequestAlertException("You cannot delete a Deployed Model", "Model", "invalid");
		}
		String path = model.getModelLocation();
		try {
			if (path != null) {
				Files.deleteIfExists(Paths.get(path));
				modelService.delete(id);
				return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
						.build();
			} else {
				modelService.delete(id);
				return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
						.build();
			}
		} catch (NoSuchFileException e) {
			throw new BadRequestAlertException("No such file/directory exists", "Model", "Not Exists");
		} catch (IOException e) {
			throw new BadRequestAlertException("Invalid permissions", "Model", "invalid");
		}
	}

	/**
	 * POST /models/:id/deploy : Deploy a model.
	 *
	 * @param id
	 *            the id of the model to deploy
	 * @return the ResponseEntity with status 200 (Ok) or with status 400 (Bad
	 *         Request) if the request is invalid
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
    @PostMapping("/models/{id}/deploy")
    @Timed
    public ResponseEntity<Model> deployModel(@PathVariable Long id, 
                                             @RequestBody ReplicationFactorDTO replicationFactor) 
                                             throws Exception {
        log.debug("REST request to get Model : {}", id);
        log.debug("Replication Factor : {}", replicationFactor);
        
        log.debug("Replication Factor : {}", replicationFactor.getReplicationFactor());

		String currentUser = SecurityUtils.getCurrentUserLogin().get();
		Model model = modelService.findOneByOwner(id, currentUser);
		if (model == null) {
			log.debug("Model not found or user {} does not have access to model {}", currentUser, id);
			throw new BadRequestAlertException("Model not found or access denied", "Model", "invalid");
		}

		Map builderConfig = jsonMapper.readValue(model.getBuilderConfig(), Map.class);
		log.debug("Builder Config: {}", builderConfig);
		log.debug("Model Builder Library: {}", builderConfig.get("library"));

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(modelDeployUrl)
            .queryParam("model_name", model.getName())
            .queryParam("model_path", model.getModelLocation())
            .queryParam("model_type", model.getType())
            .queryParam("model_library", builderConfig.get("library"))
            .queryParam("replication_factor", replicationFactor.getReplicationFactor());
		// TODO: Move this to a deployment service
		ResponseEntity<String> deployResponse = restTemplate.postForEntity(builder.build().toUri(), null, String.class);
		log.debug("py-model-manager response: {}", deployResponse);

		if (!deployResponse.getStatusCode().is2xxSuccessful()) {
			log.debug("Model deployment failed, returning py-model-manager response to client");
			throw new InternalServerErrorException("Model deployment failed!");
		}

		log.debug("Model deployed successfully using py-model-manager, updating status in db");
		model.setStatus("Deployed");
		model.setDeployedDate(Instant.now());
		model.setReplicationFactor(replicationFactor.getReplicationFactor());
		model.setPredictionUrl(this.mmPredictionUrl.replaceFirst("\\{id\\}", Long.toString(model.getId())));
		Model result = modelService.save(model);

		ResponseEntity<Model> response = ResponseEntity.ok().body(result);
		log.debug("Success response: {}", response);
		return response;
	}

	/**
     * POST /models/:id/scale : Set replication factor a model after deployment.
     *
     * @param id
     *            the id of the model deployed
     * @return the ResponseEntity with status 200 (Ok) or with status 400 (Bad
     *         Request) if the request is invalid
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @PostMapping("/models/{id}/scale")
    @Timed
    public ResponseEntity<Model> replicationFactor(@PathVariable Long id, @RequestBody ReplicationFactorDTO replicationFactor) throws Exception {
        log.debug("REST request to get Model : {}", id);
        String currentUser = SecurityUtils.getCurrentUserLogin().get();
        Model model = modelService.findOneByOwner(id, currentUser);
        if (model == null) {
            log.debug("Model not found or user {} does not have access to model {}", currentUser, id);
            throw new BadRequestAlertException("Model not found or access denied", "Model", "invalid");
        }
        
        if (model.getStatus().equals("Experiment")) {
            log.debug("Model is not deployed", currentUser, id, model.getStatus());
            throw new BadRequestAlertException("Cannot set replication factor for undeployed models", "Model", "invalid");
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(jsonMapper.writeValueAsString(replicationFactor), headers);
        ResponseEntity<String> replicationFactorResponse = restTemplate.postForEntity(replicationFactorUrl, entity, String.class,
                model.getName());
        log.debug("py-model-manager response: {}", replicationFactorResponse);

        if (!replicationFactorResponse.getStatusCode().is2xxSuccessful()) {
            log.debug("Set replication factor for deployed model failed, returning py-model-manager response to client");
            throw new InternalServerErrorException("Set replication factor for deployed model failed!");
        }

        log.debug("Replication factor set to deployed model successfully using py-model-manager");
        
        log.debug("replication Factor {}", replicationFactor.getReplicationFactor());
        
        model.setReplicationFactor(replicationFactor.getReplicationFactor());
        Model result = modelService.save(model);
        
        ResponseEntity<Model> response = ResponseEntity.ok().body(result);
        log.debug("Success response: {}", response);
        return response;
    }

	/**
	 * POST /models/{id}/undeploy : Undeploy a model.
	 *
	 * @param id
	 *            the id of the model to undeploy
	 * @return the ResponseEntity with status 200 (Ok) or with status 400 (Bad
	 *         Request) if the request is invalid
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/models/{id}/undeploy")
	@Timed
	public ResponseEntity<Model> undeployModel(@PathVariable Long id) throws Exception {
		log.debug("REST request to undeploy Model : {}", id);

		String currentUser = SecurityUtils.getCurrentUserLogin().get();
		Model model = modelService.findOneByOwner(id, currentUser);
		if (model == null) {
			log.debug("Model not found or user {} does not have access to model {}", currentUser, id);
			throw new BadRequestAlertException("Model not found or access denied", "Model", "invalid");
		}

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(modelUndeployUrl).queryParam("model_name",
				model.getName());
		// TODO: Move this to a deployment service
		ResponseEntity<String> undeployResponse = restTemplate.postForEntity(builder.build().toUri(), null,
				String.class);
		log.debug("py-model-manager response: {}", undeployResponse);

		if (!undeployResponse.getStatusCode().is2xxSuccessful()) {
			log.debug("Model undeployment failed, returning py-model-manager response to client");
			throw new InternalServerErrorException("Error undeploying the model!");
		}

		log.debug("Model undeployed successfully using py-model-manager, updating status in db");
		model.setStatus("Experiment");
		model.setDeployedDate(Instant.now());
		model.setPredictionUrl("");
		Model result = modelService.save(model);

		ResponseEntity<Model> response = ResponseEntity.ok().body(result);
		log.debug("Success response: {}", response);
		return response;
	}

	/**
	 * POST /models/:id/predict : predict using the model
	 *
	 * @param id the id of the model to use for prediction
     * @param uid (optional) unique identifier e.g. user id
	 * @param input request data
	 * @return the ResponseEntity with status 200 (OK) and prediction output in body
	 *         or with status 400 (Bad Request) if the request is not valid
	 * @throws IOException
	 * @throws JSONException
	 */
    @PostMapping("/models/{id}/predict")
    @Timed
    public ResponseEntity<Prediction> predict(@PathVariable Long id, @RequestParam(value = "uid", required=false) String uid,
                                              @RequestBody String input) throws IOException {
        log.debug("REST request to predict using Model: {}", id);
        log.debug("uid: {}", uid);
        log.debug("Prediction Input : {}", input);

        String currentUser = SecurityUtils.getCurrentUserLogin().get();
        Model model = modelService.findOneByOwner(id, currentUser);
        if (model == null) {
            log.debug("Model not found or user {} does have access to model {}", currentUser, id);
            throw new BadRequestAlertException("Model not found or access denied", ENTITY_NAME,
                "Model not found or access denied");
        }

        Map<String, Float> jsonInput;
        try {
            jsonInput = jsonMapper.readValue(input, Map.class);
            log.debug("JSON Input: {}", jsonInput);
        } catch (IOException e) {
            log.debug("Invalid request: {}", e.getMessage());
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(input, headers);
        // TODO: Move this to prediction service
        ResponseEntity<String> pymmResponse = restTemplate.postForEntity(pymmPredictionUrl, entity, String.class,
            model.getName());
        log.debug("PyMM response : {}", pymmResponse);

        PredictionResponseDTO predictionResponseDTO = jsonMapper.readValue(pymmResponse.getBody(), PredictionResponseDTO.class);
        log.debug("Prediction Response DTO : {}", predictionResponseDTO);

        dataService.asyncSavePrediction(model, jsonInput, predictionResponseDTO, uid);

        Prediction prediction = new Prediction();
        prediction.setTimestamp(Instant.now());
        prediction.setInput(input);
        prediction.setOutputValue(predictionResponseDTO.getOutput().getResult());
        prediction.setReasons(jsonMapper.writeValueAsString(predictionResponseDTO.getOutput().getReasons()));
        prediction.setModel(model);
        asyncSavePredictionToPostgres(prediction);
        prediction.setModel(null);

        ResponseEntity<Prediction> response = ResponseEntity.ok(prediction);
        log.debug("REST response: {}", response);
        return response;
    }

	/**
	 * POST /models/:id/predictions : Predictions of a model.
	 *
	 * @param id
	 *            the id of the model to get predictions.
	 * @return the ResponseEntity with status 200 (Ok) or with status 400 (Bad
	 *         Request) if the request is invalid
	 */
	@PostMapping("/models/{id}/predictions")
	public ResponseEntity<List<PredictionSubset>> getPredictions(@PathVariable Long id) {

		ResponseEntity<List<PredictionSubset>> response = null;

		predictionService.findAllByModelId(id);

	    if(predictionService.findAllByModelId(id) != null) {
	    response = ResponseEntity.ok(predictionService.findAllByModelId(id));
	    }
		return response;
	}

    /**
     * POST /models/:id/upload : Upload a model.
     *
     * @param id the id of the model which is being uploaded
     * @param file model file
     * @return the file path where it is uploaded with status 200 (Ok) or with
     *         status 400 (Bad Request) if the request is invalid
     */
    @PostMapping("/models/{id}/upload")
    @Timed
    public ResponseEntity<Model> uploadFile(@PathVariable("id") Long id,
                                            @RequestParam("file") MultipartFile file) {
        log.debug("REST request to upload Model : {}", id);
        if (file == null) {
            throw new BadRequestAlertException("Model file is required", ENTITY_NAME, "invalid");
        }
        log.debug("Model file : {}", file);

        String owner = SecurityUtils.getCurrentUserLogin().get();
        Model model = modelService.findOneByOwner(id, owner);
        if (model == null) {
            throw new BadRequestAlertException("Model not found or User does not have access", ENTITY_NAME, "notfound");
        }
        if (model.getModelLocation() != null) {
            log.debug("Model location: {}", model.getModelLocation());
            throw new BadRequestAlertException("Model file already exists", ENTITY_NAME, "fileexists");
        }

        Path modelPath = modelUploadService.store(file, model.getName(), model.getVersion());
        String modelLocation = modelPath.toUri().getPath();
        log.debug("Model saved as : {}", modelLocation);

        model.setModelLocation(modelLocation);
        Model result = modelService.save(model);

        ResponseEntity<Model> response = ResponseEntity.ok().body(result);
        log.debug("Success response: {}", response);
        return response;
    }

	/**
	 * POST /models/:modelId/predictions/:predictionId/feedback : feedback of a model.
	 *
	 * @param modelId
	 *
	 * @param predictionId
	 *
	 * @return the ResponseEntity with status 200 (Ok) or with status 400 (Bad
	 *         Request) if the request is invalid
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/models/{modelId}/predictions/{predictionId}/feedback")
	public ResponseEntity<Prediction> getFeedback(@PathVariable Long modelId, @PathVariable Long predictionId, @Valid @RequestBody FeedbackResponseDTO feedbackResponseDto) {

		ResponseEntity<Prediction> response = null;

		if(predictionService.findOne(predictionId) == null) {
			throw new BadRequestAlertException("Cannot find Model with the above prediction id", "Prediction", "Not Found");
		}
		if(feedbackResponseDto.getFeedbackValue() ==null) {
			throw new BadRequestAlertException("Feedback Value cannot be null", "Prediction", "Null");

		}
		else {
			log.debug("Prediction : ",predictionService.findOne(predictionId));
			Prediction prediction = predictionService.findOne(predictionId);
			prediction.setFeedbackClass(feedbackResponseDto.getFeedbackClass());
			prediction.setFeedbackValue(feedbackResponseDto.getFeedbackValue());
			prediction.setFeedbackFlag(true);
			prediction.setFeedbackTimestamp(Instant.now());
			prediction.setFeedbackUser(SecurityUtils.getCurrentUserLogin().get());
			predictionService.save(prediction);
			response = ResponseEntity.ok(prediction);
			return response;
		}
	}
}
