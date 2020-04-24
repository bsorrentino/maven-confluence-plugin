package org.bsc.markdown.commonmark.extension;

import org.commonmark.ext.gfm.tables.internal.TableTextContentNodeRenderer;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.text.TextContentNodeRendererContext;
import org.commonmark.renderer.text.TextContentNodeRendererFactory;
import org.commonmark.renderer.text.TextContentRenderer;

public class NoticeBlockExtension implements Parser.ParserExtension, TextContentRenderer.TextContentRendererExtension {

    @Override
    public void extend(Parser.Builder builder) {
        builder.customBlockParserFactory(new NoticeBlockParser.Factory());

    }

    @Override
    public void extend(TextContentRenderer.Builder builder) {
        builder.nodeRendererFactory(new TextContentNodeRendererFactory() {
            @Override
            public NodeRenderer create(TextContentNodeRendererContext context) {
                return new TableTextContentNodeRenderer(context);
            }
        });
    }

    public static NoticeBlockExtension create() {
        return new NoticeBlockExtension();
    }
}
