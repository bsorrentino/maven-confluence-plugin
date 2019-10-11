/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest.scrollversions;

import static java.lang.String.format;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.bsc.confluence.rest.AbstractRestConfluence;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import lombok.val;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScrollVersionsRESTConfluenceServiceIntegrationTest extends AbstractRestConfluence {

    //private static final String SCROLL_VERSIONS_URL = "http://localhost:8090/rest/scroll-versions/1.0";
    enum Version {
        ALPHA, BETA, RC1
    }

    @Before
    public void initService() throws Exception {
        
        service = new ScrollVersionsRESTConfluenceService(getConfluenceUrl(), 
                                                            Version.BETA.name(), 
                                                            credentials, 
                                                            sslInfo );
        setSpaceKey("TESTSV");
        setgetParentPageTitle("TESTSV Home");

    }
    
    @Test
    public void test090_deletePages() throws Exception{
       service.getPage(getSpaceKey(), getParentPageTitle())
           .thenApply( p -> {
               assertThat( format("page [%s] not found", getParentPageTitle()), p.isPresent(), is(true) );
               return p.get();               
           })
           .thenAccept( page -> {
                
                try {
                    val descents = service.getDescendents(page.getId());
                    descents.stream()
                        .forEach( pp -> { 
                            System.out.printf( "deleting page [%s]\n", pp.getTitle());
                            service.removePageAsync( pp.getId() ).join();
                        });
                } catch (Exception e) {
                    fail( e.getMessage() );
                }
                              
           }).get()
       ;
    }

}
