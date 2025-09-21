package com.lopezcampos.service.interface_;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.exception.base.NotFoundException;

public abstract class AbstractCrudService<
        T,          // Entity
        ID,         // ID type
        REQ,        // Request DTO
        RES,        // Response DTO
        R extends JpaRepository<T, ID>
        > implements CrudService<REQ, RES, ID> {

    protected final R repository;
    private final Class<T> entityClass;
    private final Class<RES> responseClass;

    protected AbstractCrudService(R repository, Class<T> entityClass, Class<RES> responseClass) {
        this.repository = repository;
        this.entityClass = entityClass;
        this.responseClass = responseClass;
    }

    @Override
    public RES create(REQ requestDto) {
        T entity = ModelMapperConfig.map(requestDto, entityClass);
        T saved = repository.save(entity);
        return ModelMapperConfig.map(saved, responseClass);
    }

    @Override
    public RES getById(ID id) {
        T entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entity not found with id " + id));
        return ModelMapperConfig.map(entity, responseClass);
    }

    @Override
    public List<RES> getAll() {
        return ModelMapperConfig.mapList(repository.findAll(), responseClass);
    }

    @Override
    public RES update(ID id, REQ requestDto) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Entity not found with id " + id);
        }
        T entity = ModelMapperConfig.map(requestDto, entityClass);
        
        return ModelMapperConfig.map(repository.save(entity), responseClass);
    }

    @Override
    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Entity not found with id " + id);
        }
        repository.deleteById(id);
    }
}
