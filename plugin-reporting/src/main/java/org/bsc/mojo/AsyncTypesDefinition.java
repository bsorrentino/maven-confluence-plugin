package org.bsc.mojo;

import org.bsc.confluence.ConfluenceService;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public interface AsyncTypesDefinition {


    /**
     * Type Shortcut
     */
    interface AsyncFunc<T,R> extends Function<T, CompletableFuture<R>> {}

    interface AsyncPageFunc<T> extends AsyncFunc<T, ConfluenceService.Model.Page> {}

    interface AsyncProcessPageFunc extends AsyncPageFunc<ConfluenceService.Model.Page> {}

    interface AsyncSupplier<T> extends Supplier<CompletableFuture<T>> {}

    interface AsyncPageSupplier extends AsyncSupplier<ConfluenceService.Model.Page> {}

}
