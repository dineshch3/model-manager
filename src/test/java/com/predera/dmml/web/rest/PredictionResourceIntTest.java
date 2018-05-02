package com.predera.dmml.web.rest;

import static com.predera.dmml.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.predera.dmml.ModelmanagerApp;
import com.predera.dmml.domain.Model;
import com.predera.dmml.domain.Prediction;
import com.predera.dmml.repository.PredictionRepository;
import com.predera.dmml.service.PredictionQueryService;
import com.predera.dmml.service.PredictionService;
import com.predera.dmml.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the PredictionResource REST controller.
 *
 * @see PredictionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelmanagerApp.class)
public class PredictionResourceIntTest {

//    private static final LocalDate DEFAULT_TIMESTAMP = LocalDate.ofEpochDay(0L);
//    private static final LocalDate UPDATED_TIMESTAMP = LocalDate.now(ZoneId.systemDefault());
	
	private static final Instant DEFAULT_TIMESTAMP = Instant.EPOCH;
    private static final Instant UPDATED_TIMESTAMP = Instant.now();

    private static final String DEFAULT_INPUT = "AAAAAAAAAA";
    private static final String UPDATED_INPUT = "BBBBBBBBBB";

    private static final Long DEFAULT_QUERY_ID = 1L;
    private static final Long UPDATED_QUERY_ID = 2L;

    private static final String DEFAULT_OUTPUT_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_CLASS = "BBBBBBBBBB";

    private static final Float DEFAULT_OUTPUT_VALUE = 1F;
    private static final Float UPDATED_OUTPUT_VALUE = 2F;

    private static final Boolean DEFAULT_FEEDBACK_FLAG = false;
    private static final Boolean UPDATED_FEEDBACK_FLAG = true;

    private static final String DEFAULT_FEEDBACK_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACK_CLASS = "BBBBBBBBBB";

    private static final Float DEFAULT_FEEDBACK_VALUE = 1F;
    private static final Float UPDATED_FEEDBACK_VALUE = 2F;

//    private static final LocalDate DEFAULT_FEEDBACK_TIMESTAMP = LocalDate.ofEpochDay(0L);
//    private static final LocalDate UPDATED_FEEDBACK_TIMESTAMP = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_FEEDBACK_TIMESTAMP = Instant.EPOCH;
    private static final Instant UPDATED_FEEDBACK_TIMESTAMP = Instant.now();
    
    private static final String DEFAULT_FEEDBACK_USER = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACK_USER = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_DESC = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ERROR_FLAG = false;
    private static final Boolean UPDATED_ERROR_FLAG = true;

    private static final String DEFAULT_ERROR_DESC = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_CAUSE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_CAUSE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEFAULT_FLAG = false;
    private static final Boolean UPDATED_DEFAULT_FLAG = true;

    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private PredictionQueryService predictionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPredictionMockMvc;

    private Prediction prediction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PredictionResource predictionResource = new PredictionResource(predictionService, predictionQueryService);
        this.restPredictionMockMvc = MockMvcBuilders.standaloneSetup(predictionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prediction createEntity(EntityManager em) {
        Prediction prediction = new Prediction()
            .timestamp(DEFAULT_TIMESTAMP)
            .input(DEFAULT_INPUT)
            .queryId(DEFAULT_QUERY_ID)
            .outputClass(DEFAULT_OUTPUT_CLASS)
            .outputValue(DEFAULT_OUTPUT_VALUE)
            .feedbackFlag(DEFAULT_FEEDBACK_FLAG)
            .feedbackClass(DEFAULT_FEEDBACK_CLASS)
            .feedbackValue(DEFAULT_FEEDBACK_VALUE)
            .feedbackTimestamp(DEFAULT_FEEDBACK_TIMESTAMP)
            .feedbackUser(DEFAULT_FEEDBACK_USER)
            .defaultDesc(DEFAULT_DEFAULT_DESC)
            .errorFlag(DEFAULT_ERROR_FLAG)
            .errorDesc(DEFAULT_ERROR_DESC)
            .errorCause(DEFAULT_ERROR_CAUSE)
            .defaultFlag(DEFAULT_DEFAULT_FLAG);
        // Add required entity
        Model model = ModelResourceIntTest.createEntity(em);
        em.persist(model);
        em.flush();
        prediction.setModel(model);
        return prediction;
    }

    @Before
    public void initTest() {
        prediction = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrediction() throws Exception {
        int databaseSizeBeforeCreate = predictionRepository.findAll().size();

        // Create the Prediction
        restPredictionMockMvc.perform(post("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isCreated());

        // Validate the Prediction in the database
        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeCreate + 1);
        Prediction testPrediction = predictionList.get(predictionList.size() - 1);
        assertThat(testPrediction.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testPrediction.getInput()).isEqualTo(DEFAULT_INPUT);
        assertThat(testPrediction.getQueryId()).isEqualTo(DEFAULT_QUERY_ID);
        assertThat(testPrediction.getOutputClass()).isEqualTo(DEFAULT_OUTPUT_CLASS);
        assertThat(testPrediction.getOutputValue()).isEqualTo(DEFAULT_OUTPUT_VALUE);
        assertThat(testPrediction.isFeedbackFlag()).isEqualTo(DEFAULT_FEEDBACK_FLAG);
        assertThat(testPrediction.getFeedbackClass()).isEqualTo(DEFAULT_FEEDBACK_CLASS);
        assertThat(testPrediction.getFeedbackValue()).isEqualTo(DEFAULT_FEEDBACK_VALUE);
        assertThat(testPrediction.getFeedbackTimestamp()).isEqualTo(DEFAULT_FEEDBACK_TIMESTAMP);
        assertThat(testPrediction.getFeedbackUser()).isEqualTo(DEFAULT_FEEDBACK_USER);
        assertThat(testPrediction.getDefaultDesc()).isEqualTo(DEFAULT_DEFAULT_DESC);
        assertThat(testPrediction.isErrorFlag()).isEqualTo(DEFAULT_ERROR_FLAG);
        assertThat(testPrediction.getErrorDesc()).isEqualTo(DEFAULT_ERROR_DESC);
        assertThat(testPrediction.getErrorCause()).isEqualTo(DEFAULT_ERROR_CAUSE);
        assertThat(testPrediction.isDefaultFlag()).isEqualTo(DEFAULT_DEFAULT_FLAG);
    }

    @Test
    @Transactional
    public void createPredictionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = predictionRepository.findAll().size();

        // Create the Prediction with an existing ID
        prediction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPredictionMockMvc.perform(post("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isBadRequest());

        // Validate the Prediction in the database
        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = predictionRepository.findAll().size();
        // set the field null
        prediction.setTimestamp(null);

        // Create the Prediction, which fails.

        restPredictionMockMvc.perform(post("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isBadRequest());

        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInputIsRequired() throws Exception {
        int databaseSizeBeforeTest = predictionRepository.findAll().size();
        // set the field null
        prediction.setInput(null);

        // Create the Prediction, which fails.

        restPredictionMockMvc.perform(post("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isBadRequest());

        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOutputValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = predictionRepository.findAll().size();
        // set the field null
        prediction.setOutputValue(null);

        // Create the Prediction, which fails.

        restPredictionMockMvc.perform(post("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isBadRequest());

        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPredictions() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList
        restPredictionMockMvc.perform(get("/api/predictions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].input").value(hasItem(DEFAULT_INPUT.toString())))
            .andExpect(jsonPath("$.[*].queryId").value(hasItem(DEFAULT_QUERY_ID.intValue())))
            .andExpect(jsonPath("$.[*].outputClass").value(hasItem(DEFAULT_OUTPUT_CLASS.toString())))
            .andExpect(jsonPath("$.[*].outputValue").value(hasItem(DEFAULT_OUTPUT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].feedbackFlag").value(hasItem(DEFAULT_FEEDBACK_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].feedbackClass").value(hasItem(DEFAULT_FEEDBACK_CLASS.toString())))
            .andExpect(jsonPath("$.[*].feedbackValue").value(hasItem(DEFAULT_FEEDBACK_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].feedbackTimestamp").value(hasItem(DEFAULT_FEEDBACK_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].feedbackUser").value(hasItem(DEFAULT_FEEDBACK_USER.toString())))
            .andExpect(jsonPath("$.[*].default_desc").value(hasItem(DEFAULT_DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].error_flag").value(hasItem(DEFAULT_ERROR_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].error_desc").value(hasItem(DEFAULT_ERROR_DESC.toString())))
            .andExpect(jsonPath("$.[*].error_cause").value(hasItem(DEFAULT_ERROR_CAUSE.toString())))
            .andExpect(jsonPath("$.[*].default_flag").value(hasItem(DEFAULT_DEFAULT_FLAG.booleanValue())));
    }

    @Test
    @Transactional
    public void getPrediction() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get the prediction
        restPredictionMockMvc.perform(get("/api/predictions/{id}", prediction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prediction.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.input").value(DEFAULT_INPUT.toString()))
            .andExpect(jsonPath("$.queryId").value(DEFAULT_QUERY_ID.intValue()))
            .andExpect(jsonPath("$.outputClass").value(DEFAULT_OUTPUT_CLASS.toString()))
            .andExpect(jsonPath("$.outputValue").value(DEFAULT_OUTPUT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.feedbackFlag").value(DEFAULT_FEEDBACK_FLAG.booleanValue()))
            .andExpect(jsonPath("$.feedbackClass").value(DEFAULT_FEEDBACK_CLASS.toString()))
            .andExpect(jsonPath("$.feedbackValue").value(DEFAULT_FEEDBACK_VALUE.doubleValue()))
            .andExpect(jsonPath("$.feedbackTimestamp").value(DEFAULT_FEEDBACK_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.feedbackUser").value(DEFAULT_FEEDBACK_USER.toString()))
            .andExpect(jsonPath("$.default_desc").value(DEFAULT_DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.error_flag").value(DEFAULT_ERROR_FLAG.booleanValue()))
            .andExpect(jsonPath("$.error_desc").value(DEFAULT_ERROR_DESC.toString()))
            .andExpect(jsonPath("$.error_cause").value(DEFAULT_ERROR_CAUSE.toString()))
            .andExpect(jsonPath("$.default_flag").value(DEFAULT_DEFAULT_FLAG.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllPredictionsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where timestamp equals to DEFAULT_TIMESTAMP
        defaultPredictionShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the predictionList where timestamp equals to UPDATED_TIMESTAMP
        defaultPredictionShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllPredictionsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultPredictionShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the predictionList where timestamp equals to UPDATED_TIMESTAMP
        defaultPredictionShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllPredictionsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where timestamp is not null
        defaultPredictionShouldBeFound("timestamp.specified=true");

        // Get all the predictionList where timestamp is null
        defaultPredictionShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where timestamp greater than or equals to DEFAULT_TIMESTAMP
        defaultPredictionShouldBeFound("timestamp.greaterOrEqualThan=" + DEFAULT_TIMESTAMP);

        // Get all the predictionList where timestamp greater than or equals to UPDATED_TIMESTAMP
        defaultPredictionShouldNotBeFound("timestamp.greaterOrEqualThan=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllPredictionsByTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where timestamp less than or equals to DEFAULT_TIMESTAMP
        defaultPredictionShouldNotBeFound("timestamp.lessThan=" + DEFAULT_TIMESTAMP);

        // Get all the predictionList where timestamp less than or equals to UPDATED_TIMESTAMP
        defaultPredictionShouldBeFound("timestamp.lessThan=" + UPDATED_TIMESTAMP);
    }


    @Test
    @Transactional
    public void getAllPredictionsByQueryIdIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where queryId equals to DEFAULT_QUERY_ID
        defaultPredictionShouldBeFound("queryId.equals=" + DEFAULT_QUERY_ID);

        // Get all the predictionList where queryId equals to UPDATED_QUERY_ID
        defaultPredictionShouldNotBeFound("queryId.equals=" + UPDATED_QUERY_ID);
    }

    @Test
    @Transactional
    public void getAllPredictionsByQueryIdIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where queryId in DEFAULT_QUERY_ID or UPDATED_QUERY_ID
        defaultPredictionShouldBeFound("queryId.in=" + DEFAULT_QUERY_ID + "," + UPDATED_QUERY_ID);

        // Get all the predictionList where queryId equals to UPDATED_QUERY_ID
        defaultPredictionShouldNotBeFound("queryId.in=" + UPDATED_QUERY_ID);
    }

    @Test
    @Transactional
    public void getAllPredictionsByQueryIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where queryId is not null
        defaultPredictionShouldBeFound("queryId.specified=true");

        // Get all the predictionList where queryId is null
        defaultPredictionShouldNotBeFound("queryId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByQueryIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where queryId greater than or equals to DEFAULT_QUERY_ID
        defaultPredictionShouldBeFound("queryId.greaterOrEqualThan=" + DEFAULT_QUERY_ID);

        // Get all the predictionList where queryId greater than or equals to UPDATED_QUERY_ID
        defaultPredictionShouldNotBeFound("queryId.greaterOrEqualThan=" + UPDATED_QUERY_ID);
    }

    @Test
    @Transactional
    public void getAllPredictionsByQueryIdIsLessThanSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where queryId less than or equals to DEFAULT_QUERY_ID
        defaultPredictionShouldNotBeFound("queryId.lessThan=" + DEFAULT_QUERY_ID);

        // Get all the predictionList where queryId less than or equals to UPDATED_QUERY_ID
        defaultPredictionShouldBeFound("queryId.lessThan=" + UPDATED_QUERY_ID);
    }


    @Test
    @Transactional
    public void getAllPredictionsByOutputClassIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where outputClass equals to DEFAULT_OUTPUT_CLASS
        defaultPredictionShouldBeFound("outputClass.equals=" + DEFAULT_OUTPUT_CLASS);

        // Get all the predictionList where outputClass equals to UPDATED_OUTPUT_CLASS
        defaultPredictionShouldNotBeFound("outputClass.equals=" + UPDATED_OUTPUT_CLASS);
    }

    @Test
    @Transactional
    public void getAllPredictionsByOutputClassIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where outputClass in DEFAULT_OUTPUT_CLASS or UPDATED_OUTPUT_CLASS
        defaultPredictionShouldBeFound("outputClass.in=" + DEFAULT_OUTPUT_CLASS + "," + UPDATED_OUTPUT_CLASS);

        // Get all the predictionList where outputClass equals to UPDATED_OUTPUT_CLASS
        defaultPredictionShouldNotBeFound("outputClass.in=" + UPDATED_OUTPUT_CLASS);
    }

    @Test
    @Transactional
    public void getAllPredictionsByOutputClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where outputClass is not null
        defaultPredictionShouldBeFound("outputClass.specified=true");

        // Get all the predictionList where outputClass is null
        defaultPredictionShouldNotBeFound("outputClass.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByOutputValueIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where outputValue equals to DEFAULT_OUTPUT_VALUE
        defaultPredictionShouldBeFound("outputValue.equals=" + DEFAULT_OUTPUT_VALUE);

        // Get all the predictionList where outputValue equals to UPDATED_OUTPUT_VALUE
        defaultPredictionShouldNotBeFound("outputValue.equals=" + UPDATED_OUTPUT_VALUE);
    }

    @Test
    @Transactional
    public void getAllPredictionsByOutputValueIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where outputValue in DEFAULT_OUTPUT_VALUE or UPDATED_OUTPUT_VALUE
        defaultPredictionShouldBeFound("outputValue.in=" + DEFAULT_OUTPUT_VALUE + "," + UPDATED_OUTPUT_VALUE);

        // Get all the predictionList where outputValue equals to UPDATED_OUTPUT_VALUE
        defaultPredictionShouldNotBeFound("outputValue.in=" + UPDATED_OUTPUT_VALUE);
    }

    @Test
    @Transactional
    public void getAllPredictionsByOutputValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where outputValue is not null
        defaultPredictionShouldBeFound("outputValue.specified=true");

        // Get all the predictionList where outputValue is null
        defaultPredictionShouldNotBeFound("outputValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackFlag equals to DEFAULT_FEEDBACK_FLAG
        defaultPredictionShouldBeFound("feedbackFlag.equals=" + DEFAULT_FEEDBACK_FLAG);

        // Get all the predictionList where feedbackFlag equals to UPDATED_FEEDBACK_FLAG
        defaultPredictionShouldNotBeFound("feedbackFlag.equals=" + UPDATED_FEEDBACK_FLAG);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackFlagIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackFlag in DEFAULT_FEEDBACK_FLAG or UPDATED_FEEDBACK_FLAG
        defaultPredictionShouldBeFound("feedbackFlag.in=" + DEFAULT_FEEDBACK_FLAG + "," + UPDATED_FEEDBACK_FLAG);

        // Get all the predictionList where feedbackFlag equals to UPDATED_FEEDBACK_FLAG
        defaultPredictionShouldNotBeFound("feedbackFlag.in=" + UPDATED_FEEDBACK_FLAG);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackFlag is not null
        defaultPredictionShouldBeFound("feedbackFlag.specified=true");

        // Get all the predictionList where feedbackFlag is null
        defaultPredictionShouldNotBeFound("feedbackFlag.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackClassIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackClass equals to DEFAULT_FEEDBACK_CLASS
        defaultPredictionShouldBeFound("feedbackClass.equals=" + DEFAULT_FEEDBACK_CLASS);

        // Get all the predictionList where feedbackClass equals to UPDATED_FEEDBACK_CLASS
        defaultPredictionShouldNotBeFound("feedbackClass.equals=" + UPDATED_FEEDBACK_CLASS);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackClassIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackClass in DEFAULT_FEEDBACK_CLASS or UPDATED_FEEDBACK_CLASS
        defaultPredictionShouldBeFound("feedbackClass.in=" + DEFAULT_FEEDBACK_CLASS + "," + UPDATED_FEEDBACK_CLASS);

        // Get all the predictionList where feedbackClass equals to UPDATED_FEEDBACK_CLASS
        defaultPredictionShouldNotBeFound("feedbackClass.in=" + UPDATED_FEEDBACK_CLASS);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackClass is not null
        defaultPredictionShouldBeFound("feedbackClass.specified=true");

        // Get all the predictionList where feedbackClass is null
        defaultPredictionShouldNotBeFound("feedbackClass.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackValueIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackValue equals to DEFAULT_FEEDBACK_VALUE
        defaultPredictionShouldBeFound("feedbackValue.equals=" + DEFAULT_FEEDBACK_VALUE);

        // Get all the predictionList where feedbackValue equals to UPDATED_FEEDBACK_VALUE
        defaultPredictionShouldNotBeFound("feedbackValue.equals=" + UPDATED_FEEDBACK_VALUE);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackValueIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackValue in DEFAULT_FEEDBACK_VALUE or UPDATED_FEEDBACK_VALUE
        defaultPredictionShouldBeFound("feedbackValue.in=" + DEFAULT_FEEDBACK_VALUE + "," + UPDATED_FEEDBACK_VALUE);

        // Get all the predictionList where feedbackValue equals to UPDATED_FEEDBACK_VALUE
        defaultPredictionShouldNotBeFound("feedbackValue.in=" + UPDATED_FEEDBACK_VALUE);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackValue is not null
        defaultPredictionShouldBeFound("feedbackValue.specified=true");

        // Get all the predictionList where feedbackValue is null
        defaultPredictionShouldNotBeFound("feedbackValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackTimestamp equals to DEFAULT_FEEDBACK_TIMESTAMP
        defaultPredictionShouldBeFound("feedbackTimestamp.equals=" + DEFAULT_FEEDBACK_TIMESTAMP);

        // Get all the predictionList where feedbackTimestamp equals to UPDATED_FEEDBACK_TIMESTAMP
        defaultPredictionShouldNotBeFound("feedbackTimestamp.equals=" + UPDATED_FEEDBACK_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackTimestamp in DEFAULT_FEEDBACK_TIMESTAMP or UPDATED_FEEDBACK_TIMESTAMP
        defaultPredictionShouldBeFound("feedbackTimestamp.in=" + DEFAULT_FEEDBACK_TIMESTAMP + "," + UPDATED_FEEDBACK_TIMESTAMP);

        // Get all the predictionList where feedbackTimestamp equals to UPDATED_FEEDBACK_TIMESTAMP
        defaultPredictionShouldNotBeFound("feedbackTimestamp.in=" + UPDATED_FEEDBACK_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackTimestamp is not null
        defaultPredictionShouldBeFound("feedbackTimestamp.specified=true");

        // Get all the predictionList where feedbackTimestamp is null
        defaultPredictionShouldNotBeFound("feedbackTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackTimestamp greater than or equals to DEFAULT_FEEDBACK_TIMESTAMP
        defaultPredictionShouldBeFound("feedbackTimestamp.greaterOrEqualThan=" + DEFAULT_FEEDBACK_TIMESTAMP);

        // Get all the predictionList where feedbackTimestamp greater than or equals to UPDATED_FEEDBACK_TIMESTAMP
        defaultPredictionShouldNotBeFound("feedbackTimestamp.greaterOrEqualThan=" + UPDATED_FEEDBACK_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackTimestamp less than or equals to DEFAULT_FEEDBACK_TIMESTAMP
        defaultPredictionShouldNotBeFound("feedbackTimestamp.lessThan=" + DEFAULT_FEEDBACK_TIMESTAMP);

        // Get all the predictionList where feedbackTimestamp less than or equals to UPDATED_FEEDBACK_TIMESTAMP
        defaultPredictionShouldBeFound("feedbackTimestamp.lessThan=" + UPDATED_FEEDBACK_TIMESTAMP);
    }


    @Test
    @Transactional
    public void getAllPredictionsByFeedbackUserIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackUser equals to DEFAULT_FEEDBACK_USER
        defaultPredictionShouldBeFound("feedbackUser.equals=" + DEFAULT_FEEDBACK_USER);

        // Get all the predictionList where feedbackUser equals to UPDATED_FEEDBACK_USER
        defaultPredictionShouldNotBeFound("feedbackUser.equals=" + UPDATED_FEEDBACK_USER);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackUserIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackUser in DEFAULT_FEEDBACK_USER or UPDATED_FEEDBACK_USER
        defaultPredictionShouldBeFound("feedbackUser.in=" + DEFAULT_FEEDBACK_USER + "," + UPDATED_FEEDBACK_USER);

        // Get all the predictionList where feedbackUser equals to UPDATED_FEEDBACK_USER
        defaultPredictionShouldNotBeFound("feedbackUser.in=" + UPDATED_FEEDBACK_USER);
    }

    @Test
    @Transactional
    public void getAllPredictionsByFeedbackUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where feedbackUser is not null
        defaultPredictionShouldBeFound("feedbackUser.specified=true");

        // Get all the predictionList where feedbackUser is null
        defaultPredictionShouldNotBeFound("feedbackUser.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByDefault_descIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where default_desc equals to DEFAULT_DEFAULT_DESC
        defaultPredictionShouldBeFound("default_desc.equals=" + DEFAULT_DEFAULT_DESC);

        // Get all the predictionList where default_desc equals to UPDATED_DEFAULT_DESC
        defaultPredictionShouldNotBeFound("default_desc.equals=" + UPDATED_DEFAULT_DESC);
    }

    @Test
    @Transactional
    public void getAllPredictionsByDefault_descIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where default_desc in DEFAULT_DEFAULT_DESC or UPDATED_DEFAULT_DESC
        defaultPredictionShouldBeFound("default_desc.in=" + DEFAULT_DEFAULT_DESC + "," + UPDATED_DEFAULT_DESC);

        // Get all the predictionList where default_desc equals to UPDATED_DEFAULT_DESC
        defaultPredictionShouldNotBeFound("default_desc.in=" + UPDATED_DEFAULT_DESC);
    }

    @Test
    @Transactional
    public void getAllPredictionsByDefault_descIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where default_desc is not null
        defaultPredictionShouldBeFound("default_desc.specified=true");

        // Get all the predictionList where default_desc is null
        defaultPredictionShouldNotBeFound("default_desc.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByError_flagIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where error_flag equals to DEFAULT_ERROR_FLAG
        defaultPredictionShouldBeFound("error_flag.equals=" + DEFAULT_ERROR_FLAG);

        // Get all the predictionList where error_flag equals to UPDATED_ERROR_FLAG
        defaultPredictionShouldNotBeFound("error_flag.equals=" + UPDATED_ERROR_FLAG);
    }

    @Test
    @Transactional
    public void getAllPredictionsByError_flagIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where error_flag in DEFAULT_ERROR_FLAG or UPDATED_ERROR_FLAG
        defaultPredictionShouldBeFound("error_flag.in=" + DEFAULT_ERROR_FLAG + "," + UPDATED_ERROR_FLAG);

        // Get all the predictionList where error_flag equals to UPDATED_ERROR_FLAG
        defaultPredictionShouldNotBeFound("error_flag.in=" + UPDATED_ERROR_FLAG);
    }

    @Test
    @Transactional
    public void getAllPredictionsByError_flagIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where error_flag is not null
        defaultPredictionShouldBeFound("error_flag.specified=true");

        // Get all the predictionList where error_flag is null
        defaultPredictionShouldNotBeFound("error_flag.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByError_descIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where error_desc equals to DEFAULT_ERROR_DESC
        defaultPredictionShouldBeFound("error_desc.equals=" + DEFAULT_ERROR_DESC);

        // Get all the predictionList where error_desc equals to UPDATED_ERROR_DESC
        defaultPredictionShouldNotBeFound("error_desc.equals=" + UPDATED_ERROR_DESC);
    }

    @Test
    @Transactional
    public void getAllPredictionsByError_descIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where error_desc in DEFAULT_ERROR_DESC or UPDATED_ERROR_DESC
        defaultPredictionShouldBeFound("error_desc.in=" + DEFAULT_ERROR_DESC + "," + UPDATED_ERROR_DESC);

        // Get all the predictionList where error_desc equals to UPDATED_ERROR_DESC
        defaultPredictionShouldNotBeFound("error_desc.in=" + UPDATED_ERROR_DESC);
    }

    @Test
    @Transactional
    public void getAllPredictionsByError_descIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where error_desc is not null
        defaultPredictionShouldBeFound("error_desc.specified=true");

        // Get all the predictionList where error_desc is null
        defaultPredictionShouldNotBeFound("error_desc.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByError_causeIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where error_cause equals to DEFAULT_ERROR_CAUSE
        defaultPredictionShouldBeFound("error_cause.equals=" + DEFAULT_ERROR_CAUSE);

        // Get all the predictionList where error_cause equals to UPDATED_ERROR_CAUSE
        defaultPredictionShouldNotBeFound("error_cause.equals=" + UPDATED_ERROR_CAUSE);
    }

    @Test
    @Transactional
    public void getAllPredictionsByError_causeIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where error_cause in DEFAULT_ERROR_CAUSE or UPDATED_ERROR_CAUSE
        defaultPredictionShouldBeFound("error_cause.in=" + DEFAULT_ERROR_CAUSE + "," + UPDATED_ERROR_CAUSE);

        // Get all the predictionList where error_cause equals to UPDATED_ERROR_CAUSE
        defaultPredictionShouldNotBeFound("error_cause.in=" + UPDATED_ERROR_CAUSE);
    }

    @Test
    @Transactional
    public void getAllPredictionsByError_causeIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where error_cause is not null
        defaultPredictionShouldBeFound("error_cause.specified=true");

        // Get all the predictionList where error_cause is null
        defaultPredictionShouldNotBeFound("error_cause.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByDefault_flagIsEqualToSomething() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where default_flag equals to DEFAULT_DEFAULT_FLAG
        defaultPredictionShouldBeFound("default_flag.equals=" + DEFAULT_DEFAULT_FLAG);

        // Get all the predictionList where default_flag equals to UPDATED_DEFAULT_FLAG
        defaultPredictionShouldNotBeFound("default_flag.equals=" + UPDATED_DEFAULT_FLAG);
    }

    @Test
    @Transactional
    public void getAllPredictionsByDefault_flagIsInShouldWork() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where default_flag in DEFAULT_DEFAULT_FLAG or UPDATED_DEFAULT_FLAG
        defaultPredictionShouldBeFound("default_flag.in=" + DEFAULT_DEFAULT_FLAG + "," + UPDATED_DEFAULT_FLAG);

        // Get all the predictionList where default_flag equals to UPDATED_DEFAULT_FLAG
        defaultPredictionShouldNotBeFound("default_flag.in=" + UPDATED_DEFAULT_FLAG);
    }

    @Test
    @Transactional
    public void getAllPredictionsByDefault_flagIsNullOrNotNull() throws Exception {
        // Initialize the database
        predictionRepository.saveAndFlush(prediction);

        // Get all the predictionList where default_flag is not null
        defaultPredictionShouldBeFound("default_flag.specified=true");

        // Get all the predictionList where default_flag is null
        defaultPredictionShouldNotBeFound("default_flag.specified=false");
    }

    @Test
    @Transactional
    public void getAllPredictionsByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        Model model = ModelResourceIntTest.createEntity(em);
        em.persist(model);
        em.flush();
        prediction.setModel(model);
        predictionRepository.saveAndFlush(prediction);
        Long modelId = model.getId();

        // Get all the predictionList where model equals to modelId
        defaultPredictionShouldBeFound("modelId.equals=" + modelId);

        // Get all the predictionList where model equals to modelId + 1
        defaultPredictionShouldNotBeFound("modelId.equals=" + (modelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPredictionShouldBeFound(String filter) throws Exception {
        restPredictionMockMvc.perform(get("/api/predictions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].input").value(hasItem(DEFAULT_INPUT.toString())))
            .andExpect(jsonPath("$.[*].queryId").value(hasItem(DEFAULT_QUERY_ID.intValue())))
            .andExpect(jsonPath("$.[*].outputClass").value(hasItem(DEFAULT_OUTPUT_CLASS.toString())))
            .andExpect(jsonPath("$.[*].outputValue").value(hasItem(DEFAULT_OUTPUT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].feedbackFlag").value(hasItem(DEFAULT_FEEDBACK_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].feedbackClass").value(hasItem(DEFAULT_FEEDBACK_CLASS.toString())))
            .andExpect(jsonPath("$.[*].feedbackValue").value(hasItem(DEFAULT_FEEDBACK_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].feedbackTimestamp").value(hasItem(DEFAULT_FEEDBACK_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].feedbackUser").value(hasItem(DEFAULT_FEEDBACK_USER.toString())))
            .andExpect(jsonPath("$.[*].default_desc").value(hasItem(DEFAULT_DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].error_flag").value(hasItem(DEFAULT_ERROR_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].error_desc").value(hasItem(DEFAULT_ERROR_DESC.toString())))
            .andExpect(jsonPath("$.[*].error_cause").value(hasItem(DEFAULT_ERROR_CAUSE.toString())))
            .andExpect(jsonPath("$.[*].default_flag").value(hasItem(DEFAULT_DEFAULT_FLAG.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPredictionShouldNotBeFound(String filter) throws Exception {
        restPredictionMockMvc.perform(get("/api/predictions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPrediction() throws Exception {
        // Get the prediction
        restPredictionMockMvc.perform(get("/api/predictions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrediction() throws Exception {
        // Initialize the database
        predictionService.save(prediction);

        int databaseSizeBeforeUpdate = predictionRepository.findAll().size();

        // Update the prediction
        Prediction updatedPrediction = predictionRepository.findOne(prediction.getId());
        // Disconnect from session so that the updates on updatedPrediction are not directly saved in db
        em.detach(updatedPrediction);
        updatedPrediction
            .timestamp(UPDATED_TIMESTAMP)
            .input(UPDATED_INPUT)
            .queryId(UPDATED_QUERY_ID)
            .outputClass(UPDATED_OUTPUT_CLASS)
            .outputValue(UPDATED_OUTPUT_VALUE)
            .feedbackFlag(UPDATED_FEEDBACK_FLAG)
            .feedbackClass(UPDATED_FEEDBACK_CLASS)
            .feedbackValue(UPDATED_FEEDBACK_VALUE)
            .feedbackTimestamp(UPDATED_FEEDBACK_TIMESTAMP)
            .feedbackUser(UPDATED_FEEDBACK_USER)
            .defaultDesc(UPDATED_DEFAULT_DESC)
            .errorFlag(UPDATED_ERROR_FLAG)
            .errorDesc(UPDATED_ERROR_DESC)
            .errorCause(UPDATED_ERROR_CAUSE)
            .defaultFlag(UPDATED_DEFAULT_FLAG);

        restPredictionMockMvc.perform(put("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrediction)))
            .andExpect(status().isOk());

        // Validate the Prediction in the database
        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeUpdate);
        Prediction testPrediction = predictionList.get(predictionList.size() - 1);
        assertThat(testPrediction.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testPrediction.getInput()).isEqualTo(UPDATED_INPUT);
        assertThat(testPrediction.getQueryId()).isEqualTo(UPDATED_QUERY_ID);
        assertThat(testPrediction.getOutputClass()).isEqualTo(UPDATED_OUTPUT_CLASS);
        assertThat(testPrediction.getOutputValue()).isEqualTo(UPDATED_OUTPUT_VALUE);
        assertThat(testPrediction.isFeedbackFlag()).isEqualTo(UPDATED_FEEDBACK_FLAG);
        assertThat(testPrediction.getFeedbackClass()).isEqualTo(UPDATED_FEEDBACK_CLASS);
        assertThat(testPrediction.getFeedbackValue()).isEqualTo(UPDATED_FEEDBACK_VALUE);
        assertThat(testPrediction.getFeedbackTimestamp()).isEqualTo(UPDATED_FEEDBACK_TIMESTAMP);
        assertThat(testPrediction.getFeedbackUser()).isEqualTo(UPDATED_FEEDBACK_USER);
        assertThat(testPrediction.getDefaultDesc()).isEqualTo(UPDATED_DEFAULT_DESC);
        assertThat(testPrediction.isErrorFlag()).isEqualTo(UPDATED_ERROR_FLAG);
        assertThat(testPrediction.getErrorDesc()).isEqualTo(UPDATED_ERROR_DESC);
        assertThat(testPrediction.getErrorCause()).isEqualTo(UPDATED_ERROR_CAUSE);
        assertThat(testPrediction.isDefaultFlag()).isEqualTo(UPDATED_DEFAULT_FLAG);
    }

    @Test
    @Transactional
    public void updateNonExistingPrediction() throws Exception {
        int databaseSizeBeforeUpdate = predictionRepository.findAll().size();

        // Create the Prediction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPredictionMockMvc.perform(put("/api/predictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prediction)))
            .andExpect(status().isCreated());

        // Validate the Prediction in the database
        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrediction() throws Exception {
        // Initialize the database
        predictionService.save(prediction);

        int databaseSizeBeforeDelete = predictionRepository.findAll().size();

        // Get the prediction
        restPredictionMockMvc.perform(delete("/api/predictions/{id}", prediction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Prediction> predictionList = predictionRepository.findAll();
        assertThat(predictionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prediction.class);
        Prediction prediction1 = new Prediction();
        prediction1.setId(1L);
        Prediction prediction2 = new Prediction();
        prediction2.setId(prediction1.getId());
        assertThat(prediction1).isEqualTo(prediction2);
        prediction2.setId(2L);
        assertThat(prediction1).isNotEqualTo(prediction2);
        prediction1.setId(null);
        assertThat(prediction1).isNotEqualTo(prediction2);
    }
}
