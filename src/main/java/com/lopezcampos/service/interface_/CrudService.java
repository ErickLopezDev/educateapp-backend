package com.lopezcampos.service.interface_;

import java.util.List;

public interface CrudService<REQ, RES, ID> {
    RES create(REQ requestDto);
    RES getById(ID id);
    List<RES> getAll();
    RES update(ID id, REQ requestDto);
    void delete(ID id);
}
