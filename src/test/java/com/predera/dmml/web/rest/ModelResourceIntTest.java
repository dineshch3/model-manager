package com.predera.dmml.web.rest;

import static com.predera.dmml.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import com.predera.dmml.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.predera.dmml.ModelmanagerApp;
import com.predera.dmml.config.ApplicationProperties;
import com.predera.dmml.domain.Model;
import com.predera.dmml.repository.ModelRepository;
import com.predera.dmml.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the ModelResource REST controller.
 *
 * @see ModelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelmanagerApp.class)
@TestPropertySource(properties = "application.pyModelManager.predictionUrl=http://localhost:5000/py-model-manager/api/{model-name}/predict")
public class ModelResourceIntTest {

	private final Logger log = LoggerFactory.getLogger(ModelResourceIntTest.class);

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_TYPE = "AAAAAAAAAA";
	private static final String UPDATED_TYPE = "BBBBBBBBBB";

	private static final String DEFAULT_ALGORITHM = "AAAAAAAAAA";
	private static final String UPDATED_ALGORITHM = "BBBBBBBBBB";

	private static final String DEFAULT_LIBRARY = "scikit";
    private static final String UPDATED_LIBRARY = "automl";

    private static final String DEFAULT_PROJECT = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "Experiment";
	private static final String UPDATED_STATUS = "BBBBBBBBBB";

	private static final String DEFAULT_OWNER = "user";
	private static final String UPDATED_OWNER = "user";

	private static final String DEFAULT_PERFORMANCE_METRICS = "AAAAAAAAAA";
	private static final String UPDATED_PERFORMANCE_METRICS = "BBBBBBBBBB";

	private static final String DEFAULT_MODEL_LOCATION = "AAAAAAAAAA";
	private static final String UPDATED_MODEL_LOCATION = "BBBBBBBBBB";

	private static final String DEFAULT_FEATURE_SIGNIFICANCE = "AAAAAAAAAA";
	private static final String UPDATED_FEATURE_SIGNIFICANCE = "BBBBBBBBBB";

	private static final String DEFAULT_BUILDER_CONFIG = "AAAAAAAAAA";
	private static final String UPDATED_BUILDER_CONFIG = "BBBBBBBBBB";

	private static final Instant DEFAULT_CREATED_DATE = Instant.EPOCH;
	private static final Instant UPDATED_CREATED_DATE = Instant.now();

	private static final Instant DEFAULT_DEPLOYED_DATE = Instant.EPOCH;
	private static final Instant UPDATED_DEPLOYED_DATE = Instant.now();

	private static final String DEFAULT_TRAINING_DATASET = "AAAAAAAAAA";
	private static final String UPDATED_TRAINING_DATASET = "BBBBBBBBBB";

	@Autowired
	private ModelRepository modelRepository;

	@Autowired
	private ModelService modelService;

	@Autowired
	private ModelQueryService modelQueryService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private ModelUploadService modelUploadService;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private EntityManager em;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	private MockMvc restModelMockMvc;

	private Model model;

	@Autowired
	private PredictionService predictionService;

	@Autowired
	private DataService dataService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final ModelResource modelResource = new ModelResource(modelService, modelQueryService, dataService, modelUploadService,
				applicationProperties, predictionService);
		this.restModelMockMvc = MockMvcBuilders.standaloneSetup(modelResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setConversionService(createFormattingConversionService()).setMessageConverters(jacksonMessageConverter)
				.apply(springSecurity(springSecurityFilterChain)).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Model createEntity(EntityManager em) {
		Model model = new Model().name(DEFAULT_NAME).type(DEFAULT_TYPE).algorithm(DEFAULT_ALGORITHM)
				.status(DEFAULT_STATUS).owner(DEFAULT_OWNER).performanceMetrics(DEFAULT_PERFORMANCE_METRICS)
				.modelLocation(DEFAULT_MODEL_LOCATION).featureSignificance(DEFAULT_FEATURE_SIGNIFICANCE)
				.builderConfig(DEFAULT_BUILDER_CONFIG).createdDate(DEFAULT_CREATED_DATE)
				.deployedDate(DEFAULT_DEPLOYED_DATE).trainingDataset(DEFAULT_TRAINING_DATASET)
                .library(DEFAULT_LIBRARY).project(DEFAULT_PROJECT).version(DEFAULT_VERSION);
		return model;
	}

	@Before
	public void initTest() {
		model = createEntity(em);
	}

	@Test
	@Transactional
	@WithMockUser
	public void createModel() throws Exception {
		int databaseSizeBeforeCreate = modelRepository.findAll().size();

		// Create the Model
		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isCreated());

		// Validate the Model in the database
		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeCreate + 1);
		Model testModel = modelList.get(modelList.size() - 1);
		assertThat(testModel.getName()).isEqualTo(DEFAULT_NAME);
		assertThat(testModel.getType()).isEqualTo(DEFAULT_TYPE);
		assertThat(testModel.getAlgorithm()).isEqualTo(DEFAULT_ALGORITHM);
		assertThat(testModel.getStatus()).isEqualTo(DEFAULT_STATUS);
		assertThat(testModel.getOwner()).isEqualTo(DEFAULT_OWNER);
		assertThat(testModel.getPerformanceMetrics()).isEqualTo(DEFAULT_PERFORMANCE_METRICS);
		assertThat(testModel.getModelLocation()).isNull();
		assertThat(testModel.getFeatureSignificance()).isEqualTo(DEFAULT_FEATURE_SIGNIFICANCE);
		assertThat(testModel.getBuilderConfig()).isEqualTo(DEFAULT_BUILDER_CONFIG);
		assertThat(testModel.getCreatedDate()).isNotNull();
		assertThat(testModel.getDeployedDate()).isNotNull();
		assertThat(testModel.getTrainingDataset()).isEqualTo(DEFAULT_TRAINING_DATASET);
	}

	@Test
	@Transactional
	public void createModelWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = modelRepository.findAll().size();

		// Create the Model with an existing ID
		model.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		// Validate the Model in the database
		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkNameIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setName(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkTypeIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setType(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkAlgorithmIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setAlgorithm(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkStatusIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setStatus(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkOwnerIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setOwner(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkPerformanceMetricsIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setPerformanceMetrics(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkFeatureSignificanceIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setFeatureSignificance(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkBuilderConfigIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setBuilderConfig(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkCreatedDateIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setCreatedDate(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkDeployedDateIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setDeployedDate(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkTrainingDatasetIsRequired() throws Exception {
		int databaseSizeBeforeTest = modelRepository.findAll().size();
		// set the field null
		model.setTrainingDataset(null);

		// Create the Model, which fails.

		restModelMockMvc.perform(post("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isBadRequest());

		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllModels() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList
		restModelMockMvc.perform(get("/api/models?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
				.andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
				.andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
				.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
				.andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
				.andExpect(jsonPath("$.[*].performanceMetrics").value(hasItem(DEFAULT_PERFORMANCE_METRICS.toString())))
				.andExpect(jsonPath("$.[*].modelLocation").value(hasItem(DEFAULT_MODEL_LOCATION.toString())))
				.andExpect(
						jsonPath("$.[*].featureSignificance").value(hasItem(DEFAULT_FEATURE_SIGNIFICANCE.toString())))
				.andExpect(jsonPath("$.[*].builderConfig").value(hasItem(DEFAULT_BUILDER_CONFIG.toString())))
				.andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
				.andExpect(jsonPath("$.[*].deployedDate").value(hasItem(DEFAULT_DEPLOYED_DATE.toString())))
				.andExpect(jsonPath("$.[*].trainingDataset").value(hasItem(DEFAULT_TRAINING_DATASET.toString())));
	}

	@Test
	@Transactional
	public void getModel() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get the model
		restModelMockMvc.perform(get("/api/models/{id}", model.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(model.getId().intValue()))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
				.andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
				.andExpect(jsonPath("$.algorithm").value(DEFAULT_ALGORITHM.toString()))
				.andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
				.andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()))
				.andExpect(jsonPath("$.performanceMetrics").value(DEFAULT_PERFORMANCE_METRICS.toString()))
				.andExpect(jsonPath("$.modelLocation").value(DEFAULT_MODEL_LOCATION.toString()))
				.andExpect(jsonPath("$.featureSignificance").value(DEFAULT_FEATURE_SIGNIFICANCE.toString()))
				.andExpect(jsonPath("$.builderConfig").value(DEFAULT_BUILDER_CONFIG.toString()))
				.andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
				.andExpect(jsonPath("$.deployedDate").value(DEFAULT_DEPLOYED_DATE.toString()))
				.andExpect(jsonPath("$.trainingDataset").value(DEFAULT_TRAINING_DATASET.toString()));
	}

	@Test
	@Transactional
	public void getAllModelsByNameIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where name equals to DEFAULT_NAME
		defaultModelShouldBeFound("name.equals=" + DEFAULT_NAME);

		// Get all the modelList where name equals to UPDATED_NAME
		defaultModelShouldNotBeFound("name.equals=" + UPDATED_NAME);
	}

	@Test
	@Transactional
	public void getAllModelsByNameIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where name in DEFAULT_NAME or UPDATED_NAME
		defaultModelShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

		// Get all the modelList where name equals to UPDATED_NAME
		defaultModelShouldNotBeFound("name.in=" + UPDATED_NAME);
	}

	@Test
	@Transactional
	public void getAllModelsByNameIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where name is not null
		defaultModelShouldBeFound("name.specified=true");

		// Get all the modelList where name is null
		defaultModelShouldNotBeFound("name.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByTypeIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where type equals to DEFAULT_TYPE
		defaultModelShouldBeFound("type.equals=" + DEFAULT_TYPE);

		// Get all the modelList where type equals to UPDATED_TYPE
		defaultModelShouldNotBeFound("type.equals=" + UPDATED_TYPE);
	}

	@Test
	@Transactional
	public void getAllModelsByTypeIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where type in DEFAULT_TYPE or UPDATED_TYPE
		defaultModelShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

		// Get all the modelList where type equals to UPDATED_TYPE
		defaultModelShouldNotBeFound("type.in=" + UPDATED_TYPE);
	}

	@Test
	@Transactional
	public void getAllModelsByTypeIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where type is not null
		defaultModelShouldBeFound("type.specified=true");

		// Get all the modelList where type is null
		defaultModelShouldNotBeFound("type.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByAlgorithmIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where algorithm equals to DEFAULT_ALGORITHM
		defaultModelShouldBeFound("algorithm.equals=" + DEFAULT_ALGORITHM);

		// Get all the modelList where algorithm equals to UPDATED_ALGORITHM
		defaultModelShouldNotBeFound("algorithm.equals=" + UPDATED_ALGORITHM);
	}

	@Test
	@Transactional
	public void getAllModelsByAlgorithmIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where algorithm in DEFAULT_ALGORITHM or
		// UPDATED_ALGORITHM
		defaultModelShouldBeFound("algorithm.in=" + DEFAULT_ALGORITHM + "," + UPDATED_ALGORITHM);

		// Get all the modelList where algorithm equals to UPDATED_ALGORITHM
		defaultModelShouldNotBeFound("algorithm.in=" + UPDATED_ALGORITHM);
	}

	@Test
	@Transactional
	public void getAllModelsByAlgorithmIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where algorithm is not null
		defaultModelShouldBeFound("algorithm.specified=true");

		// Get all the modelList where algorithm is null
		defaultModelShouldNotBeFound("algorithm.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByStatusIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where status equals to DEFAULT_STATUS
		defaultModelShouldBeFound("status.equals=" + DEFAULT_STATUS);

		// Get all the modelList where status equals to UPDATED_STATUS
		defaultModelShouldNotBeFound("status.equals=" + UPDATED_STATUS);
	}

	@Test
	@Transactional
	public void getAllModelsByStatusIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where status in DEFAULT_STATUS or UPDATED_STATUS
		defaultModelShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

		// Get all the modelList where status equals to UPDATED_STATUS
		defaultModelShouldNotBeFound("status.in=" + UPDATED_STATUS);
	}

	@Test
	@Transactional
	public void getAllModelsByStatusIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where status is not null
		defaultModelShouldBeFound("status.specified=true");

		// Get all the modelList where status is null
		defaultModelShouldNotBeFound("status.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByOwnerIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where owner equals to DEFAULT_OWNER
		defaultModelShouldBeFound("owner.equals=" + DEFAULT_OWNER);

		// Get all the modelList where owner equals to UPDATED_OWNER
		defaultModelShouldNotBeFound("owner.equals=" + UPDATED_OWNER);
	}

	@Test
	@Transactional
	public void getAllModelsByOwnerIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where owner in DEFAULT_OWNER or UPDATED_OWNER
		defaultModelShouldBeFound("owner.in=" + DEFAULT_OWNER + "," + UPDATED_OWNER);

		// Get all the modelList where owner equals to UPDATED_OWNER
		defaultModelShouldNotBeFound("owner.in=" + UPDATED_OWNER);
	}

	@Test
	@Transactional
	public void getAllModelsByOwnerIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where owner is not null
		defaultModelShouldBeFound("owner.specified=true");

		// Get all the modelList where owner is null
		defaultModelShouldNotBeFound("owner.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByPerformanceMetricsIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where performanceMetrics equals to
		// DEFAULT_PERFORMANCE_METRICS
		defaultModelShouldBeFound("performanceMetrics.equals=" + DEFAULT_PERFORMANCE_METRICS);

		// Get all the modelList where performanceMetrics equals to
		// UPDATED_PERFORMANCE_METRICS
		defaultModelShouldNotBeFound("performanceMetrics.equals=" + UPDATED_PERFORMANCE_METRICS);
	}

	@Test
	@Transactional
	public void getAllModelsByPerformanceMetricsIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where performanceMetrics in DEFAULT_PERFORMANCE_METRICS
		// or UPDATED_PERFORMANCE_METRICS
		defaultModelShouldBeFound(
				"performanceMetrics.in=" + DEFAULT_PERFORMANCE_METRICS + "," + UPDATED_PERFORMANCE_METRICS);

		// Get all the modelList where performanceMetrics equals to
		// UPDATED_PERFORMANCE_METRICS
		defaultModelShouldNotBeFound("performanceMetrics.in=" + UPDATED_PERFORMANCE_METRICS);
	}

	@Test
	@Transactional
	public void getAllModelsByPerformanceMetricsIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where performanceMetrics is not null
		defaultModelShouldBeFound("performanceMetrics.specified=true");

		// Get all the modelList where performanceMetrics is null
		defaultModelShouldNotBeFound("performanceMetrics.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByModelLocationIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where modelLocation equals to DEFAULT_MODEL_LOCATION
		defaultModelShouldBeFound("modelLocation.equals=" + DEFAULT_MODEL_LOCATION);

		// Get all the modelList where modelLocation equals to UPDATED_MODEL_LOCATION
		defaultModelShouldNotBeFound("modelLocation.equals=" + UPDATED_MODEL_LOCATION);
	}

	@Test
	@Transactional
	public void getAllModelsByModelLocationIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where modelLocation in DEFAULT_MODEL_LOCATION or
		// UPDATED_MODEL_LOCATION
		defaultModelShouldBeFound("modelLocation.in=" + DEFAULT_MODEL_LOCATION + "," + UPDATED_MODEL_LOCATION);

		// Get all the modelList where modelLocation equals to UPDATED_MODEL_LOCATION
		defaultModelShouldNotBeFound("modelLocation.in=" + UPDATED_MODEL_LOCATION);
	}

	@Test
	@Transactional
	public void getAllModelsByModelLocationIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where modelLocation is not null
		defaultModelShouldBeFound("modelLocation.specified=true");

		// Get all the modelList where modelLocation is null
		defaultModelShouldNotBeFound("modelLocation.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByFeatureSignificanceIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where featureSignificance equals to
		// DEFAULT_FEATURE_SIGNIFICANCE
		defaultModelShouldBeFound("featureSignificance.equals=" + DEFAULT_FEATURE_SIGNIFICANCE);

		// Get all the modelList where featureSignificance equals to
		// UPDATED_FEATURE_SIGNIFICANCE
		defaultModelShouldNotBeFound("featureSignificance.equals=" + UPDATED_FEATURE_SIGNIFICANCE);
	}

	@Test
	@Transactional
	public void getAllModelsByFeatureSignificanceIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where featureSignificance in
		// DEFAULT_FEATURE_SIGNIFICANCE or UPDATED_FEATURE_SIGNIFICANCE
		defaultModelShouldBeFound(
				"featureSignificance.in=" + DEFAULT_FEATURE_SIGNIFICANCE + "," + UPDATED_FEATURE_SIGNIFICANCE);

		// Get all the modelList where featureSignificance equals to
		// UPDATED_FEATURE_SIGNIFICANCE
		defaultModelShouldNotBeFound("featureSignificance.in=" + UPDATED_FEATURE_SIGNIFICANCE);
	}

	@Test
	@Transactional
	public void getAllModelsByFeatureSignificanceIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where featureSignificance is not null
		defaultModelShouldBeFound("featureSignificance.specified=true");

		// Get all the modelList where featureSignificance is null
		defaultModelShouldNotBeFound("featureSignificance.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByBuilderConfigIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where builderConfig equals to DEFAULT_BUILDER_CONFIG
		defaultModelShouldBeFound("builderConfig.equals=" + DEFAULT_BUILDER_CONFIG);

		// Get all the modelList where builderConfig equals to UPDATED_BUILDER_CONFIG
		defaultModelShouldNotBeFound("builderConfig.equals=" + UPDATED_BUILDER_CONFIG);
	}

	@Test
	@Transactional
	public void getAllModelsByBuilderConfigIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where builderConfig in DEFAULT_BUILDER_CONFIG or
		// UPDATED_BUILDER_CONFIG
		defaultModelShouldBeFound("builderConfig.in=" + DEFAULT_BUILDER_CONFIG + "," + UPDATED_BUILDER_CONFIG);

		// Get all the modelList where builderConfig equals to UPDATED_BUILDER_CONFIG
		defaultModelShouldNotBeFound("builderConfig.in=" + UPDATED_BUILDER_CONFIG);
	}

	@Test
	@Transactional
	public void getAllModelsByBuilderConfigIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where builderConfig is not null
		defaultModelShouldBeFound("builderConfig.specified=true");

		// Get all the modelList where builderConfig is null
		defaultModelShouldNotBeFound("builderConfig.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByCreatedDateIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where createdDate equals to DEFAULT_CREATED_DATE
		defaultModelShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

		// Get all the modelList where createdDate equals to UPDATED_CREATED_DATE
		defaultModelShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
	}

	@Test
	@Transactional
	public void getAllModelsByCreatedDateIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where createdDate in DEFAULT_CREATED_DATE or
		// UPDATED_CREATED_DATE
		defaultModelShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

		// Get all the modelList where createdDate equals to UPDATED_CREATED_DATE
		defaultModelShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
	}

	@Test
	@Transactional
	public void getAllModelsByCreatedDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where createdDate is not null
		defaultModelShouldBeFound("createdDate.specified=true");

		// Get all the modelList where createdDate is null
		defaultModelShouldNotBeFound("createdDate.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where createdDate greater than or equals to
		// DEFAULT_CREATED_DATE
		defaultModelShouldBeFound("createdDate.greaterOrEqualThan=" + DEFAULT_CREATED_DATE);

		// Get all the modelList where createdDate greater than or equals to
		// UPDATED_CREATED_DATE
		defaultModelShouldNotBeFound("createdDate.greaterOrEqualThan=" + UPDATED_CREATED_DATE);
	}

	@Test
	@Transactional
	public void getAllModelsByCreatedDateIsLessThanSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where createdDate less than or equals to
		// DEFAULT_CREATED_DATE
		defaultModelShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

		// Get all the modelList where createdDate less than or equals to
		// UPDATED_CREATED_DATE
		defaultModelShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
	}

	@Test
	@Transactional
	public void getAllModelsByDeployedDateIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where deployedDate equals to DEFAULT_DEPLOYED_DATE
		defaultModelShouldBeFound("deployedDate.equals=" + DEFAULT_DEPLOYED_DATE);

		// Get all the modelList where deployedDate equals to UPDATED_DEPLOYED_DATE
		defaultModelShouldNotBeFound("deployedDate.equals=" + UPDATED_DEPLOYED_DATE);
	}

	@Test
	@Transactional
	public void getAllModelsByDeployedDateIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where deployedDate in DEFAULT_DEPLOYED_DATE or
		// UPDATED_DEPLOYED_DATE
		defaultModelShouldBeFound("deployedDate.in=" + DEFAULT_DEPLOYED_DATE + "," + UPDATED_DEPLOYED_DATE);

		// Get all the modelList where deployedDate equals to UPDATED_DEPLOYED_DATE
		defaultModelShouldNotBeFound("deployedDate.in=" + UPDATED_DEPLOYED_DATE);
	}

	@Test
	@Transactional
	public void getAllModelsByDeployedDateIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where deployedDate is not null
		defaultModelShouldBeFound("deployedDate.specified=true");

		// Get all the modelList where deployedDate is null
		defaultModelShouldNotBeFound("deployedDate.specified=false");
	}

	@Test
	@Transactional
	public void getAllModelsByDeployedDateIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where deployedDate greater than or equals to
		// DEFAULT_DEPLOYED_DATE
		defaultModelShouldBeFound("deployedDate.greaterOrEqualThan=" + DEFAULT_DEPLOYED_DATE);

		// Get all the modelList where deployedDate greater than or equals to
		// UPDATED_DEPLOYED_DATE
		defaultModelShouldNotBeFound("deployedDate.greaterOrEqualThan=" + UPDATED_DEPLOYED_DATE);
	}

	@Test
	@Transactional
	public void getAllModelsByDeployedDateIsLessThanSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where deployedDate less than or equals to
		// DEFAULT_DEPLOYED_DATE
		defaultModelShouldNotBeFound("deployedDate.lessThan=" + DEFAULT_DEPLOYED_DATE);

		// Get all the modelList where deployedDate less than or equals to
		// UPDATED_DEPLOYED_DATE
		defaultModelShouldBeFound("deployedDate.lessThan=" + UPDATED_DEPLOYED_DATE);
	}

	@Test
	@Transactional
	public void getAllModelsByTrainingDatasetIsEqualToSomething() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where trainingDataset equals to
		// DEFAULT_TRAINING_DATASET
		defaultModelShouldBeFound("trainingDataset.equals=" + DEFAULT_TRAINING_DATASET);

		// Get all the modelList where trainingDataset equals to
		// UPDATED_TRAINING_DATASET
		defaultModelShouldNotBeFound("trainingDataset.equals=" + UPDATED_TRAINING_DATASET);
	}

	@Test
	@Transactional
	public void getAllModelsByTrainingDatasetIsInShouldWork() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where trainingDataset in DEFAULT_TRAINING_DATASET or
		// UPDATED_TRAINING_DATASET
		defaultModelShouldBeFound("trainingDataset.in=" + DEFAULT_TRAINING_DATASET + "," + UPDATED_TRAINING_DATASET);

		// Get all the modelList where trainingDataset equals to
		// UPDATED_TRAINING_DATASET
		defaultModelShouldNotBeFound("trainingDataset.in=" + UPDATED_TRAINING_DATASET);
	}

	@Test
	@Transactional
	public void getAllModelsByTrainingDatasetIsNullOrNotNull() throws Exception {
		// Initialize the database
		modelRepository.saveAndFlush(model);

		// Get all the modelList where trainingDataset is not null
		defaultModelShouldBeFound("trainingDataset.specified=true");

		// Get all the modelList where trainingDataset is null
		defaultModelShouldNotBeFound("trainingDataset.specified=false");
	}

	/**
	 * Executes the search, and checks that the default entity is returned
	 */
	private void defaultModelShouldBeFound(String filter) throws Exception {
		restModelMockMvc.perform(get("/api/models?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
				.andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
				.andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
				.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
				.andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
				.andExpect(jsonPath("$.[*].performanceMetrics").value(hasItem(DEFAULT_PERFORMANCE_METRICS.toString())))
				.andExpect(jsonPath("$.[*].modelLocation").value(hasItem(DEFAULT_MODEL_LOCATION.toString())))
				.andExpect(
						jsonPath("$.[*].featureSignificance").value(hasItem(DEFAULT_FEATURE_SIGNIFICANCE.toString())))
				.andExpect(jsonPath("$.[*].builderConfig").value(hasItem(DEFAULT_BUILDER_CONFIG.toString())))
				.andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
				.andExpect(jsonPath("$.[*].deployedDate").value(hasItem(DEFAULT_DEPLOYED_DATE.toString())))
				.andExpect(jsonPath("$.[*].trainingDataset").value(hasItem(DEFAULT_TRAINING_DATASET.toString())));
	}

	/**
	 * Executes the search, and checks that the default entity is not returned
	 */
	private void defaultModelShouldNotBeFound(String filter) throws Exception {
		restModelMockMvc.perform(get("/api/models?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").isEmpty());
	}

	@Test
	@Transactional
	public void getNonExistingModel() throws Exception {
		// Get the model
		restModelMockMvc.perform(get("/api/models/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateModel() throws Exception {
		// Initialize the database
		modelService.save(model);

		int databaseSizeBeforeUpdate = modelRepository.findAll().size();

		// Update the model
		Model updatedModel = modelRepository.findOne(model.getId());
		// Disconnect from session so that the updates on updatedModel are not directly
		// saved in db
		em.detach(updatedModel);
		updatedModel.name(UPDATED_NAME).type(UPDATED_TYPE).algorithm(UPDATED_ALGORITHM).status(UPDATED_STATUS)
				.owner(UPDATED_OWNER).performanceMetrics(UPDATED_PERFORMANCE_METRICS)
				.modelLocation(UPDATED_MODEL_LOCATION).featureSignificance(UPDATED_FEATURE_SIGNIFICANCE)
				.builderConfig(UPDATED_BUILDER_CONFIG).createdDate(UPDATED_CREATED_DATE)
				.deployedDate(UPDATED_DEPLOYED_DATE).trainingDataset(UPDATED_TRAINING_DATASET);

		restModelMockMvc.perform(put("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedModel))).andExpect(status().isOk());

		// Validate the Model in the database
		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
		Model testModel = modelList.get(modelList.size() - 1);
		assertThat(testModel.getName()).isEqualTo(UPDATED_NAME);
		assertThat(testModel.getType()).isEqualTo(UPDATED_TYPE);
		assertThat(testModel.getAlgorithm()).isEqualTo(UPDATED_ALGORITHM);
		assertThat(testModel.getStatus()).isEqualTo(UPDATED_STATUS);
		assertThat(testModel.getOwner()).isEqualTo(UPDATED_OWNER);
		assertThat(testModel.getPerformanceMetrics()).isEqualTo(UPDATED_PERFORMANCE_METRICS);
		assertThat(testModel.getModelLocation()).isEqualTo(UPDATED_MODEL_LOCATION);
		assertThat(testModel.getFeatureSignificance()).isEqualTo(UPDATED_FEATURE_SIGNIFICANCE);
		assertThat(testModel.getBuilderConfig()).isEqualTo(UPDATED_BUILDER_CONFIG);
		assertThat(testModel.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
		assertThat(testModel.getDeployedDate()).isEqualTo(UPDATED_DEPLOYED_DATE);
		assertThat(testModel.getTrainingDataset()).isEqualTo(UPDATED_TRAINING_DATASET);
	}

	@Test
	@Transactional
	public void updateNonExistingModel() throws Exception {
		int databaseSizeBeforeUpdate = modelRepository.findAll().size();

		// Create the Model

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restModelMockMvc.perform(put("/api/models").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(model))).andExpect(status().isCreated());

		// Validate the Model in the database
		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteModel() throws Exception {
		// Initialize the database
		modelService.save(model);

		int databaseSizeBeforeDelete = modelRepository.findAll().size();

		// Get the model
		restModelMockMvc.perform(delete("/api/models/{id}", model.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Model> modelList = modelRepository.findAll();
		assertThat(modelList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@WithMockUser
	public void deployModel() throws Exception {
		// Initialize
		model.setName(UUID.randomUUID().toString());
		model.setModelLocation("/data/models/sdk-testing.pkl");
		model.setType("classification");
		model.setBuilderConfig(
				"{\"hyperparameters\": {\"warm_start\": false, \"oob_score\": false, \"n_jobs\": 1, \"verbose\": 0, \"max_leaf_nodes\": null, \"bootstrap\": true, \"min_samples_leaf\": 1, \"n_estimators\": 10, \"min_samples_split\": 2, \"min_weight_fraction_leaf\": 0.0, \"criterion\": \"gini\", \"random_state\": null, \"min_impurity_split\": 1e-07, \"max_features\": \"auto\", \"max_depth\": null, \"class_weight\": null}, \"type\": \"classification\", \"library\": \"sklearn\", \"algorithm\": \"Random Forest\"}");
		Model savedModel = modelService.save(model);

		deploySavedModel(savedModel);
	}

	public void deploySavedModel(Model savedModel) throws Exception {
		// Initialize
		log.debug("Model : {}", savedModel);

		LocalDate deployedDate = LocalDate.now();
		String predictionUrl = applicationProperties.getModelManager().getPredictionUrl().replaceFirst("\\{id\\}",
				Long.toString(savedModel.getId()));
		log.debug("PredictionUrl: {}", predictionUrl);

		// Perform test
		MvcResult result = restModelMockMvc
				.perform(post("/api/models/{id}/deploy", savedModel.getId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		// Verify
		Model deployedModel = modelService.findOne(savedModel.getId());
		assertThat(deployedModel.getStatus()).isEqualTo("Deployed");
		assertThat(deployedModel.getDeployedDate()).isEqualTo(deployedDate);
		assertThat(deployedModel.getPredictionUrl()).isEqualTo(predictionUrl);
	}

	@Test
	@WithMockUser
	public void undeployModel() throws Exception {
		// Initialize
		model.setName(UUID.randomUUID().toString());
		model.setModelLocation("/data/models/sdk-testing.pkl");
		Model savedModel = modelService.save(model);
		deploySavedModel(savedModel);

		// Perform test
		MvcResult result = restModelMockMvc
				.perform(post("/api/models/{id}/undeploy", savedModel.getId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		// Verify
		Model undeployedModel = modelService.findOne(savedModel.getId());
		assertThat(undeployedModel.getStatus()).isEqualTo("Experiment");
		assertThat(undeployedModel.getDeployedDate()).isEqualTo(Instant.now());
		assertThat(undeployedModel.getPredictionUrl()).isEqualTo("");

		// Verify the model can be redployed
		deploySavedModel(undeployedModel);
	}

	@Test
	@WithMockUser
	public void predictModel() throws Exception {
		// Initialize
		model.setName("credit-lr57");
		modelService.save(model);
		String input = "{\"account_length\":86.0,\"vmail_message\":29.0,\"day_mins\":225.4,\"day_calls\":79.0,\"day_charge\":38.32,\"eve_mins\":187.1,\"eve_calls\":112.0,\"eve_charge\":15.9,\"night_mins\":281.1,\"night_calls\":112.0,\"night_charge\":12.65,\"intl_mins\":12.9,\"intl_calls\":3.0,\"intl_charge\":3.48,\"custserv_calls\":1.0,\"AK\":0.0,\"AL\":0.0,\"AR\":0.0,\"AZ\":0.0,\"CA\":0.0,\"CO\":0.0,\"CT\":0.0,\"DC\":0.0,\"DE\":0.0,\"FL\":0.0,\"GA\":0.0,\"HI\":0.0,\"IA\":0.0,\"ID\":0.0,\"IL\":0.0,\"IN\":0.0,\"KS\":0.0,\"KY\":0.0,\"LA\":0.0,\"MA\":0.0,\"MD\":0.0,\"ME\":0.0,\"MI\":0.0,\"MN\":0.0,\"MO\":0.0,\"MS\":0.0,\"MT\":0.0,\"NC\":0.0,\"ND\":0.0,\"NE\":0.0,\"NH\":0.0,\"NJ\":0.0,\"NM\":0.0,\"NV\":0.0,\"NY\":0.0,\"OH\":1.0,\"OK\":0.0,\"OR\":0.0,\"PA\":0.0,\"RI\":0.0,\"SC\":0.0,\"SD\":0.0,\"TN\":0.0,\"TX\":0.0,\"UT\":0.0,\"VA\":0.0,\"VT\":0.0,\"WA\":0.0,\"WI\":0.0,\"WV\":0.0,\"WY\":0.0,\"408\":0.0,\"415\":1.0,\"510\":0.0,\"no\":1.0,\"yes\":0.0,\"vno\":0.0,\"vyes\":1.0}";
//		String uid = "123445";
		// Perform action
		MvcResult result = restModelMockMvc.perform(post("/api/models/{id}/predict" , model.getId())
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(input)))
				.andExpect(status().isOk()).andReturn();

		// Verify

	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Model.class);
		Model model1 = new Model();
		model1.setId(1L);
		Model model2 = new Model();
		model2.setId(model1.getId());
		assertThat(model1).isEqualTo(model2);
		model2.setId(2L);
		assertThat(model1).isNotEqualTo(model2);
		model1.setId(null);
		assertThat(model1).isNotEqualTo(model2);
	}
}
