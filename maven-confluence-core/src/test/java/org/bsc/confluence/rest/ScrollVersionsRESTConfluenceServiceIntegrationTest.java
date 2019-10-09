/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.confluence.rest;

import org.bsc.confluence.rest.scrollversions.ScrollVersionsRESTConfluenceService;
import org.junit.Before;

public class ScrollVersionsRESTConfluenceServiceIntegrationTest extends AbstractRestConfluence {

    private static final String SCROLL_VERSIONS_URL = "http://localhost:8090/rest/scroll-versions/1.0";
    private static final String SCROLL_VERSIONS_VERSION = "ALPHA";
    private static final String SCROLL_VERSIONS_SPACE_KEY = "TESTSV";

    private ScrollVersionsRESTConfluenceService scrollVersionsRESTConfluenceService = new ScrollVersionsRESTConfluenceService(URL, SCROLL_VERSIONS_URL, credentials, sslInfo, SCROLL_VERSIONS_VERSION);

    @Before
    public void initService() throws Exception {
        service = scrollVersionsRESTConfluenceService;
        SPACE_KEY = SCROLL_VERSIONS_SPACE_KEY;
    }

}
