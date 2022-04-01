package org.bsc.markdown.commonmark;

import lombok.NonNull;
import org.bsc.markdown.MarkdownParserContext;
import org.bsc.markdown.MarkdownVisitorHelper;
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
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.bsc.markdown.MarkdownVisitorHelper.*;

/**
 * CommonmarkConfluenceWikiVisitor
 *
 * class that implements the Commonmark Visitor for translating to Confluence Wiki
 *
 */
public class CommonmarkConfluenceWikiVisitor /*extends AbstractVisitor*/ implements Visitor  {

    public static class Parser  {

        private final List<Extension> extensions = Arrays.asList(StrikethroughExtension.create(), TablesExtension.create(), NoticeBlockExtension.create());

        private final org.commonmark.parser.Parser parser = org.commonmark.parser.Parser.builder().extensions(extensions).build();

        public final Node parse( String content ) {
            return parser.parse(content);
        }

    }

    public static Parser parser() {
        return new Parser();
    }

    private Stack<StringBuilder> bufferStack = new Stack<>();

    private final MarkdownParserContext parseContext;

/*
    @Override
    public void visit(Document document) {
        processChildren(document).preAndPost("<<DOC>>\n").process().nl();
    }
*/

    public CommonmarkConfluenceWikiVisitor(MarkdownParserContext parseContext ) {
        this.parseContext = parseContext;
        bufferStack.push(  new StringBuilder( 500 * 1024 ) ) ;
    }

    private StringBuilder buffer() {
        return bufferStack.peek();
    }

    private boolean isParentRoot(Node node) {
        return (node.getParent() instanceof Document);
    }

    /**
     *
     * @param node
     * @param text
     * @return
     */
    public static String escapeMarkdownText( Node node, String text ) {
        if( node!=null && node.getParent() instanceof TableCell) return text;
        return MarkdownVisitorHelper.escapeMarkdownText(text);
    }

    private final static Pattern isHTMLCommentPattern = Pattern.compile( "^([\\s]*)<!--(?:[\\s]*)(.+)-->$", Pattern.DOTALL );

    /**
     *
     * @param text
     * @return
     */
    public static Matcher parseHTMLComment( @NonNull String text ) {
        return isHTMLCommentPattern.matcher(text);
    }

    /**
     * Visit document node
     *
     * @param node
     */
    @Override
    public void visit(Document node) {
        processChildren(node).process();
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

        final ChildrenProcessor p = processChildren(node);

        if( isParentRoot(node) ) {
            p.post("\n");
        }
        else if( node.getParent() instanceof ListBlock ) {
            p.pre("\n");
        }

        p.process().nl();
    }

    /**
     * A line break (not in a code span or HTML tag) that is preceded by two or more spaces and does not occur at the end of a block is parsed as a hard line break
     * (rendered in HTML as a <br /> tag):
     *
     * @param node
     */
    @Override
    public void visit(HardLineBreak node) {
        processChildren(node).pre("\\\\").process().nl();
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
                .process().nl();
    }

    @Override
    public void visit(Text node) {
        final String literal = escapeMarkdownText( node, node.getLiteral() );
        processChildren(node)
                .pre( literal )
                .process();
    }

    @Override
    public void visit(Heading node) {
        processChildren(node)
                .pre( format( "h%s. ", node.getLevel()) )
                .process().nl();
    }

    @Override
    public void visit(ListItem node) {
        processChildren(node).process();
    }

    @Override
    public void visit(OrderedList node) {
        final Node parent = node.getParent();

        if( parent instanceof ListItem ) { // IS SUBLIST
            processChildren(node)
                    .map(n -> (parent.getParent() instanceof OrderedList) ? "## " : "*# ")
                    .process();
        }
        else {
            processChildren(node).map( n -> "# " )
                    .process()
                    .nl();
        }
    }

    @Override
    public void visit(BulletList node) {
        final Node parent = node.getParent();

        if( parent instanceof ListItem ) { // IS SUBLIST
            processChildren(node)
                    .map( n -> (parent.getParent() instanceof OrderedList)  ? "#* " : "** " )
                    .process();
        }
        else {
            processChildren(node)
                    .map(n -> "* ")
                    .process()
                    .nl();
        }
    }

    @Override
    public void visit(BlockQuote node) {

        processChildren(node)
                .pre("{quote}\n")
                .post("{quote}\n")
                .process()
                .nl( isParentRoot(node) );
    }

    @Override
    public void visit(Code node) {
        final String literal = escapeMarkdownText( node, node.getLiteral());

        processChildren(node)
                .pre( "{{%s}}", literal )
                .process();
    }

    @Override
    public void visit(FencedCodeBlock node) {
        final Function<String,String> info = (v) -> (v==null || v.length()==0 ) ? "" : ":"+v ;

        processChildren(node)
                .pre( "{code%s}\n%s", info.apply(node.getInfo()), node.getLiteral() )
                .post("{code}")
                .process().nl();
    }

    @Override
    public void visit(Emphasis node) {
        processChildren(node).pre("_").post("_").process();

    }

    @Override
    public void visit(StrongEmphasis node) {
        processChildren(node).pre("*").post("*").process();;
    }

    @Override
    public void visit(Image node) {

        final String destination = processImageUrl(node.getDestination(), parseContext);

        processChildren(node)
                //.pre( format("<<IMG destination=[%s] title=[%s]>>", node.getDestination(), node.getTitle())).post("<</IMG>>")
                .pre( "!%s|", destination, node.getTitle() )
                .post("!")
                .process()
                .nl( isParentRoot(node) );
    }

    @Override
    public void visit(LinkReferenceDefinition node) {
        processChildren(node)
                //.pre("<<LNKR>>").post( "<</LNKR>>")
                .process().nl();
    }

    @Override
    public void visit(Link node) {

        final String destination = processLinkUrl(node.getDestination(), parseContext);

        processChildren(node)
                //.preAndPost("<<LNK>>")
                .pre( "[" )
                .captureOutput( v -> buffer().append(v) ) // ignore text
                .post("|%s%s]", destination, ofNullable(node.getTitle()).map( v -> "|"+v ).orElse(""))
                .process()
                .nl(isParentRoot(node));
    }

    @Override
    public void visit(ThematicBreak node) {
        processChildren(node).pre("----").process().nl();
    }

    //@Custom
    public void visit( TableBlock node ) {
        processChildren(node).process();
    }

    //@Custom
    public void visit( TableHead node ) {
        processChildren(node).process();
    }

    //@Custom
    public void visit( TableBody node ) {
        processChildren(node)
                .process();
    }

    //@Custom
    public void visit( TableRow node ) {
        processChildren(node)
                .pre("|")
                .post( () -> ( node.getParent() instanceof TableHead ) ? "|" : "" )
                .process()
                .nl();
    }

    //@Custom
    public void visit( TableCell node ) {
        processChildren(node)
                .captureOutput( ( value ) -> {
                    // System.out.printf("\n\n===> TableCell [%s]\n\n", value);
                    buffer().append( value.isEmpty() ? ' ' : value );
                })
                .pre( () -> ( node.isHeader()) ? "|" : "" )
                .post( "|" )
                .process();
    }

    @Override
    public void visit(HtmlBlock node) {
        final String literal = node.getLiteral();

        final Matcher m = parseHTMLComment(literal);
        if( m.matches() && isConfluenceMacro( m.group(2) ) ) {
            processChildren(node)
                    .pre(m.group(1))
                    .post(m.group(2))
                    .process().nl();
        }
        else {
            processChildren(node)
                    .pre("{html}\n%s\n", literal)
                    .post("{html}")
                    .process().nl();
        }
    }

    @Override
    public void visit(HtmlInline node) {
        processChildren(node)
                //.pre("<<HTMI>>").post("<</HTMI>>")
                .process().nl();
    }

    //@Custom
    public void visit( Strikethrough node ) {
        processChildren(node).pre("-").post("-").process();
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

        p.post("{%s}\n", type)
                .process()
                .nl(isParentRoot(node));
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

        processChildren(node).pre("<<CSTN type=\"%s\">>", node.getClass().getSimpleName()).post("<</CSTN>>").process().nl();
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
        processChildren(node).pre("<<CSTB type=\"%s\">>", node.getClass().getSimpleName()).post("<</CSTB>>").process().nl();
    }

    @Override
    public void visit(IndentedCodeBlock node) {
        processChildren(node).pre("<<ICB>>").post("<</ICB>>").process().nl();
    }

    @Override
    public String toString() {
        return buffer().toString();
    }


    class ChildrenProcessor<T extends Node> {
        private final boolean debug;

        Optional<Supplier<String>> pre = Optional.empty();
        Optional<Supplier<String>> post = Optional.empty();
//        Optional<Consumer<Node>> forEach = Optional.empty();;
        Optional<Function<Node,String>> map = Optional.empty();;
        Optional<Consumer<String>> captureOutput = Optional.empty();;

        final T parent;

        public ChildrenProcessor(T parent, boolean debug ) {
            this.parent = parent;
            this.debug = debug;
        }
        public ChildrenProcessor(T parent) {
            this( parent, false);
        }

        ChildrenProcessor<T> captureOutput(Consumer<String> v) {
            captureOutput = ofNullable(v);
            return this;
        }

        ChildrenProcessor<T> pre(String v, Object ...args) {
            pre = of( () -> format( v, args));
            return this;
        }

        ChildrenProcessor<T> pre( Supplier<String> supplier) {
            pre = ofNullable( supplier );
            return this;
        }

        ChildrenProcessor<T> post(String v, Object ...args) {
            post = of( () -> format( v, args));
            return this;
        }

        ChildrenProcessor<T> post(Supplier<String> supplier) {
            post = ofNullable( supplier );
            return this;
        }

//        ChildrenProcessor forEach(Consumer<Node> v ) {
//            forEach = ofNullable(v);
//            return this;
//        }
        <A extends Node> ChildrenProcessor<T> map(Function<Node,String> v ) {
            map = ofNullable(v);
            return this;
        }

        ChildrenProcessor<T> nl() {
            buffer().append('\n');
            return this;
        }

        ChildrenProcessor<T> nl( boolean condition ) {
            if( condition ) buffer().append('\n');
            return this;
        }

        ChildrenProcessor<T> process() {

            if(debug) buffer().append( format( "<%s>", parent.getClass().getSimpleName()) );
            pre.ifPresent( v -> buffer().append(v.get()) );

            captureOutput.ifPresent(
                    consumer -> bufferStack.push( new StringBuilder() ) );

            Node next;
            for(Node node = parent.getFirstChild(); node != null; node = next) {
                next = node.getNext();
                if( map.isPresent() ) {
                    buffer().append( map.get().apply( node ) );
                }
                node.accept(CommonmarkConfluenceWikiVisitor.this);
            }

            captureOutput.ifPresent( consumer ->
                    consumer.accept(bufferStack.pop().toString()) );

            post.ifPresent( v -> buffer().append(v.get()) );
            if( debug ) buffer().append( format( "</%s>", parent.getClass().getSimpleName()) );

            return this;

        }
    }

    protected <T extends Node> ChildrenProcessor<T> processChildren(T parent ) {
        return new ChildrenProcessor<T>( parent, false );
    }
}
