package com.predera.dmml.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.predera.dmml.domain.Model;
import com.predera.dmml.repository.ModelRepository;
import com.predera.dmml.service.ModelService;
import com.predera.dmml.service.dto.ModelSubset;


/**
 * Service Implementation for managing Model.
 */
@Service
@Transactional
public class ModelServiceImpl implements ModelService{

    private final Logger log = LoggerFactory.getLogger(ModelServiceImpl.class);

    private final ModelRepository modelRepository;

    public ModelServiceImpl(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    /**
     * Save a model.
     *
     * @param model the entity to save
     * @return the persisted entity
     */
    @Override
    public Model save(Model model) {
        log.debug("Request to save Model : {}", model);
        return modelRepository.save(model);
    }

    /**
     * Get all the models.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Model> findAll(Pageable pageable) {
        log.debug("Request to get all Models");
        return modelRepository.findAll(pageable);
    }

    /**
     * Get one model by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Model findOne(Long id) {
        log.debug("Request to get Model : {}", id);
        return modelRepository.findOne(id);
    }

    /**
     * Delete the model by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Model : {}", id);
        modelRepository.delete(id);
    }

    /**
     * Get the "Owner" model.
     *
     * @param owner the owner of the entity
     * returns the owner of the entity
     * @return 
     */
	@Override
	public Model findOneByOwner(Long id,String owner) {
		log.debug("Request to get Model : {}", owner);
        return modelRepository.findOneByIdAndOwner(id, owner);
	}

	@Override
	public Long countAllByOwner(String owner) {
		log.debug("Request to get count of models by owner : {}", owner);
		return modelRepository.countAllByOwner(owner);
	}

	@Override
	public List<ModelSubset> findAllByOwner(String owner) {
		log.debug("Request to get all models by owner : {}", owner);
		return modelRepository.findAllByOwner(owner);
	}
}
