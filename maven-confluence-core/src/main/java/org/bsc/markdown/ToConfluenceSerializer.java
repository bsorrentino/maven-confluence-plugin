package org.bsc.markdown;


import org.pegdown.ast.AbbreviationNode;
import org.pegdown.ast.AnchorLinkNode;
import org.pegdown.ast.AutoLinkNode;
import org.pegdown.ast.BlockQuoteNode;
import org.pegdown.ast.BulletListNode;
import org.pegdown.ast.CodeNode;
import org.pegdown.ast.DefinitionListNode;
import org.pegdown.ast.DefinitionNode;
import org.pegdown.ast.DefinitionTermNode;
import org.pegdown.ast.ExpImageNode;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.HtmlBlockNode;
import org.pegdown.ast.InlineHtmlNode;
import org.pegdown.ast.ListItemNode;
import org.pegdown.ast.MailLinkNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.OrderedListNode;
import org.pegdown.ast.ParaNode;
import org.pegdown.ast.QuotedNode;
import org.pegdown.ast.RefImageNode;
import org.pegdown.ast.RefLinkNode;
import org.pegdown.ast.ReferenceNode;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.SimpleNode;
import org.pegdown.ast.SpecialTextNode;
import org.pegdown.ast.StrikeNode;
import org.pegdown.ast.StrongEmphSuperNode;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.TableBodyNode;
import org.pegdown.ast.TableCaptionNode;
import org.pegdown.ast.TableCellNode;
import org.pegdown.ast.TableColumnNode;
import org.pegdown.ast.TableHeaderNode;
import org.pegdown.ast.TableNode;
import org.pegdown.ast.TableRowNode;
import org.pegdown.ast.TextNode;
import org.pegdown.ast.VerbatimNode;
import org.pegdown.ast.Visitor;
import org.pegdown.ast.WikiLinkNode;

import static java.lang.String.format;
import java.net.URISyntaxException;
import org.bsc.functional.F;
import org.pegdown.Extensions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bsorrentino
 */
public abstract class ToConfluenceSerializer implements Visitor {

    private StringBuilder _buffer = new StringBuilder( 500 * 1024 );

    private final java.util.Stack<Node> nodeStack = new java.util.Stack<Node>();

    public static int extensions() {

        int EXT = Extensions.NONE;

        EXT |= Extensions.FENCED_CODE_BLOCKS;
        EXT |= Extensions.TABLES;
        EXT |= Extensions.STRIKETHROUGH;

        return EXT;
    }

    /**
     *
     * @param text
     * @param node
     * @return [line,col]
     */
    public static int[] lineAndColFromNode(String text, Node node) {
        int lastEOL = 0;
        int prevEOL = 0;
        int length = text.length();
        int pos = 0;
        int line = 0;
        int col = 0;

        int offset = node.getStartIndex();

        if (offset > length) {
            offset = length;
        }

        while (pos < length) {
            pos = text.indexOf('\n', pos);
            if (pos == -1) break; // no EOL at the end or we hit the end

            prevEOL = lastEOL;
            lastEOL = pos;

            if (pos > offset) {
                break;
            }

            line++;
            pos++;
        }

        if (prevEOL < offset && lastEOL >= offset) {
            // offset is on this line
            col = offset - prevEOL;
        }

        line++;
        //System.out.println("offset : " + offset + " = (" + String.valueOf(line) + ":" + String.valueOf(col) + ")");

        return new int[] {line,col};
    }

    @Override
    public String toString() {
        return _buffer.toString();
    }

    protected abstract void notImplementedYet( Node node );

    protected StringBuilder bufferVisit( F<Void,Void> closure  ) {

        final StringBuilder _sb = new StringBuilder();

        final StringBuilder _original = _buffer;
        _buffer = _sb;
        try {
            closure.f(null);
        }
        finally {
            _buffer = _original;
        }

        return _sb;
    }

    protected interface FindPredicate<T extends Node> {

        boolean f( T node, Node parent, int index );

    }

    /**
    * process:
    *  note,warning,info,tip
    */
    protected class SpecialPanelProcessor {
        private String element;
        private String title;


        final FindPredicate<TextNode> isSpecialPanelText = new FindPredicate<TextNode>() {

            @Override
            public boolean f(TextNode p, Node parent, int index ) {
                if( index != 0 ) return false;

                if( "note:".equalsIgnoreCase(p.getText()) ) {
                    element = "note"; // SET ELEMENT TAG
                    return true;
                }
                if( "warning:".equalsIgnoreCase(p.getText()) ) {
                    element = "warning"; // SET ELEMENT TAG
                    return true;
                }
                if( "info:".equalsIgnoreCase(p.getText()) ) {
                    element = "info"; // SET ELEMENT TAG
                    return true;
                }
                if( "tip:".equalsIgnoreCase(p.getText()) ) {
                    element = "tip"; // SET ELEMENT TAG
                    return true;
                }


                return false;
            }
        };

        boolean apply( BlockQuoteNode bqn ) {
            element = null;
            title = null;

            java.util.List<Node> children = bqn.getChildren();

            if( children.size() != 2 ) return false;

            Node node1 = bqn.getChildren().get(1);

            if( !(node1 instanceof BlockQuoteNode ) ) return false;

            final boolean result =
                    findByClass(bqn.getChildren().get(0),
                        StrongEmphSuperNode.class,
                        new FindPredicate<StrongEmphSuperNode>() {

                @Override
                public boolean f(StrongEmphSuperNode p, final Node parent, final int index) {
                    if( index!=0 || !p.isStrong() ) return false;

                    boolean found =  findByClass(p,
                                    TextNode.class, isSpecialPanelText );

                    if( found ) { // GET ELEMENT TITLE

                        final StringBuilder _sb = bufferVisit(new F<Void,Void>() {

                            @Override
                            public Void f(Void p) {
                               parent.getChildren().remove(0);
                               visitChildren(parent);
                               return null;
                            }

                        });
                        title = _sb.toString().trim();

                    }

                    return found;

                }

            });

            if( result ) {

                _buffer.append( format("{%s:title=%s}", element, title));
                visitChildren(node1);
                _buffer.append( format("{%s}", element) ).append('\n');;
            }

            return result;

        }
    }

    protected <T extends Node> void forEachChild( T node, FindPredicate<T> cb ) {
        final java.util.List<Node> children = node.getChildren();

        for (int index = 0 ; index < children.size() ; ++index) {

            final Node child = children.get(index);

            if( !cb.f( (T)child, node, index ) ) {
                return ;
            }
        }
    }

    protected <T extends Node> void visitChildren(T node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }

    protected <T extends Node, R extends Node> boolean findByClass(T node, final Class<R> clazz, final FindPredicate<R> predicate ) {
        boolean result = false;
        final java.util.List<Node> children = node.getChildren();

        for (int index = 0 ; index < children.size() ; ++index) {

            final Node child = children.get(index);

            if( clazz.isInstance(child) && predicate.f(clazz.cast(child), node, index)) {
                result = true;
            } else {
                result = findByClass( child, clazz, predicate);
            }
            if( result ) break;

        }

        return result;
    }

    final SpecialPanelProcessor specialPanelProcessor = new SpecialPanelProcessor();


    @Override
    public void visit(RootNode rn) {
        //specialPanelProcessor.init(rn);
        visitChildren(rn);
    }

    @Override
    public void visit(SuperNode sn) {
        visitChildren(sn);
    }

    @Override
    public void visit(ParaNode pn) {
        visitChildren(pn);
        _buffer.append('\n');
    }


    @Override
    public void visit(HeaderNode hn) {
        _buffer.append( format( "h%s.", hn.getLevel()) );
        visitChildren(hn);
        _buffer.append('\n');
    }


    @Override
    public void visit(final BlockQuoteNode bqn) {

      final String text = bufferVisit(new F<Void,Void>() {

          @Override
          public Void f(Void p) {
             visitChildren(bqn);
             return null;
          }

      }).toString();

      final String lines[] = text.split("\n");

      if( lines.length == 1 ) {
        _buffer.append('\n')
               .append( "bq. ")
               .append( text )
               .append('\n');
        return;
      }

      if( specialPanelProcessor.apply(bqn) ) return;

      _buffer.append('\n')
             .append( "{quote}" )
             .append('\n')
             .append( text )
             .append('\n')
             .append( "{quote}" )
             .append('\n');

    }

    @Override
    public void visit(TextNode tn) {
        _buffer.append( tn.getText() );
    }

    @Override
    public void visit(ExpLinkNode eln) {
        _buffer.append( '[');
        visitChildren(eln);
        _buffer.append( format( "|%s|%s]", eln.url, eln.title));
    }


    @Override
    public void visit(VerbatimNode vn) {

        final String lines[] = vn.getText().split("\n");
        if( lines.length == 1 ) {
            _buffer.append( "{noformat}")
                   .append(vn.getText())
                   .append( "{noformat}");
            return;
        }

        if( vn.getType()==null || vn.getType().isEmpty() ) {
            _buffer.append( "{noformat}")
                    .append('\n')
                    .append(vn.getText())
                    .append('\n')
                    .append( "{noformat}")
                    .append('\n')
                    ;
            return;
        }

        _buffer.append( format("{code:%s}", vn.getType()) )
                .append('\n')
                .append(vn.getText())
                .append('\n')
                .append( "{code}")
                .append('\n')
                ;
    }

    @Override
    public void visit(CodeNode cn) {

        final String lines[] = cn.getText().split("\n");
        if( lines.length == 1 ) {
            _buffer.append( "{{")
                   .append(cn.getText())
                   .append( "}}");
            return;
        }

        _buffer
            .append( "{code}")
            .append('\n')
            .append(cn.getText())
            .append( "{code}")
            .append('\n')
            ;
    }

    @Override
    public void visit(StrongEmphSuperNode sesn) {
        char sym = '*';
        if( !sesn.isStrong() ) {
            final String chars = sesn.getChars();
            if( chars.equals("_")) sym = '_';
        }
        _buffer.append( sym);
        visitChildren(sesn);
        _buffer.append( sym );

    }



    @Override
    public void visit(StrikeNode sn) {
        _buffer.append("-");
        visitChildren(sn);
        _buffer.append("-");
    }

    @Override
    public void visit(BulletListNode bln) {

        _buffer.append('\n');
        for (Node child : bln.getChildren()) {
            _buffer.append("* ");
            child.accept(this);
            _buffer.append('\n');
        }
		_buffer.append('\n');

    }
    @Override
    public void visit(ListItemNode lin) {
        visitChildren(lin);
    }

    @Override
    public void visit(final ExpImageNode ein) {
        //visitChildren(ein);

        try {

            final java.net.URI uri = new java.net.URI(ein.url);

            final String scheme = uri.getScheme();

            if( !uri.isAbsolute()
                     && (null==uri.getScheme() || scheme.isEmpty())
                     && findByClass(ein, TextNode.class, new FindPredicate<TextNode>() {

                            @Override
                            public boolean f(TextNode node, Node parent, int index) {
                                _buffer.append( format( "!%s!", node.getText() ));
                                return true;
                            }
                        }))
            {
             return;
            }

         } catch (URISyntaxException ex) {
             // @TODO notify error
         }


        _buffer.append( format( "!%s!", ein.url));

    }

    @Override
    public void visit(HtmlBlockNode hbn) {
    }

    @Override
    public void visit(InlineHtmlNode ihn) {
    }

    @Override
    public void visit(TableHeaderNode thn) {
        nodeStack.push(thn);
        try {
            visitChildren(thn);
        }
        finally {
            assert thn == nodeStack.pop();
        }
    }

    @Override
    public void visit(TableBodyNode tbn) {
       nodeStack.push(tbn);
        try {
            visitChildren(tbn);
        }
        finally {
            assert tbn == nodeStack.pop();
        }
     }
    @Override
    public void visit(TableRowNode trn) {
        final Node n = nodeStack.peek();

        if( n instanceof TableHeaderNode )
            _buffer.append("||");
        else if( n instanceof TableBodyNode )
            _buffer.append('|');

        visitChildren(trn);
        _buffer.append('\n');
    }

    @Override
    public void visit(TableCaptionNode tcn) {
        notImplementedYet(tcn);
    }

    @Override
    public void visit(TableCellNode tcn) {

        final Node n = nodeStack.peek();

        visitChildren(tcn);

        if( n instanceof TableHeaderNode )
            _buffer.append("||");
        else if( n instanceof TableBodyNode )
            _buffer.append('|');

    }

    @Override
    public void visit(RefLinkNode rln) {
        _buffer.append( '[' );
        visitChildren(rln);
        _buffer.append( ']' );
    }



    @Override
    public void visit(TableColumnNode tcn) {
        notImplementedYet(tcn);
    }

    @Override
    public void visit(TableNode tn) {
        visitChildren(tn);
    }

    @Override
    public void visit(AnchorLinkNode aln) {
        _buffer.append( aln.getText() );
    }

    @Override
    public void visit(SpecialTextNode stn) {
        _buffer.append(stn.getText());
    }


    @Override
    public void visit(AbbreviationNode an) {
        notImplementedYet(an);
    }


    @Override
    public void visit(AutoLinkNode aln) {
        notImplementedYet(aln);
    }


    @Override
    public void visit(DefinitionListNode dln) {
        notImplementedYet(dln);
    }

    @Override
    public void visit(DefinitionNode dn) {
        notImplementedYet(dn);
    }

    @Override
    public void visit(DefinitionTermNode dtn) {
        notImplementedYet(dtn);
    }




    @Override
    public void visit(MailLinkNode mln) {
        notImplementedYet(mln);
    }

    @Override
    public void visit(OrderedListNode oln) {
        _buffer.append('\n');
        for (Node child : oln.getChildren()) {
            _buffer.append("# ");
            child.accept(this);
            _buffer.append('\n');
        }
        _buffer.append('\n');
    }

    @Override
    public void visit(QuotedNode qn) {
        notImplementedYet(qn);
    }

    @Override
    public void visit(ReferenceNode rn) {
        notImplementedYet(rn);
    }

    @Override
    public void visit(RefImageNode rin) {
        notImplementedYet(rin);
    }

    @Override
    public void visit(SimpleNode sn) {
        notImplementedYet(sn);
    }

    @Override
    public void visit(WikiLinkNode wln) {
        notImplementedYet(wln);
    }

    @Override
    public void visit(Node node) {
        notImplementedYet(node);
    }

}
