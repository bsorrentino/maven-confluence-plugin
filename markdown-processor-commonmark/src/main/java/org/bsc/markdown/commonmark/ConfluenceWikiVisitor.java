package org.bsc.markdown.commonmark;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.Strikethrough;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
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

        private final List<Extension> extensions = Arrays.asList(StrikethroughExtension.create());

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

    private StringBuilder _buffer = new StringBuilder( 500 * 1024 );

    private Stack<Node> callStack = new Stack<>();

/*
    @Override
    public void visit(Document document) {
        processChildren(document).preAndPost("<<DOC>>\n").process();
    }
*/

    @Override
    public void visit(Text node) {
        final Node parent = node.getParent();

        if( parent instanceof Paragraph ) {
            if( parent.getParent() instanceof ListItem ) {
                _buffer.append( " " );
            }
        }
        _buffer.append( node.getLiteral() );
        visitChildren(node);
    }

    @Override
    public void visit(Heading node) {

        processChildren(node)
                .pre( format( "\nh%s. ", node.getLevel()) )
                .process();

    }

    @Override
    public void visit(OrderedList node) {
        final Node parent = node.getParent();

        final Function<Node,String> f ;
        if( parent instanceof ListItem ) {
            f =  ( parent.getParent() instanceof OrderedList )  ? n -> "\n## " : n -> "\n*# ";
        }
        else {
            f = n -> "# ";
        }
        processChildren(node).map(f).process();;
    }

    @Override
    public void visit(BlockQuote blockQuote) {
        processChildren(blockQuote).preAndPost("{quote}").process();
    }

    @Override
    public void visit(BulletList node) {
        final Node parent = node.getParent();

        final Function<Node,String> f ;
        if( parent instanceof ListItem ) {
            f =  ( parent.getParent() instanceof OrderedList )  ? n -> "\n#* " : n -> "\n** ";
        }
        else {
            f = n -> "* ";
        }
        processChildren(node).map(f).process();;
    }

    @Override
    public void visit(Code node) {
        processChildren(node)
                .pre( format("{code[%s]}", node.getLiteral() ) )
                .post( "{code}")
                .process();
    }

    @Override
    public void visit(Emphasis node) {
        processChildren(node).preAndPost("_").process( false);

    }

    @Override
    public void visit(FencedCodeBlock node) {
        processChildren(node)
                .pre( format("{code [%s] [%s]}",node.getLiteral(), node.getInfo() ) ).process();
    }

    @Override
    public void visit(Paragraph node) {
        final Node parent = node.getParent();

        if( parent instanceof ListItem ) {
            processChildren(node).process(true);
            return;
        }
        processChildren(node).preAndPost("\n").process();
    }

    @Override
    public void visit(ListItem node) {
        processChildren(node).process(false);
    }

    @Override
    public void visit(StrongEmphasis node) {
        processChildren(node).preAndPost("*").process(false);;
    }

    @Override
    public void visit(CustomNode node) {

        if( node instanceof Strikethrough ) {
            processChildren(node).preAndPost("-").process(false);
            return;
        }

        processChildren(node).preAndPost("<<CSTN>>").process();
    }


    @Override
    public void visit(HardLineBreak node) {
        processChildren(node).preAndPost("<<HLB>>").process();
    }

    @Override
    public void visit(SoftLineBreak node) {
        processChildren(node).preAndPost("<<SLB>>").process();
    }

    @Override
    public void visit(ThematicBreak thematicBreak) {
        processChildren(thematicBreak).pre("<<THB>>").process();
    }

    @Override
    public void visit(HtmlInline node) {
        processChildren(node).preAndPost("<<HTMI>>").process();
    }

    @Override
    public void visit(HtmlBlock node) {
        processChildren(node).preAndPost("<<HTMB>>").process();
    }

    @Override
    public void visit(Image node) {
        processChildren(node).preAndPost("<<IMG>>").process();
    }

    @Override
    public void visit(IndentedCodeBlock node) {
        processChildren(node).pre("<<ICB>>").process();
    }

    @Override
    public void visit(Link node) {
        processChildren(node).preAndPost("<<LNK>>").process();
    }

    @Override
    public void visit(LinkReferenceDefinition node) {
        processChildren(node).preAndPost("<<LNKR>>").process();
    }

    @Override
    public void visit(CustomBlock node) {
        processChildren(node).preAndPost("<<CSTB>>").process();
    }

    @Override
    public String toString() {
        return _buffer.toString();
    }


    class ChildrenProcessor<T extends Node> {
        Optional<String> pre = Optional.empty();
        Optional<String> post = Optional.empty();
        Optional<Consumer<Node>> forEach = Optional.empty();;
        Optional<Function<Node,String>> map = Optional.empty();;

        final T parent;

        public ChildrenProcessor(T parent) {
            this.parent = parent;
        }

        ChildrenProcessor pre(String v) {
            pre = ofNullable(v);
            return this;
        }
        ChildrenProcessor post(String v) {
            post = ofNullable(v);
            return this;
        }
        ChildrenProcessor preAndPost(String v) {
            post = pre = ofNullable(v);
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
            callStack.push(parent);

            pre.ifPresent( v -> _buffer.append(v) );
            Node next;
            for(Node node = parent.getFirstChild(); node != null; node = next) {
                next = node.getNext();
                if( forEach.isPresent() ) {
                   forEach.get().accept(node);
                }
                else {
                    if( map.isPresent() ) {
                        _buffer.append( map.get().apply( node ) );
                    }
                    node.accept(ConfluenceWikiVisitor.this);
                }
            }
            post.ifPresent( v -> _buffer.append(v) );
            if( writeln ) _buffer.append('\n');

            callStack.pop();
        }
    }

    protected <T extends Node> ChildrenProcessor processChildren(T parent ) {
        return new ChildrenProcessor( parent );
    }
}
