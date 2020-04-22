package org.bsc.maven.reporting.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * 
 * @author Sorrentino
 *
 */
public class ConfluenceWikiWriter extends PrintWriter {

	
	
	public ConfluenceWikiWriter(File file) throws FileNotFoundException {
		super(file);
	}

	/**
	 * 
	 * @param out
	 */
	public ConfluenceWikiWriter(Writer out) {
		super(out);
	}

	public void printBiggestHeading( String value ) {
		super.printf( "h1. %s\n", value );
	}

	public ConfluenceWikiWriter appendBiggerHeading() {
		append( "h2. ");
		return this;
	}
	public ConfluenceWikiWriter appendBigHeading() {
		append( "h3. ");
		return this;
	}
	
	/**
	 *  {info:title=Useful Information}
	 *	This macro is useful for including helpful information in your confluence pages
	 *	{info}
	 *
	 * @param value
	 */
	public void printInfo( String title, String value ) {
		printf( "{info:title=%s}\n%s\n{info}", title, value );
		println();
	}
	
	public void printBiggerHeading( String value ) {
		super.printf( "h2. %s\n", value );
	}

	public void printBigHeading( String value ) {
		super.printf( "h3. %s\n", value );
	}
	
	public void printNormalHeading( String value ) {
		super.printf( "h4. %s\n", value );
	}

	public void printSmallHeading( String value ) {
		super.printf( "h5. %s\n", value );
	}

	public void printSmallestHeading( String value ) {
		super.printf( "h6. %s\n", value );
	}
	
	public ConfluenceWikiWriter appendBullet() {
		append("* ");
		return this;	
	}
	
	public void printBullet( String value ) {
		printBullets(1, value);
	}
	
	public void printQuote( String value  ) {
		println( "{quote}");
		println( value);
		println( "{quote}");
	}
	
	public void printNewParagraph() {
		println();
	}
	
	public void printLineBreak() {
		println( "\\\\");
	}
	
	public void printStrong( String value  ) {
		super.printf( "*%s*\n", value );		
	}

	
	public void printBullets( int depth, String value ) {
		for( int i=0; i< depth ; ++i ) {
			append('*');
		}
		append( ' ').append(value).append('\n');
	}
	
	public void printLinkToAnchor( String anchorName, String value  ) {
		printf( "[%s|#%s]\n", value, anchorName );		
	}
	public void printLink( String link, String value  ) {
		printf( "[%s|%s]\n", value, link );		
	}
	
	public ConfluenceWikiWriter appendAnchor(String anchorName, String value ) {
		printAnchor( anchorName, value );
		return this;
	}
	
	public void printAnchor( String anchorName, String value ) {
		printf( createAnchor(anchorName, value) );
	}

	static String createLinkToAnchor(String anchorName, String value) {
		return String.format("[%s|#%s]", value, anchorName);
	}

	static String createAnchor(String anchorName, String value) {
		return String.format("{anchor:%s}%s", anchorName, value);
	}

}
