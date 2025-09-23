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
            Class<?> controllerClass
    ) {
        Long id = idExtractor.apply(item);

        return EntityModel.of(item,
                linkTo(methodOn(controllerClass).getClass()).slash(id).withSelfRel(),
                linkTo(methodOn(controllerClass).getClass()).withRel("all")
        );
    }

    public static <T> CollectionModel<EntityModel<T>> toCollectionModel(
            List<T> list,
            Function<T, Long> idExtractor,
            Class<?> controllerClass
    ) {
        List<EntityModel<T>> models = list.stream()
                .map(item -> toModel(item, idExtractor, controllerClass))
                .toList();

        return CollectionModel.of(models,
                linkTo(methodOn(controllerClass).getClass()).withSelfRel()
        );
    }
}
