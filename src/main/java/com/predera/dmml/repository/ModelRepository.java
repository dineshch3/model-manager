package com.predera.dmml.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.predera.dmml.domain.Model;
import com.predera.dmml.service.dto.ModelSubset;


/**
 * Spring Data JPA repository for the Model entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelRepository extends JpaRepository<Model, Long>, JpaSpecificationExecutor<Model> {

	Model findOneByIdAndOwner(Long id, String owner);
	
	List<ModelSubset> findAllByOwner(String owner);
	
	Long countAllByOwner(String owner);
}
