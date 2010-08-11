package org.bsc.maven.plugin.confluence;

import static org.bsc.maven.plugin.confluence.ConfluenceUtils.decode;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.Parameter;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.reporting.MavenReportException;
import org.apache.maven.tools.plugin.PluginToolsRequest;
import org.apache.maven.tools.plugin.generator.Generator;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.swizzle.confluence.Confluence;
import org.codehaus.swizzle.confluence.Page;

import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.VariableNotDefinedException;

/**
 * 
 * @author Sorrentino
 *
 */
@SuppressWarnings("unchecked")
public class PluginConfluenceDocGenerator implements Generator {
    public static final String DEFAULT_PLUGIN_TEMPLATE_WIKI = "defaultPluginTemplate.wiki";

	private final Confluence confluence;
	private final Page parentPage;
    private final java.io.File templateWiki;
	
    private final ConfluenceGeneratePluginDocMojo mojo;
    /**
	 * 
	 * @param space
	 */
	public PluginConfluenceDocGenerator( ConfluenceGeneratePluginDocMojo mojo, Confluence confluence, Page parentPage, java.io.File templateWiki ) {
		this.mojo = mojo;
		this.confluence = confluence;
		this.parentPage = parentPage;
        this.templateWiki = templateWiki;
	
	}

	
	/**
	 * 
	 */
	public void execute( File destinationDirectory, PluginDescriptor pluginDescriptor ) throws IOException {
	    	

        try {
			processMojoDescriptors(pluginDescriptor);
		} catch (Exception e) {
			
			throw new IOException( e.getMessage() );
		}
    }
	
	/**
	 * 
	 */
	public void execute(File destinationDirectory, PluginToolsRequest request) throws IOException {
        try {
			processMojoDescriptors(request.getPluginDescriptor());
		} catch (Exception e) {
			
			throw new IOException( e.getMessage() );
		}
		
	}

	
	/**
	 * 
	 * @param mojoDescriptor
	 * @param destinationDirectory
	 * @throws IOException
	 */
    protected void processMojoDescriptors( PluginDescriptor pluginDescriptor ) throws Exception
    {
    	List<MojoDescriptor> mojos = pluginDescriptor.getMojos();

    	if( mojos==null ) {
            mojo.getLog().warn( "no mojos found [pluginDescriptor.getMojos()]");
            return ;
        }

        MiniTemplator t = null;

        if( templateWiki==null || !templateWiki.exists()) {
        		mojo.getLog().warn("template not set! default using ...");

                java.io.InputStream is = getClass().getClassLoader().getResourceAsStream(DEFAULT_PLUGIN_TEMPLATE_WIKI);

                if( is==null ) {
                        final String msg = "default template cannot be found";
                        mojo.getLog().error( msg);
                        throw new MavenReportException(msg);
                }

                try {
                        t = new MiniTemplator(new java.io.InputStreamReader(is));
                } catch (Exception e) {
                        final String msg = "error loading template";
                        mojo.getLog().error(msg,e);
                        throw new MavenReportException(msg, e);
                }
        }
        else {
                try {
                        t = new MiniTemplator(templateWiki);
                } catch (Exception e) {
                        final String msg = "error loading template";
                        mojo.getLog().error(msg,e);
                        throw new MavenReportException(msg, e);
                }

        }
   	
        mojo.addProperties(t);
        
    	String title = pluginDescriptor.getArtifactId() + "-" + pluginDescriptor.getVersion();
    	
    	Page page = ConfluenceUtils.getOrCreatePage(confluence, parentPage, title);

        {
            StringWriter writer = new StringWriter(100*1024);

            writeSummary( writer, pluginDescriptor, mojos );
            
            writer.flush();

            try {
                    t.setVariable("plugin.summary", writer.toString());
            } catch (VariableNotDefinedException e) {
            		mojo.getLog().warn( String.format( "variable %s not defined in template", "plugin.summary"));
            }

        }

        {
            StringWriter writer = new StringWriter(100*1024);

            writeGoals( writer, mojos );

            writer.flush();

            try {
                    t.setVariable("plugin.goals", writer.toString());
            } catch (VariableNotDefinedException e) {
            		mojo.getLog().warn( String.format( "variable %s not defined in template", "plugin.goals"));
            }

        }

        StringBuilder wiki = new StringBuilder()
                .append( "{info:title=").append("auto generated page").append('}')
                .append( "this page has been generated by plugin: " )
                .append( "[org.bsc.maven:maven-confluence-reporting-plugin|http://code.google.com/p/maven-confluence-plugin/]")
                .append("{info}")
                .append('\n')
                .append(t.generateOutput());

        page.setContent( wiki.toString() );
        
        confluence.storePage(page);

    }

	/**
	 * 
	 * @param writer
	 * @param mojoDescriptor
	 */
    private void writeSummary( Writer writer, PluginDescriptor pluginDescriptor, List<MojoDescriptor> mojos ) {
    	
    	ConfluenceWikiWriter w = new ConfluenceWikiWriter( writer );
    	
    	w.printBiggerHeading( "Description");
    	
    	String description = pluginDescriptor.getDescription();
    	
    	if( null!=description ) {
    		w.println( description );
    	}

    	w.printNewParagraph();
    	    	

    }

    private void writeGoals( Writer writer, List<MojoDescriptor> mojos ) {

        ConfluenceWikiWriter w = new ConfluenceWikiWriter( writer );

    	w.printBiggerHeading( "Plugin Goals");

    	for ( MojoDescriptor descriptor : mojos )
        {
    		w.appendBullet().printLinkToAnchor(descriptor.getGoal(), descriptor.getFullGoalName());
        }

    	w.printNewParagraph();

    	for ( MojoDescriptor descriptor : mojos )
        {
    		writeGoal(w, descriptor);
        }

    }
    /**
     * 
     * @param writer
     * @param mojoDescriptor
     */
    private void writeGoal( ConfluenceWikiWriter w, MojoDescriptor mojoDescriptor )
    {
        
    	w.appendBigHeading()
    		.appendAnchor(mojoDescriptor.getGoal(), mojoDescriptor.getFullGoalName())
    		.println();
    	
        String description = ( mojoDescriptor.getDescription() != null ) ?
        								mojoDescriptor.getDescription() :
        								"No description.";
        
        w.printQuote(description);

    	w.printNewParagraph();
        
        writeGoalAttributes( mojoDescriptor, w );

    	w.printNewParagraph();
        
        writeGoalParameterTable( mojoDescriptor, w );

    }

    /**
     * 
     * @param mojoDescriptor
     * @param w
     */
    private void writeGoalAttributes( MojoDescriptor mojoDescriptor, ConfluenceWikiWriter w )
    {
        w.printNormalHeading("Mojo Attributes");
        
        String value = mojoDescriptor.getDeprecated();
        
        if ( StringUtils.isNotEmpty( value ) ) {
        	w.printBullet("This plugin goal has been deprecated: " + value);
        }

        if ( mojoDescriptor.isProjectRequired() ) {
        	w.printBullet("Requires a Maven 2.0 project to execute.");
        }

        if ( mojoDescriptor.isAggregator() ) {
            w.printBullet( "Executes as an aggregator plugin." );
        }

        if ( mojoDescriptor.isDirectInvocationOnly() ) {
            w.printBullet( "Executes by direct invocation only." );
        }

        value = mojoDescriptor.isDependencyResolutionRequired();
        
        if ( StringUtils.isNotEmpty( value ) ) {
            w.printBullet( "Requires dependency resolution of artifacts in scope: <code>" + value + "</code>" );
        }

        value = mojoDescriptor.getSince();
        if ( StringUtils.isNotEmpty( value ) ) {
            w.printBullet( "Since version: <code>" + value + "</code>" );
        }

        value = mojoDescriptor.getPhase();
        if ( StringUtils.isNotEmpty( value ) )
        {
            w.printBullet( "Automatically executes within the lifecycle phase: <code>" + value + "</code>" );
        }

        value = mojoDescriptor.getExecutePhase();
        if ( StringUtils.isNotEmpty( value ) )
        {
            w.printBullet(
                "Invokes the execution of the lifecycle phase <code>" + value + "</code> prior to executing itself." );
        }

        value = mojoDescriptor.getExecuteGoal();
        if ( StringUtils.isNotEmpty( value ) )
        {
            w.printBullet(
                "Invokes the execution of this plugin's goal <code>" + value + "</code> prior to executing itself." );
        }

        value = mojoDescriptor.getExecuteLifecycle();
        if ( StringUtils.isNotEmpty( value ) )
        {
            w.printBullet( "Executes in its own lifecycle: <code>" + value + "</code>" );
        }

        if ( mojoDescriptor.isOnlineRequired() )
        {
            w.printBullet( "Requires that mvn runs in online mode." );
        }

        if ( !mojoDescriptor.isInheritedByDefault() )
        {
            w.printBullet( "Is NOT inherited by default in multi-project builds." );
        }
    }

    /**
     * 
     * @param mojoDescriptor
     * @param w
     */
    private void writeGoalParameterTable( MojoDescriptor mojoDescriptor, ConfluenceWikiWriter w )
    {
        List<Parameter> parameterList = mojoDescriptor.getParameters();

        //remove components and read-only parameters
        List<Parameter> list = filterParameters( parameterList );

        if ( list != null && list.size() > 0 )
        {
            writeParameterSummary( list, w );

            writeParameterDetails( list, w );
        }
    }

    /**
     * 
     * @param parameterList
     * @return
     */
    private List<Parameter> filterParameters( List<Parameter> parameterList )
    {
        List<Parameter> filtered = new ArrayList<Parameter>();

        if ( parameterList != null )
        {
            for ( Parameter parameter : parameterList ) {

                if ( parameter.isEditable() )
                {
                    String expression = parameter.getExpression();

                    if ( expression == null || !expression.startsWith( "${component." ) )
                    {
                        filtered.add( parameter );
                    }
                }
            }
        }

        return filtered;
    }

    /**
     * 
     * @param parameterList
     * @param w
     */
    private void writeParameterDetails( List<Parameter> parameterList, ConfluenceWikiWriter w )
    {
    	w.printNormalHeading("Parameter Details");
    	
    	w.printNewParagraph();

        for ( Parameter parameter : parameterList )
        {

        	w.printSmallHeading( parameter.getName() );
        	

            String description = parameter.getDescription();
            if ( StringUtils.isEmpty( description ) )
            {
                description = "No Description.";
            }
            
            w.println( decode(description) );
            
            
            writeDetail( "Deprecated", parameter.getDeprecated(), w );

            writeDetail( "Type", parameter.getType(), w );

            writeDetail( "Since", parameter.getSince(), w );

            if ( parameter.isRequired() )
            {
                writeDetail( "Required", "Yes", w );
            }
            else
            {
                writeDetail( "Required", "No", w );
            }

            writeDetail( "Expression", parameter.getExpression(), w );

            writeDetail( "Default", parameter.getDefaultValue(), w );

        }

    }

    private void writeDetail( String param, String value, ConfluenceWikiWriter w )
    {
        if ( StringUtils.isNotEmpty( value ) )
        {
        	w.printf("|%s|%s|\n", decode(param), decode(value) );
        }
    }

    /**
     * 
     * @param parameterList
     * @param w
     */
    private void writeParameterSummary( List<Parameter> parameterList, ConfluenceWikiWriter w )
    {
        List requiredParams = getParametersByRequired( true, parameterList );
        if ( requiredParams.size() > 0 )
        {
            writeParameterList( "Required Parameters", requiredParams, w );
        }

        List optionalParams = getParametersByRequired( false, parameterList );
        if ( optionalParams.size() > 0 )
        {
            writeParameterList( "Optional Parameters", optionalParams, w );
        }
    }

    /**
     * 
     * @param title
     * @param parameterList
     * @param w
     */
    private void writeParameterList( String title, List<Parameter> parameterList, ConfluenceWikiWriter w )
    {
    	w.printNormalHeading(title);
    	
    	w.printNewParagraph();
    	
    	w.printf( "||%s||%s||%s||\n", "Name", "Type", "Description");

        for ( Parameter parameter : parameterList ) {

            int index = parameter.getType().lastIndexOf( "." );

            w.print( '|' );
			
            w.print( parameter.getName() );

            w.print( '|' );
			
            w.print( parameter.getType().substring( index + 1 ) );
			
            w.print( '|' );

            String description = parameter.getDescription();
            if ( StringUtils.isEmpty( description ) )
            {
                description = "No description.";
            }
            if ( StringUtils.isNotEmpty( parameter.getDeprecated() ) )
            {
                description = "Deprecated. " + description;
            }
            
            w.print( decode(description.replace( '\n' , ' ')) );

            if ( StringUtils.isNotEmpty( parameter.getDefaultValue() ) )
            {
                w.printf( " Default value is %s" , decode(parameter.getDefaultValue()) );
            }
      
            w.println( '|' );
        }

    }

    /**
     * 
     * @param required
     * @param parameterList
     * @return
     */
    private List<Parameter> getParametersByRequired( boolean required, List<Parameter> parameterList )
    {
        List<Parameter> list = new ArrayList<Parameter>();

        for ( Iterator parameters = parameterList.iterator(); parameters.hasNext(); )
        {
            Parameter parameter = (Parameter) parameters.next();

            if ( parameter.isRequired() == required )
            {
                list.add( parameter );
            }
        }

        return list;
    }


}
