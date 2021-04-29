package org.bsc.mojo;

import org.bsc.confluence.ConfluenceService;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface TypesDefinition {


    /**
     * Type Shortcut
     */
    interface ProcessPageFunc extends Function<ConfluenceService.Model.Page, CompletableFuture<ConfluenceService.Model.Page>> {}

}
