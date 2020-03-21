package org.bsc.markdown.flexmark;

import com.vladsch.flexmark.profile.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.data.DataHolder;

/**
 *
 */
public class ConfluenceWikiVisitor {

    final static DataHolder OPTIONS() {

        int EXT =   com.vladsch.flexmark.profile.pegdown.Extensions.NONE
                    | com.vladsch.flexmark.profile.pegdown.Extensions.FENCED_CODE_BLOCKS
                    | com.vladsch.flexmark.profile.pegdown.Extensions.TABLES
                    | com.vladsch.flexmark.profile.pegdown.Extensions.STRIKETHROUGH
                 // | Extensions.SMARTS; // Breaks link including a dash -
                    ;
        return PegdownOptionsAdapter.flexmarkOptions(true, EXT );
    }


}
