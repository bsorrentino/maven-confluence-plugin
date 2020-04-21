package org.bsc.markdown.commonmark;

import org.bsc.markdown.commonmark.extension.NoticeBlock;
import org.bsc.markdown.commonmark.extension.NoticeBlockExtension;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.Strikethrough;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.*;
import org.commonmark.node.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class ConfluenceWikiVisitor extends AbstractVisitor {

    public static class Parser {

        private final ConfluenceWikiVisitor visitor = new ConfluenceWikiVisitor();

        private final List<Extension> extensions = Arrays.asList(StrikethroughExtension.create(), TablesExtension.create(), NoticeBlockExtension.create());

        private final org.commonmark.parser.Parser parser = org.commonmark.parser.Parser.builder().extensions(extensions).build();

        public final String parse( String content ) {
            final Node node = parser.parse(content);
            node.accept(visitor);
            return visitor.toString();
        }

    }

    public static Parser parser() {
        return new Parser();
    }

    private Stack<StringBuilder> bufferStack = new Stack<>();

/*
    @Override
    public void visit(Document document) {
        processChildren(document).preAndPost("<<DOC>>\n").process();
    }
*/

    ConfluenceWikiVisitor() {
        bufferStack.push(  new StringBuilder( 500 * 1024 ) ) ;
    }

    private StringBuilder buffer() {
        return bufferStack.peek();
    }

    private boolean isRoot( Block node ) {
        return (node.getParent() instanceof Document);
    }

    /**
     * A sequence of non-blank lines that cannot be interpreted as other kinds of blocks forms a paragraph.
     * The contents of the paragraph are the result of parsing the paragraph’s raw content as inlines.
     * The paragraph’s raw content is formed by concatenating the lines and removing initial and final whitespace.
     *
     * @param node
     */
    @Override
    public void visit(Paragraph node) {
        final Node parent = node.getParent();

        final ChildrenProcessor p =
                    processChildren(node)
                    //.pre("<<PRGH>>")
                    ;
        if( parent instanceof Document ) {
            p.post("\n");
        }

        p
        //.post( "<</PRGH>>")
        .process();
    }

    /**
     * A line break (not in a code span or HTML tag) that is preceded by two or more spaces and does not occur at the end of a block is parsed as a hard line break
     * (rendered in HTML as a <br /> tag):
     *
     * @param node
     */
    @Override
    public void visit(HardLineBreak node) {
        processChildren(node).pre("\\").process();
    }

    /**
     * A regular line break (not in a code span or HTML tag) that is not preceded by two or more spaces or a backslash is parsed as a softbreak.
     * (A softbreak may be rendered in HTML either as a line ending or as a space. The result will be the same in browsers. In the examples here, a line ending will be used.)
     *
     * @param node
     */
    @Override
    public void visit(SoftLineBreak node) {
        processChildren(node)
                //.pre("<<SLB>>").post( "<</SLB>>")
                .process(false);
    }

    @Override
    public void visit(Text node) {
        final Node parent = node.getParent();

        if( parent instanceof Paragraph ) {
            if( parent.getParent() instanceof ListItem ) {
                buffer().append( " " );
            }
        }
        buffer().append( node.getLiteral() );
        visitChildren(node);
    }

    @Override
    public void visit(Heading node) {

        processChildren(node)
                .pre( format( "h%s. ", node.getLevel()) )
                .process();

    }

    @Override
    public void visit(OrderedList node) {
        final Node parent = node.getParent();

        final Function<Node,String> f ;
        if( parent instanceof ListItem ) {
            f =  ( parent.getParent() instanceof OrderedList )  ? n -> "## " : n -> "*# ";
        }
        else {
            f = n -> "# ";
        }
        processChildren(node).map(f).process(false);;
    }

    @Override
    public void visit(BlockQuote node) {

        processChildren(node)
                .pre("{quote}\n")
                .post("{quote}\n")
                .process( isRoot(node) );
    }

    @Override
    public void visit(BulletList node) {
        final Node parent = node.getParent();

        final Function<Node,String> f ;
        if( parent instanceof ListItem ) {
            f =  ( parent.getParent() instanceof OrderedList )  ? n -> "#* " : n -> "** ";
        }
        else {
            f = n -> "* ";
        }
        processChildren(node).map(f).process(false);;
    }

    @Override
    public void visit(Code node) {
        processChildren(node)
                .pre( "{{%s}}", node.getLiteral() )
                .process(false);
    }

    @Override
    public void visit(FencedCodeBlock node) {
        final Function<String,String> info = (v) -> (v==null || v.length()==0 ) ? "" : ":"+v ;

        processChildren(node)
                .pre( "{code%s}\n%s", info.apply(node.getInfo()), node.getLiteral() )
                .post("{code}")
                .process();
    }

    @Override
    public void visit(Emphasis node) {
        processChildren(node).pre("_").post("_").process( false);

    }


    @Override
    public void visit(ListItem node) {
        processChildren(node).process(false);
    }

    @Override
    public void visit(StrongEmphasis node) {
        processChildren(node).pre("*").post("*").process(false);;
    }

    @Override
    public void visit(Image node) {
        processChildren(node)
                //.pre( format("<<IMG destination=[%s] title=[%s]>>", node.getDestination(), node.getTitle())).post("<</IMG>>")
                .pre( "!%s", node.getDestination() )
                .captureOutput( v -> {} ) // ignore text
                .post("!")
                .process( false);
    }

    @Override
    public void visit(LinkReferenceDefinition node) {
        processChildren(node)
                //.preAndPost("<<LNKR>>")
                .process();
    }

    @Override
    public void visit(Link node) {
        processChildren(node)
                //.preAndPost("<<LNK>>")
                .pre( "[" )
                .captureOutput( v -> buffer().append(v) ) // ignore text
                .post("|%s%s]", node.getDestination(), ofNullable(node.getTitle()).map( v -> "|"+v ).orElse(""))
                .process(false);
    }

    @Override
    public void visit(ThematicBreak node) {
        processChildren(node).pre("---").process();
    }

    //@Custom
    public void visit( TableBlock node ) {
        processChildren(node).process(false);
    }

    //@Custom
    public void visit( TableHead node ) {
        processChildren(node).process(false);
    }

    //@Custom
    public void visit( TableBody node ) {
        processChildren(node).process(false);
    }

    //@Custom
    public void visit( TableRow node ) {
        final ChildrenProcessor p = processChildren(node).pre("|");

        if( node.getParent() instanceof  TableHead ) {
            p.post( "|" );
        }
        p.process();
    }

    //@Custom
    public void visit( TableCell node ) {
        final ChildrenProcessor p = processChildren(node);

        if( node.isHeader()) {
            p.pre( "|");
        }
        p.post("|").process(false);
    }

    @Override
    public void visit(HtmlBlock node) {
        processChildren(node).pre("{html}\n%s\n", node.getLiteral()).post("{html}").process();
    }

    //@Custom
    public void visit( Strikethrough node ) {
        processChildren(node).pre("-").post("-").process(false);
    }

    //@custom
    public void visit(NoticeBlock node) {
        final String type = node.getType().getTagName();
        final ChildrenProcessor p = processChildren(node);

        if( node.getTitle().isPresent() ) {
            p.pre( "{%s:title=%s}\n", type, node.getTitle().get());
        }
        else {
            p.pre("{%s}\n", type);
        }

        p.post("{%s}\n", type).process(isRoot(node));
    }

    @Override
    public void visit(CustomNode node) {

        if( node instanceof Strikethrough ) {
            visit( (Strikethrough)node );
            return;
        }
        else if( node instanceof TableCell ) {
            visit( (TableCell)node );
            return;
        }
        else if( node instanceof TableRow ) {
            visit( (TableRow)node );
            return;
        }
        else if( node instanceof TableHead ) {
            visit( (TableHead)node );
            return;
        }
        else if( node instanceof TableBody ) {
            visit( (TableBody)node );
            return;
        }

        processChildren(node).pre("<<CSTN type=\"%s\">>", node.getClass().getSimpleName()).post("<</CSTN>>").process();
    }

    @Override
    public void visit(CustomBlock node) {

        if( node instanceof TableBlock ) {
            visit( (TableBlock)node );
            return;
        }
        else if( node instanceof NoticeBlock) {
            visit( (NoticeBlock)node );
            return;
        }
        processChildren(node).pre("<<CSTB type=\"%s\">>", node.getClass().getSimpleName()).post("<</CSTB>>").process();
    }


    @Override
    public void visit(HtmlInline node) {
        processChildren(node).pre("<<HTMI>>").post("<</HTMI>>").process();
    }

    @Override
    public void visit(IndentedCodeBlock node) {
        processChildren(node).pre("<<ICB>>").post("<</ICB>>").process();
    }

    @Override
    public String toString() {
        return buffer().toString();
    }


    class ChildrenProcessor<T extends Node> {
        Optional<String> pre = Optional.empty();
        Optional<String> post = Optional.empty();
        Optional<Consumer<Node>> forEach = Optional.empty();;
        Optional<Function<Node,String>> map = Optional.empty();;
        Optional<Consumer<String>> captureOutput = Optional.empty();;

        final T parent;

        public ChildrenProcessor(T parent) {
            this.parent = parent;
        }

        ChildrenProcessor captureOutput(Consumer<String> v) {
            captureOutput = ofNullable(v);
            return this;
        }
        ChildrenProcessor pre(String v, Object ...args) {
            pre = ofNullable(format( v, (Object[])args));
            return this;
        }
        ChildrenProcessor post(String v, Object ...args) {
            post = ofNullable(format( v, (Object[])args));
            return this;
        }
        ChildrenProcessor forEach(Consumer<Node> v ) {
            forEach = ofNullable(v);
            return this;
        }
        <A extends Node> ChildrenProcessor map(Function<Node,String> v ) {
            map = ofNullable(v);
            return this;
        }

        void process( ) { process( true );  }

        void process( boolean writeln  ) {

            pre.ifPresent( v -> buffer().append(v) );

            captureOutput.ifPresent( consumer -> bufferStack.push( new StringBuilder() ) );

            Node next;
            for(Node node = parent.getFirstChild(); node != null; node = next) {
                next = node.getNext();
                if( forEach.isPresent() ) {
                   forEach.get().accept(node);
                }
                else {
                    if( map.isPresent() ) {
                        buffer().append( map.get().apply( node ) );
                    }
                    node.accept(ConfluenceWikiVisitor.this);
                }
            }
            captureOutput.ifPresent( consumer -> {
                final String content = bufferStack.pop().toString();
                consumer.accept(content);
            });

            post.ifPresent( v -> buffer().append(v) );
            if( writeln ) buffer().append('\n');

        }
    }

    protected <T extends Node> ChildrenProcessor processChildren(T parent ) {
        return new ChildrenProcessor( parent );
    }
}
