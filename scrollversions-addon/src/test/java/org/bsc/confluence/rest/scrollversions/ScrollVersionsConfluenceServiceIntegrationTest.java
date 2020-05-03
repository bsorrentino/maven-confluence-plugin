/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest.scrollversions;

import lombok.val;
import org.bsc.confluence.ConfluenceService;
import org.bsc.ssl.SSLCertificateInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScrollVersionsConfluenceServiceIntegrationTest {


    private static void debug(String message, Object...args ) {
        System.out.printf( message, (Object[])args );
        System.out.println();
    }
    private static void trace(String message, Object...args ) {
        System.out.printf( message, (Object[])args );
        System.out.println();
    }

    @BeforeClass
    public static void clearSpace() {
        val ssl = new SSLCertificateInfo();

        val service = new ScrollVersionsConfluenceService(
                "http://localhost:8090/rest/api",
                "alpha",
                new ConfluenceService.Credentials( "admin", "admin"),
                ssl);

        val space = "SVTS";

        clearSpace( service, space).join();

    }

    private static CompletableFuture<Void> sleep( TimeUnit timeunit, long unit ) {
        val result = new CompletableFuture<Void>();
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
        val unversioned = service.getVersionsPages(space, "isUnversioned", "true")
                .thenAccept(p -> {
                    p.forEach(u -> {
                            debug("unversioned [%s]", u.getConfluencePageTitle());
                            service.delegate.removePage(String.valueOf(u.getConfluencePageId()))
                                    .thenCompose( r -> sleep( TimeUnit.SECONDS, 1))
                                    .join();
                    });
                });
        val versioned = service.getVersionsPages(space, "pageType", "change")
                .thenAccept(p -> {
                    p.forEach(u -> {
                        debug("versioned [%s]", u.getConfluencePageTitle());
                        service.delegate.removePage(String.valueOf(u.getConfluencePageId()))
                                .thenCompose( r -> sleep( TimeUnit.SECONDS, 1))
                                .join();

                    });
                });
        val masters = service.getVersionsPages(space, "pageType", "masterPage")
                .thenAccept(p -> {
                    p.forEach(u -> {
                        debug("master [%s]", u.getConfluencePageTitle());
                        service.delegate.removePage(String.valueOf(u.getConfluencePageId()))
                                .thenCompose( r -> sleep( TimeUnit.SECONDS, 1))
                                .join();
                    });
                });

        val page = "Topic 1";


        CompletableFuture<Void> createFirstPage =
                service.getScrollVersions(space)
                        .thenCompose(versions ->
                            sleep( TimeUnit.SECONDS, 5 ).thenCompose( none ->
                                service.delegate.createPageByTitle(space, page)
                                    .thenAccept(res ->
                                            versions.stream().forEach( v ->
                                                    service.manageVersionPage(Long.valueOf(res.getId()), page, v, ScrollVersionsConfluenceService.ChangeType.ADD_VERSION).join())
                                    )));


        return CompletableFuture.allOf(masters, versioned, unversioned)
                .thenCompose(v -> createFirstPage)
                ;
    }

    ScrollVersionsConfluenceService service;

    @Before
    public void createService() {
        val ssl = new SSLCertificateInfo();

        val service = new ScrollVersionsConfluenceService(
                "http://localhost:8090/rest/api",
                "alpha",
                new ConfluenceService.Credentials( "admin", "admin"),
                ssl);

        val space = "SVTS";

    }
    @Test
    public void testGetPageInVersion() {


    }

}
