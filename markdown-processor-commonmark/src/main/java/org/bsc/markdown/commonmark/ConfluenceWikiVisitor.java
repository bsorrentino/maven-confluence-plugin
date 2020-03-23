package org.bsc.markdown.commonmark;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;

public class ConfluenceWikiVisitor extends AbstractVisitor {

    private StringBuilder _buffer = new StringBuilder( 500 * 1024 );

    @Override
    public void visit(Heading heading) {

        _buffer.append( String.format( "\n\nh%s.", heading.getLevel()) );
        visitChildren(heading);
        _buffer.append("\n\n");

    }

    @Override
    public String toString() {
        return _buffer.toString()
    }
}
