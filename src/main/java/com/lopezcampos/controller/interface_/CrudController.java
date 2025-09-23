package com.lopezcampos.controller.interface_;

import java.util.List;

public interface CrudController<T> {
  T getById(Long id);
  List<T> getAll();
  T create(T dto);
  T update(Long id, T dto);
  void delete(Long id);
}
