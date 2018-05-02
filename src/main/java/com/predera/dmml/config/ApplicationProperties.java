package com.predera.dmml.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Modelmanager.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final ApplicationProperties.ModelManager modelManager = new ApplicationProperties.ModelManager();
    private final ApplicationProperties.PyModelManager pyModelManager = new ApplicationProperties.PyModelManager();
    private final ApplicationProperties.DataService dataService = new ApplicationProperties.DataService();

    public ApplicationProperties() {
    }

    public ApplicationProperties.ModelManager getModelManager() {
        return this.modelManager;
    }

    public ApplicationProperties.PyModelManager getPyModelManager() {
        return this.pyModelManager;
    }
    
    public ApplicationProperties.DataService getDataService() {
    	return this.dataService;
    }

    public static class ModelManager {
        private String predictionUrl;

        public ModelManager() {
            this.predictionUrl = "/modelmanager/api/models/{id}/predict";
        }

        public String getPredictionUrl() {
            return predictionUrl;
        }

        public void setPredictionUrl(String predictionUrl) {
            this.predictionUrl = predictionUrl;
        }
    }

    public static class PyModelManager {
        private String predictionUrl;
        private String modelDeployUrl;
        private String modelUndeployUrl;
        private String replicationFactorUrl;

        public PyModelManager() {
            this.predictionUrl = "/py-model-manager/api/{model-name}/predict";
            this.modelDeployUrl = "/py-model-manager/api/models/deploy";
            this.modelUndeployUrl = "/py-model-manager/api/models/deploy";
            this.replicationFactorUrl = "/py-model-manager/api/{model-name}/replicas";
        }

        public String getPredictionUrl() {
            return predictionUrl;
        }

        public void setPredictionUrl(String predictionUrl) {
            this.predictionUrl = predictionUrl;
        }

        public String getModelDeployUrl() {
            return modelDeployUrl;
        }

        public void setModelDeployUrl(String modelDeployUrl) {
            this.modelDeployUrl = modelDeployUrl;
        }

        public String getModelUndeployUrl() {
            return modelUndeployUrl;
        }

        public void setModelUndeployUrl(String modelUndeployUrl) {
            this.modelUndeployUrl = modelUndeployUrl;
        }

        public String getReplicationFactorUrl() {
            return replicationFactorUrl;
        }

        public void setReplicationFactorUrl(String replicationFactorUrl) {
            this.replicationFactorUrl = replicationFactorUrl;
        }
    }
    
    public class DataService {
        public Elasticsearch elasticsearch= new Elasticsearch();

		public Elasticsearch getElasticsearch() {
			return elasticsearch;
		}

		public void setElasticsearch(Elasticsearch elasticsearch) {
			this.elasticsearch = elasticsearch;
		}
    }
    
    public static class Elasticsearch {
    	private String index;
    	private String docType;
    	private String hostname;
    	private int port;
    	private String scheme;
    	
		public Elasticsearch() {
			this.index = "predictions";
			this.docType = "prediction";
			this.hostname = "localhost";
			this.port = 9200;
			this.scheme = "http";
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public String getDocType() {
			return docType;
		}

		public void setDocType(String docType) {
			this.docType = docType;
		}

		public String getHostname() {
			return hostname;
		}

		public void setHostname(String hostname) {
			this.hostname = hostname;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getScheme() {
			return scheme;
		}

		public void setScheme(String scheme) {
			this.scheme = scheme;
		}
		
    }
}
