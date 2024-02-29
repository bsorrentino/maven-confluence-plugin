/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest.scrollversions;

import static java.util.Collections.emptyMap;

import org.bsc.confluence.ConfluenceService;
import org.bsc.ssl.SSLCertificateInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ScrollVersionsConfluenceServiceIntegrationTest {


    private static void debug(String message, Object...args ) {
        System.out.printf( message, (Object[])args );
        System.out.println();
    }
    private static void trace(String message, Object...args ) {
        System.out.printf( message, (Object[])args );
        System.out.println();
    }

    @BeforeAll
    public static void clearSpace() {
        final var ssl = new SSLCertificateInfo();

        final var service = new ScrollVersionsConfluenceService(
                "http://localhost:8090/rest/api",
                "alpha",
                new ConfluenceService.Credentials( "admin", "admin", emptyMap()),
                ssl);

        final var space = "SVTS";

        clearSpace( service, space).join();

    }

    private static CompletableFuture<Void> sleep( TimeUnit timeunit, long unit ) {
        final var result = new CompletableFuture<Void>();
        try {
            timeunit.sleep( unit);
            result.complete(null);
        }
        catch( InterruptedException ex ) {
            result.completeExceptionally(ex);
        }
        return result;

    }
    public static CompletableFuture<Void> clearSpace(final ScrollVersionsConfluenceService service, final String space ) {
        final var unversioned = service.getVersionsPages(space, "isUnversioned", "true")
                .thenAccept(p -> {
                    p.forEach(u -> {
                            debug("unversioned [%s]", u.getConfluencePageTitle());
                            service.delegate.removePage( ConfluenceService.Model.ID.of(u.getConfluencePageId()) )
                                    .thenCompose( r -> sleep( TimeUnit.SECONDS, 1))
                                    .join();
                    });
                });
        final var versioned = service.getVersionsPages(space, "pageType", "change")
                .thenAccept(p -> {
                    p.forEach(u -> {
                        debug("versioned [%s]", u.getConfluencePageTitle());
                        service.delegate.removePage(ConfluenceService.Model.ID.of(u.getConfluencePageId()))
                                .thenCompose( r -> sleep( TimeUnit.SECONDS, 1))
                                .join();

                    });
                });
        final var masters = service.getVersionsPages(space, "pageType", "masterPage")
                .thenAccept(p -> {
                    p.forEach(u -> {
                        debug("master [%s]", u.getConfluencePageTitle());
                        service.delegate.removePage(ConfluenceService.Model.ID.of(u.getConfluencePageId()))
                                .thenCompose( r -> sleep( TimeUnit.SECONDS, 1))
                                .join();
                    });
                });

        final var page = "Topic 1";

        CompletableFuture<Void> createFirstPage =
                service.getScrollVersions(space)
                        .thenCompose(versions ->
                            sleep( TimeUnit.SECONDS, 5 ).thenCompose( none ->
                                service.delegate.createPageByTitle(space, page, ConfluenceService.Storage.of( "", ConfluenceService.Storage.Representation.WIKI))
                                    .thenAccept(res ->
                                            versions.stream().forEach( v ->
                                                    service.manageVersionPage(res.getId(), page, v, ScrollVersionsConfluenceService.ChangeType.ADD_VERSION).join())
                                    )));


        return CompletableFuture.allOf(masters, versioned, unversioned)
                .thenCompose(v -> createFirstPage)
                ;
    }

    ScrollVersionsConfluenceService service;

    @BeforeEach
    public void createService() {
        final var ssl = new SSLCertificateInfo();

        final var service = new ScrollVersionsConfluenceService(
                "http://localhost:8090/rest/api",
                "alpha",
                new ConfluenceService.Credentials( "admin", "admin", emptyMap()),
                ssl);

        final var space = "SVTS";

    }
    @Test
    public void testGetPageInVersion() {


    }

}
