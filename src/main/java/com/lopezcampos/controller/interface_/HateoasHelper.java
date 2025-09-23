package com.lopezcampos.controller.interface_;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class HateoasHelper {

    public static <T> EntityModel<T> toModel(
            T item,
            Function<T, Long> idExtractor,
            Class<?> controllerClass,
            String getByIdRel,
            String createRel,
            String updateRel,
            String deleteRel
    ) {
        Long id = idExtractor.apply(item);

        EntityModel<T> model = EntityModel.of(item,
                linkTo(methodOn(controllerClass).getClass()).slash(id).withRel(getByIdRel),
                linkTo(methodOn(controllerClass).getClass()).withRel("all")
        );

         // GET by ID
        if (getByIdRel != null) {
            model.add(linkTo(controllerClass).slash(id).withRel(getByIdRel));
        }

        // GET all 
        model.add(linkTo(controllerClass).withRel("all"));

        // POST / Create
        if (createRel != null) {
            model.add(linkTo(controllerClass).withRel(createRel));
        }

        // PUT / Update
        if (updateRel != null) {
            model.add(linkTo(controllerClass).slash(id).withRel(updateRel));
        }

        // DELETE
        if (deleteRel != null) {
            model.add(linkTo(controllerClass).slash(id).withRel(deleteRel));
        }

        return model;
    }

    public static <T> CollectionModel<EntityModel<T>> toCollectionModel(
            List<T> list,
            Function<T, Long> idExtractor,
            Class<?> controllerClass,
            String getByIdRel,
            String createRel,
            String updateRel,
            String deleteRel
    ) {
        List<EntityModel<T>> models = list.stream()
                .map(item -> toModel(item, idExtractor, controllerClass, getByIdRel, createRel, updateRel, deleteRel))
                .toList();

        return CollectionModel.of(models,
                linkTo(methodOn(controllerClass).getClass()).withSelfRel(),
                linkTo(methodOn(controllerClass).getClass()).withRel(createRel)
        );
    }
}
