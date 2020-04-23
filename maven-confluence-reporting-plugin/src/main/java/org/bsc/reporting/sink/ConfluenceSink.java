package org.bsc.reporting.sink;

import java.io.PrintWriter;
import java.io.Writer;
import org.apache.maven.doxia.sink.SinkEventAttributes;

import org.bsc.confluence.ConfluenceUtils;


/**
 * 
 * Sink to produce confluence wiki render.
 * 
 * @plexus.component role="cosm.maven.reporting.Sink" role-hint="Confluence"
 * 
 * @author Sorrentino
 *
 */
public class ConfluenceSink extends org.apache.maven.doxia.sink.AbstractSink {



    @Override
    public void author(SinkEventAttributes sea) {
        /*
            MutableAttributeSet atts = 
                    SinkUtils.filterAttributes(sea, SinkUtils.SINK_BASE_ATTRIBUTES  );
        */
    }

    @Override
    public void date(SinkEventAttributes sea) {
    }

    @Override
    public void body(SinkEventAttributes sea) {
    }

    @Override
    public void section(int i, SinkEventAttributes sea) {
    }

    @Override
    public void section_(int i) {
    }

    @Override
    public void sectionTitle(int i, SinkEventAttributes sea) {
        _w.printf("h%d. ", i);

    }

    @Override
    public void sectionTitle_(int i) {
    }

    @Override
    public void list(SinkEventAttributes sea) {
		_w.print('*');
    }

    @Override
    public void listItem(SinkEventAttributes sea) {
    }

    @Override
    public void numberedList(int i, SinkEventAttributes sea) {
    }

    @Override
    public void numberedListItem(SinkEventAttributes sea) {
    }

    @Override
    public void definitionList(SinkEventAttributes sea) {
    }

    @Override
    public void definitionListItem(SinkEventAttributes sea) {
    }

    @Override
    public void definition(SinkEventAttributes sea) {
    }

    @Override
    public void definedTerm(SinkEventAttributes sea) {
    }

    @Override
    public void figure(SinkEventAttributes sea) {
    }

    @Override
    public void figureCaption(SinkEventAttributes sea) {
    }

    @Override
    public void figureGraphics(String string, SinkEventAttributes sea) {
    }

    @Override
    public void table(SinkEventAttributes sea) {
		_w.println();
    }

    @Override
    public void tableRow(SinkEventAttributes sea) {
    }

    @Override
    public void tableCell(SinkEventAttributes sea) {
		if( commandStack.isEmpty() ) {
			commandStack.push(Command.CELL);
		}
		
		_w.print('|');
    }

    @Override
    public void tableHeaderCell(SinkEventAttributes sea) {
		if( commandStack.isEmpty() ) {
			commandStack.push(Command.HEADER);
		}
		_w.print("||");
    }

    @Override
    public void tableCaption(SinkEventAttributes sea) {
    }

    @Override
    public void paragraph(SinkEventAttributes sea) {
    }

    @Override
    public void verbatim(SinkEventAttributes sea) {
                if( !commandStack.empty() && Command.PANEL==commandStack.peek()) {
        		_w.println("{panel}");
                        dataStack.push("{panel}");
                    
                }
                else {
        		_w.println("{noFormat}");
                        dataStack.push("{noFormat}");
                    
                }
    }

    @Override
    public void horizontalRule(SinkEventAttributes sea) {
		_w.println( "---");	
    }

    @Override
    public void anchor(String string, SinkEventAttributes sea) {
    }

    @Override
    public void link(String name, SinkEventAttributes sea) {
		dataStack.push(name);
		commandStack.push(Command.LINK);
		
		_w.print('[');
    }

    @Override
    public void lineBreak(SinkEventAttributes sea) {
    		_w.println("\\\\");
    }

    @Override
    public void text(String text, SinkEventAttributes sea) {
		if( !commandStack.isEmpty() ) {
			Command c = (Command)commandStack.peek();
			
			// ignore text after title
			if( Command.TITLE == c ) return;
		}
		_w.print( ConfluenceUtils.decode(text) );
    }

    @Override
    public void comment(String string) {
    }

    @Override
    public void unknown(String string, Object[] os, SinkEventAttributes sea) {
    }

	public enum Command {
		
		HEADER,
		CELL,
		LINK,
		ANCHOR,
		TITLE,
                PANEL
		
	}
	
	final java.util.Stack<Command> commandStack = new java.util.Stack<Command>();
	final java.util.Stack<String> dataStack = new java.util.Stack<String>();
	
	PrintWriter _w;
	
        /**
         * 
         * 
         */
        public static void pushCommandBlock( org.apache.maven.doxia.sink.Sink sink, ConfluenceSink.Command cmd, Runnable task ) {
            if( task == null ) throw new IllegalArgumentException("task parameter is null!");
            
            if( sink instanceof ConfluenceSink ) {
           
                final ConfluenceSink s = (ConfluenceSink) sink;
                s.execCommand(cmd, task);
             
            }
            else             
                task.run();
        }

        public ConfluenceSink( Writer w ) {
		_w = new PrintWriter(w);
		
	}

        public void execCommand(  Command cmd, Runnable task ) {
            if( task == null ) throw new IllegalArgumentException("task parameter is null!");
            if( cmd == null ) throw new IllegalArgumentException("cmd parameter is null!");

            try {
            commandStack.push( cmd );

                task.run();
            }
            finally {
                commandStack.pop();
            }
            
        }
	public Writer getWriter() {
		return _w;
	}
	
	@Override
	public void horizontalRule() {
            horizontalRule(null);
	}

	@Override
	public void sectionTitle() {

        }

	
	@Override
	public void anchor_() {
		
	}

	@Override
	public void anchor(String name) {
            anchor(name, null);
	}

	@Override
	public void author_() {
		
	}

	@Override
	public void author() {

            author(null);
	}

	@Override
	public void body_() {
		
	}

	@Override
	public void body() {
            body( null );
	}

	@Override
	public void bold_() {
		
		_w.print('*');
	}

	@Override
	public void bold() {
		
		_w.print('*');
	}

	@Override
	public void close() {
		
		_w.flush();
		_w.flush();
	}

	@Override
	public void date_() {
		
	}

	@Override
	public void date() {
		
            date(null);
	}

	@Override
	public void definedTerm_() {
		
	}

	@Override
	public void definedTerm() {
		
            definedTerm(null);
	}

	@Override
	public void definition_() {
		
	}

	@Override
	public void definition() {
            definition( null );
	}

	@Override
	public void definitionList_() {
		
	}

	@Override
	public void definitionList() {
            definitionList(null);
	}

	@Override
	public void definitionListItem_() {
		
	}

	@Override
	public void definitionListItem() {
            definitionListItem(null);
        }

	@Override
	public void figure_() {
		
	}

	@Override
	public void figure() {
            figure(null);
        }

	@Override
	public void figureCaption_() {
		
	}

	@Override
	public void figureCaption() {
            figureCaption(null);
	}

	@Override
	public void figureGraphics(String name) {
            figureGraphics(name, null);
	}

	@Override
	public void flush() {	
		_w.flush();
	}

	@Override
	public void head_() {
		
	}

	@Override
	public void head() {
            head(null);
	}

        @Override
        public void head(SinkEventAttributes sea) {
        }

	@Override
	public void italic_() {
		
		_w.print( '_');			
	}

	@Override
	public void italic() {
		
		_w.print( '_');			
	}

	@Override
	public void lineBreak() {
            lineBreak(null);
	}

	@Override
	public void link_() {
		
		commandStack.pop();
		String link = (String)dataStack.pop();
		
		_w.printf("|%s]", ConfluenceUtils.encodeAnchor(link));
		//_w.println();
	}

	@Override
	public void link(String name) {
	
            link(name, null);
	}

	@Override
	public void list_() {
		
	}

	@Override
	public void list() {
            list(null);
	}

	@Override
	public void listItem_() {
		
	}

	@Override
	public void listItem() {
		
		_w.print("* ");
	}

	@Override
	public void monospaced_() {
		
		_w.println("}}");
	}

	@Override
	public void monospaced() {
		
		_w.print("{{");
	}

	@Override
	public void nonBreakingSpace() {
		
	}

	@Override
	public void numberedList_() {
		
	}

	@Override
	public void numberedList(int numbering) {
            numberedList(numbering, null);
	}

	@Override
	public void numberedListItem_() {
		
	}

	@Override
	public void numberedListItem() {
            numberedListItem(null);
	}

	@Override
	public void pageBreak() {

		_w.println("----");
	}

	@Override
	public void paragraph_() {
		
		_w.println();
	}

	@Override
	public void paragraph() {
		paragraph(null);
	}

	@Override
	public void rawText(String text) {
		
		_w.print(text);
	}

	@Override
	public void section1_() {
	}

	@Override
	public void section1() {
		section(1, null);
	}

	@Override
	public void section2_() {
		
	}

	@Override
	public void section2() {
		section(2, null);
		
	}

	@Override
	public void section3_() {
		
	}

	@Override
	public void section3() {
		section(3, null);
		
	}

	@Override
	public void section4_() {
		
	}

	@Override
	public void section4() {
		section(4, null);
		
	}

	@Override
	public void section5_() {
		
	}

	@Override
	public void section5() {
		section(5, null);		
	}

	@Override
	public void sectionTitle_() {
		
		_w.print("h1. ");
	}

	@Override
	public void sectionTitle1_() {
		
		_w.println();
	}

	@Override
	public void sectionTitle1() {
            sectionTitle(1, null);
	}

	@Override
	public void sectionTitle2_() {
		
		_w.println();
	}

	@Override
	public void sectionTitle2() {
		
            sectionTitle(2, null);
	}

	@Override
	public void sectionTitle3_() {
		
		_w.println();
	}

	@Override
	public void sectionTitle3() {
		
            sectionTitle(3, null);
	}

	@Override
	public void sectionTitle4_() {
		
		_w.println();
	}

	@Override
	public void sectionTitle4() {
		
            sectionTitle(4, null);
	}

	@Override
	public void sectionTitle5_() {
		
		_w.println();
	}

	@Override
	public void sectionTitle5() {
		
            sectionTitle(5, null);
	}

	@Override
	public void table_() {
		
	}

	@Override
	public void table() {
            table(null);
	}

	@Override
	public void tableCaption_() {

	}

	@Override
	public void tableCaption() {		
            tableCaption(null);
	}

	@Override
	public void tableCell_() {		
		
	}

	@Override
	public void tableCell() {		
            tableCell((SinkEventAttributes)null);
				
	}

	@Override
	public void tableCell(String width) {
            tableCell((SinkEventAttributes)null);
	}

	@Override
	public void tableHeaderCell_() {
		
	}

	@Override
	public void tableHeaderCell() {
            tableHeaderCell((SinkEventAttributes)null);
	}

	@Override
	public void tableHeaderCell(String width) {
	}

	@Override
	public void tableRow_() {
            
		Command c = (Command) commandStack.pop();
		
		if( Command.CELL==c ) {
			_w.println('|');
		}
		else if( Command.HEADER==c ) {
			_w.println( "||");
		}
		else {
			_w.println();
		}
		
	}

	@Override
	public void tableRow() {
            tableRow(null);
	}

	@Override
	public void tableRows_() {
		
	}

	@Override
	public void tableRows(int[] justification, boolean grid) {
		
	}

	@Override
	public void text(String text) {
            this.text(text,null);
	}

	@Override
	public void title_() {
		
		commandStack.pop();
	}

	@Override
	public void title() {
            title(null);
	}

        @Override
        public void title(SinkEventAttributes sea) {
            commandStack.push(Command.TITLE);
        }

	@Override
	public void verbatim_() {
		
		_w.println( dataStack.pop() );
	}

	@Override
	public void verbatim(boolean boxed) {
            verbatim(null);
	}

	
}
