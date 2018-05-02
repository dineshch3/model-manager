package com.predera.dmml.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.predera.dmml.domain.Model;
import com.predera.dmml.service.dto.ModelSubset;

/**
 * Service Interface for managing Model.
 */
public interface ModelService {

    /**
     * Save a model.
     *
     * @param model the entity to save
     * @return the persisted entity
     */
    Model save(Model model);

    /**
     * Get all the models.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Model> findAll(Pageable pageable);

    /**
     * Get the "id" model.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Model findOne(Long id);

    /**
     * Delete the "id" model.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    /**
     * Get the "Owner" model.
     *
     * @param id the id of the entity
     * @param owner the owner of the entity
     * @return the entity
     */
    Model findOneByOwner(Long id,String owner);
    
    /**
     * Get all the "Owner" model.
     *
     * @param owner the owner of the entity
     * @return entities with login owner
     */
    List<ModelSubset> findAllByOwner(String owner);
    
    Long countAllByOwner(String owner);
}
