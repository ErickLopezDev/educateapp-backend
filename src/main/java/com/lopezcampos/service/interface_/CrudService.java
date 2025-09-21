package com.lopezcampos.service.interface_;

import java.util.List;

public interface CrudService<D, ID> {
    D create(D dto);
    D getById(ID id);
    List<D> getAll();
    D update(ID id, D dto);
    void delete(ID id);
}
