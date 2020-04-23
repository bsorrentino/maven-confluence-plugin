package org.bsc.confluence.rest;

import org.junit.Test;

public class Issue213Test {

    @Test(expected = java.lang.NumberFormatException.class )
    public void bigIdWithInt() {

        String pageId = "2182288832";

        int result = Integer.valueOf( pageId );


    }

    @Test
    public void bigIdWithLong() {

        String pageId = "2182288832";

        long result = Long.valueOf( pageId );


    }
}
