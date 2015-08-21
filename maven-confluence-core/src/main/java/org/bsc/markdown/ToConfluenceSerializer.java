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
import org.bsc.functional.F;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bsorrentino
 */
public class ToConfluenceSerializer implements Visitor {

    private StringBuilder _buffer = new StringBuilder( 500 * 1024 );

    @Override
    public String toString() {
        return _buffer.toString();
    }
    
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
        
        boolean apply( BlockQuoteNode bqn ) {
            element = null;
            title = null;
            
            final boolean result = 
                    findByClass(bqn.getChildren().get(0), 
                        StrongEmphSuperNode.class, 
                        new FindPredicate<StrongEmphSuperNode>() {

                @Override
                public boolean f(StrongEmphSuperNode p, final Node parent, final int index) {
                    if( index!=0 || !p.isStrong() ) return false;

                    boolean found =  findByClass(p, 
                                    TextNode.class, 
                                    new FindPredicate<TextNode>() {

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
                    });

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
                bqn.getChildren().remove(0);
            
                _buffer.append( format("{%s:title=%s}", element, title));
                visitChildren(bqn);
                _buffer.append( format("{%s}", element) ).append('\n');;
            }
            
            return result;

        }
    }
    
    protected <T extends Node> void visitChildren(T node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }

    protected <T extends Node, R extends Node> boolean findByClass(T node, Class<R> clazz, FindPredicate<R> predicate ) {
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

    @Override
    public void visit(RootNode rn) {
        visitChildren(rn);
    }
    
    @Override
    public void visit(SuperNode sn) {
        //sb.append('\n');
        visitChildren(sn);
        //sb.append('\n');
    }

    @Override
    public void visit(ParaNode pn) {
        _buffer.append('\n');
        visitChildren(pn);
        _buffer.append('\n');
    }


    @Override
    public void visit(HeaderNode hn) {
        _buffer.append( format( "h%s.", hn.getLevel()) );
        visitChildren(hn);
        _buffer.append('\n');
    }


    final SpecialPanelProcessor specialPanelProcessor = new SpecialPanelProcessor();

    @Override
    public void visit(BlockQuoteNode bqn) {
        
        if( !specialPanelProcessor.apply(bqn) ) {
            
            _buffer.append( "{quote}" );
            visitChildren(bqn);
            _buffer.append( "{quote}" ).append('\n');            
        }
        
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
    public void visit(CodeNode cn) {
        
        String lines[] = cn.getText().split("\n");
        if( lines.length == 1 || lines[0].isEmpty() ) {
            _buffer.append( "{noformat}").append(cn.getText()).append( "{noformat}");
            return;
        }
        
        _buffer.append( format("{code:%s}", lines[0])).append('\n');
        for( int i =  1 ; i < lines.length; ++i ) {
            _buffer.append(lines[i]).append('\n');
        }
        _buffer.append( "{code}");
    }

    @Override
    public void visit(StrongEmphSuperNode sesn) {
        char sym = '*';
        if( !sesn.isStrong() ) {
            final String chars = sesn.getChars();
            if( chars.equals("*")) sym = '_';
        }
        _buffer.append( sym);
        visitChildren(sesn);
        _buffer.append( sym );
        
    }

    @Override
    public void visit(BulletListNode bln) {
    }

    @Override
    public void visit(SpecialTextNode stn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public void visit(AbbreviationNode an) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(AnchorLinkNode aln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(AutoLinkNode aln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void visit(DefinitionListNode dln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(DefinitionNode dn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(DefinitionTermNode dtn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(ExpImageNode ein) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void visit(HtmlBlockNode hbn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(InlineHtmlNode ihn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(ListItemNode lin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(MailLinkNode mln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(OrderedListNode oln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(QuotedNode qn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(ReferenceNode rn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(RefImageNode rin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(RefLinkNode rln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void visit(SimpleNode sn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(StrikeNode sn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(TableBodyNode tbn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(TableCaptionNode tcn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(TableCellNode tcn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(TableColumnNode tcn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(TableHeaderNode thn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(TableNode tn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(TableRowNode trn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(VerbatimNode vn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(WikiLinkNode wln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(Node node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
