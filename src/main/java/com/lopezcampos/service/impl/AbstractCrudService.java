package com.lopezcampos.service.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.exception.base.NotFoundException;
import com.lopezcampos.service.interface_.CrudService;

public abstract class AbstractCrudService<
        T,
        ID,
        D,
        R extends JpaRepository<T, ID>
        > implements CrudService<D, ID>{

    protected final R repository;
    private final Class<T> entityClass;
    private final Class<D> dtoClass;

    protected AbstractCrudService(R repository, Class<T> entityClass, Class<D> dtoClass) {
        this.repository = repository;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    public D create(D dto) {
        T entity = ModelMapperConfig.map(dto, entityClass);
        return ModelMapperConfig.map(repository.save(entity), dtoClass);
    }

    public D getById(ID id) {
        T entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entity not found with id " + id));
        return ModelMapperConfig.map(entity, dtoClass);
    }

    public List<D> getAll() {
        return ModelMapperConfig.mapList(repository.findAll(), dtoClass);
    }

    public D update(ID id, D dto) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Entity not found with id " + id);
        }
        T entity = ModelMapperConfig.map(dto, entityClass);
        return ModelMapperConfig.map(repository.save(entity), dtoClass);
    }

    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Entity not found with id " + id);
        }
        repository.deleteById(id);
    }
}
