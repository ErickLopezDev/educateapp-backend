package com.lopezcampos.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lopezcampos.service.interface_.CrudService;

public abstract class AbstractCrudService<T, ID, R extends JpaRepository<T, ID>> implements CrudService<T, ID> {

    protected final R repository;

    protected AbstractCrudService(R repository) {
        this.repository = repository;
    }

    @Override
    public T create(T entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<T> getById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        return Optional.ofNullable(repository.findById(id).orElseThrow(() -> 
            new RuntimeArgumentException("Entity with ID " + id + " not found")));
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public T update(ID id, T entity) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        if (!repository.existsById(id)) {
            throw new RuntimeException("Entity with ID " + id + " not found");
        }
        return repository.save(entity);
    }

    @Override
    public void delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        if (!repository.existsById(id)) {
            throw new RuntimeException("Entity with ID " + id + " not found");
        }
        repository.deleteById(id);
    }
}

//TODO: crear exceptions customizados
class RuntimeArgumentException extends RuntimeException {
    public RuntimeArgumentException(String message) {
        super(message);
    }
}