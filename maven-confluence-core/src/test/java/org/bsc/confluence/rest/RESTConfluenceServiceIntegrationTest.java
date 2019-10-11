/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author softphone
 */
public class RESTConfluenceServiceIntegrationTest extends AbstractRestConfluence {

    RESTConfluenceServiceImpl restConfluenceService = new RESTConfluenceServiceImpl(confluenceUrl, credentials, sslInfo);

    @Test @Ignore
    public void dummy() {}

    @Before
    public void initService() throws Exception {
        service = restConfluenceService;
    }

}
