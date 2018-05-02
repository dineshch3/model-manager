package com.predera.dmml.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Model.class)
public abstract class Model_ {

	public static volatile SingularAttribute<Model, String> owner;
	public static volatile SingularAttribute<Model, Integer> replicationFactor;
	public static volatile SingularAttribute<Model, String> builderConfig;
	public static volatile SingularAttribute<Model, String> performanceMetrics;
	public static volatile SingularAttribute<Model, String> project;
	public static volatile SingularAttribute<Model, String> trainingDataset;
	public static volatile SingularAttribute<Model, String> type;
	public static volatile SingularAttribute<Model, String> version;
	public static volatile SingularAttribute<Model, String> predictionUrl;
	public static volatile SingularAttribute<Model, String> library;
	public static volatile SingularAttribute<Model, Instant> createdDate;
	public static volatile SingularAttribute<Model, String> modelLocation;
	public static volatile SingularAttribute<Model, String> name;
	public static volatile SingularAttribute<Model, Instant> deployedDate;
	public static volatile SingularAttribute<Model, Long> id;
	public static volatile SingularAttribute<Model, String> featureSignificance;
	public static volatile SingularAttribute<Model, String> algorithm;
	public static volatile SingularAttribute<Model, String> status;

}

