package com.graphql.fedration.fedration_book.loader;

import com.graphql.fedration.fedration_book.model.Author;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AuthorDataLoaderRegistry {

    public AuthorDataLoaderRegistry(BatchLoaderRegistry registry, AuthorDataLoader loader) {
        registry.forTypePair(Long.class, Author.class)
                .registerBatchLoader((ids, env) ->
                        Mono.fromFuture(loader.loadAuthors(ids))
                                .flatMapMany(Flux::fromIterable)
                );
    }
}
