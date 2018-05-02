package com.predera.dmml.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Prediction.class)
public abstract class Prediction_ {

	public static volatile SingularAttribute<Prediction, Boolean> defaultFlag;
	public static volatile SingularAttribute<Prediction, String> reasons;
	public static volatile SingularAttribute<Prediction, String> errorDesc;
	public static volatile SingularAttribute<Prediction, Float> outputValue;
	public static volatile SingularAttribute<Prediction, Boolean> errorFlag;
	public static volatile SingularAttribute<Prediction, Boolean> feedbackFlag;
	public static volatile SingularAttribute<Prediction, Long> queryId;
	public static volatile SingularAttribute<Prediction, String> feedbackClass;
	public static volatile SingularAttribute<Prediction, Float> feedbackValue;
	public static volatile SingularAttribute<Prediction, String> input;
	public static volatile SingularAttribute<Prediction, String> errorCause;
	public static volatile SingularAttribute<Prediction, String> feedbackUser;
	public static volatile SingularAttribute<Prediction, Instant> feedbackTimestamp;
	public static volatile SingularAttribute<Prediction, String> outputClass;
	public static volatile SingularAttribute<Prediction, Model> model;
	public static volatile SingularAttribute<Prediction, Long> id;
	public static volatile SingularAttribute<Prediction, Instant> timestamp;
	public static volatile SingularAttribute<Prediction, String> defaultDesc;

}

