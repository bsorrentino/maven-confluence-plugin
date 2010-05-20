package org.bsc.maven.reporting.sink;

import java.io.PrintWriter;
import java.io.Writer;

import org.apache.maven.doxia.sink.SinkAdapter;
import org.bsc.maven.plugin.confluence.ConfluenceUtils;


/**
 * 
 * Sink to produce confluence wiki render.
 * 
 * @plexus.component role="cosm.maven.reporting.Sink" role-hint="Confluence"
 * 
 * @author Sorrentino
 *
 */
@SuppressWarnings("unchecked")
public class ConfluenceSink extends SinkAdapter implements org.codehaus.doxia.sink.Sink {

	enum Command {
		
		HEADER,
		CELL,
		LINK,
		ANCHOR,
		TITLE
		
	}
	
	java.util.Stack commandStack = new java.util.Stack();
	java.util.Stack dataStack = new java.util.Stack();
	
	org.codehaus.doxia.sink.Sink _s;
	PrintWriter _w;
	
	public ConfluenceSink( Writer w, org.codehaus.doxia.sink.Sink delegate ) {
		this._s = delegate;
		_w = new PrintWriter(w);
		
	}

	public Writer getWriter() {
		return _w;
	}
	
	@Override
	public void horizontalRule() {

		_w.println( "---");	
		_s.horizontalRule();
	}

	@Override
	public void sectionTitle() {
		
		_w.print( "sectionTitle");	
		_s.sectionTitle();
	}

	
	@Override
	public void anchor_() {
		
		_s.anchor_();
	}

	@Override
	public void anchor(String name) {
		
		_w.printf("{anchor:%s} ", ConfluenceUtils.encodeAnchor(name) );		
		_s.anchor(name);
	}

	@Override
	public void author_() {
		
		_s.author_();
	}

	@Override
	public void author() {

		_w.print( "author");			
		_s.author();
	}

	@Override
	public void body_() {
		
		_s.body_();
	}

	@Override
	public void body() {

		_s.body();
	}

	@Override
	public void bold_() {
		
		_w.print('*');
		_s.bold_();
	}

	@Override
	public void bold() {
		
		_w.print('*');
		_s.bold();
	}

	@Override
	public void close() {
		
		_w.flush();
		_w.flush();
		//_w.close();
		//_s.close();
	}

	@Override
	public void date_() {
		
		_s.date_();
	}

	@Override
	public void date() {
		
		_w.print("date");
		_s.date();
	}

	@Override
	public void definedTerm_() {
		
		_s.definedTerm_();
	}

	@Override
	public void definedTerm() {
		
		_w.print( "definedTerm");			
		_s.definedTerm();
	}

	@Override
	public void definition_() {
		
		_s.definition_();
	}

	@Override
	public void definition() {
		
		_w.print( "definition");			
		_s.definition();
	}

	@Override
	public void definitionList_() {
		
		_s.definitionList_();
	}

	@Override
	public void definitionList() {
		
		_w.print("definitionList");
		_s.definitionList();
	}

	@Override
	public void definitionListItem_() {
		
		_s.definitionListItem_();
	}

	@Override
	public void definitionListItem() {
		
		_w.print("definitionListItem");
		_s.definitionListItem();
	}

	@Override
	public void figure_() {
		
		_s.figure_();
	}

	@Override
	public void figure() {
		
		_w.print( "figure");			
		_s.figure();
	}

	@Override
	public void figureCaption_() {
		
		_s.figureCaption_();
	}

	@Override
	public void figureCaption() {
		
		_w.print( "figureCaption");			
		_s.figureCaption();
	}

	@Override
	public void figureGraphics(String name) {
		
		_s.figureGraphics(name);
	}

	@Override
	public void flush() {
		
		_w.flush();
		_s.flush();
	}

	@Override
	public void head_() {
		
		_s.head_();
	}

	@Override
	public void head() {
		
		//_w.print( "head");			
		_s.head();
	}

	@Override
	public void italic_() {
		
		_w.print( '_');			
		_s.italic_();
	}

	@Override
	public void italic() {
		
		_w.print( '_');			
		_s.italic();
	}

	@Override
	public void lineBreak() {

		_w.println("\\\\");
		_s.lineBreak();
	}

	@Override
	public void link_() {
		
		commandStack.pop();
		String link = (String)dataStack.pop();
		
		_w.printf("|%s]", ConfluenceUtils.encodeAnchor(link));
		_w.println();
		_s.link_();
	}

	@Override
	public void link(String name) {
		
		dataStack.push(name);
		commandStack.push(Command.LINK);
		
		_w.print('[');
		_s.link(name);
	}

	@Override
	public void list_() {
		
		_s.list_();
	}

	@Override
	public void list() {
		
		_w.print('*');
		_s.list();
	}

	@Override
	public void listItem_() {
		
		_s.listItem_();
	}

	@Override
	public void listItem() {
		
		_w.print("* ");
		_s.listItem();
	}

	@Override
	public void monospaced_() {
		
		_w.println("}}");
		_s.monospaced_();
	}

	@Override
	public void monospaced() {
		
		_w.print("{{");
		_s.monospaced();
	}

	@Override
	public void nonBreakingSpace() {
		
		_s.nonBreakingSpace();
	}

	@Override
	public void numberedList_() {
		
		_s.numberedList_();
	}

	@Override
	public void numberedList(int numbering) {
		
		_w.printf("numberedList(%d)",numbering);
		_s.numberedList(numbering);
	}

	@Override
	public void numberedListItem_() {
		
		_s.numberedListItem_();
	}

	@Override
	public void numberedListItem() {
		
		_s.numberedListItem();
	}

	@Override
	public void pageBreak() {

		_w.println("----");
		_s.pageBreak();
	}

	@Override
	public void paragraph_() {
		
		_w.println();
		_s.paragraph_();
	}

	@Override
	public void paragraph() {
		
		//_w.print( "paragraph" );			
		_s.paragraph();
	}

	@Override
	public void rawText(String text) {
		
		_w.printf("rawText(%s)", text);
		_s.rawText(text);
	}

	@Override
	public void section1_() {
		
		_s.section1_();
	}

	@Override
	public void section1() {
		
		//_w.print("section1");
		_s.section1();
	}

	@Override
	public void section2_() {
		
		_s.section2_();
	}

	@Override
	public void section2() {
		
		//_w.print("section2");
		_s.section2();
	}

	@Override
	public void section3_() {
		
		_s.section3_();
	}

	@Override
	public void section3() {
		
		//_w.print("section3");
		_s.section3();
	}

	@Override
	public void section4_() {
		
		_s.section4_();
	}

	@Override
	public void section4() {
		
		//_w.print("section4");
		_s.section4();
	}

	@Override
	public void section5_() {
		
		_s.section5_();
	}

	@Override
	public void section5() {
		
		//_w.print("section5");
		_s.section5();
	}

	@Override
	public void sectionTitle_() {
		
		_w.print("h1. ");
		_s.sectionTitle_();
	}

	@Override
	public void sectionTitle1_() {
		
		_w.println();
		_s.sectionTitle1_();
	}

	@Override
	public void sectionTitle1() {
		
		_w.print("h1. ");
		_s.sectionTitle1();
	}

	@Override
	public void sectionTitle2_() {
		
		_w.println();
		_s.sectionTitle2_();
	}

	@Override
	public void sectionTitle2() {
		
		_w.print("h2. ");
		_s.sectionTitle2();
	}

	@Override
	public void sectionTitle3_() {
		
		_w.println();
		_s.sectionTitle3_();
	}

	@Override
	public void sectionTitle3() {
		
		_w.print("h3. ");
		_s.sectionTitle3();
	}

	@Override
	public void sectionTitle4_() {
		
		_w.println();
		_s.sectionTitle4_();
	}

	@Override
	public void sectionTitle4() {
		
		_w.print("h4. ");
		_s.sectionTitle4();
	}

	@Override
	public void sectionTitle5_() {
		
		_w.println();
		_s.sectionTitle5_();
	}

	@Override
	public void sectionTitle5() {
		
		_w.print("h5. ");
		_s.sectionTitle5();
	}

	@Override
	public void table_() {
		
		_s.table_();
	}

	@Override
	public void table() {
		
		_w.println();
		_s.table();
	}

	@Override
	public void tableCaption_() {

		_s.tableCaption_();
	}

	@Override
	public void tableCaption() {		
		
		_w.print("tableCaption");
		_s.tableCaption();
	}

	@Override
	public void tableCell_() {		
		
		_s.tableCell_();

	}

	@Override
	public void tableCell() {		

		if( commandStack.isEmpty() ) {
			commandStack.push(Command.CELL);
		}
		
		_w.print('|');
		_s.tableCell();
				
	}

	@Override
	public void tableCell(String width) {

		if( commandStack.isEmpty() ) {
			commandStack.push(Command.CELL);
		}

		_w.print('|');
		_s.tableCell(width);
	}

	@Override
	public void tableHeaderCell_() {
		
		_s.tableHeaderCell_();
	}

	@Override
	public void tableHeaderCell() {
		
		if( commandStack.isEmpty() ) {
			commandStack.push(Command.HEADER);
		}
		_w.print("||");
		_s.tableHeaderCell();
	}

	@Override
	public void tableHeaderCell(String width) {
		
		if( commandStack.isEmpty() ) {
			commandStack.push(Command.HEADER);
		}
		_w.print("||");
		_s.tableHeaderCell(width);
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
		
		_s.tableRow_();
	}

	@Override
	public void tableRow() {

		_s.tableRow();
	}

	@Override
	public void tableRows_() {
		
		_s.tableRows_();
	}

	@Override
	public void tableRows(int[] justification, boolean grid) {
		
		_s.tableRows(justification, grid);
	}

	@Override
	public void text(String text) {
	
		_s.text(text);
		if( !commandStack.isEmpty() ) {
			Command c = (Command)commandStack.peek();
			
			// ignore text after title
			if( Command.TITLE == c ) return;
		}
		_w.print( ConfluenceUtils.decode(text) );
	}

	@Override
	public void title_() {
		
		commandStack.pop();
		_s.title_();
	}

	@Override
	public void title() {
		commandStack.push(Command.TITLE);
		
		//_w.print("title");
		_s.title();
	}

	@Override
	public void verbatim_() {
		
		_w.println("{noFormat}");
		_s.verbatim_();
	}

	@Override
	public void verbatim(boolean boxed) {
		
		_w.println("{noFormat}");
		_s.verbatim(boxed);
	}


	
	
}
