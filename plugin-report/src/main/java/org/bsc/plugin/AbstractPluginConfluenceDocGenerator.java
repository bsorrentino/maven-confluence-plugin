package org.bsc.plugin;

import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.Parameter;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.tools.plugin.PluginToolsRequest;
import org.apache.maven.tools.plugin.generator.Generator;
import org.apache.maven.tools.plugin.generator.GeneratorException;
import org.bsc.confluence.ConfluenceService;
import org.bsc.confluence.ConfluenceService.Model;
import org.bsc.confluence.ConfluenceService.Storage;
import org.bsc.confluence.ConfluenceService.Storage.Representation;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static org.bsc.confluence.ConfluenceHtmlUtils.replaceHTML;
import static org.bsc.plugin.ConfluenceWikiWriter.createAnchor;
import static org.bsc.plugin.ConfluenceWikiWriter.createLinkToAnchor;

/**
 *
 * @author Sorrentino
 *
 */
public abstract class AbstractPluginConfluenceDocGenerator implements Generator {

    public static final String PLUGIN_SUMMARY_VAR           = "plugin.summary";
    public static final String PLUGIN_GOALS_VAR             = "plugin.goals";

    public static final String DEFAULT_PLUGIN_TEMPLATE_WIKI = "defaultPluginTemplate.confluence";

    public class Goal {

        public final MojoDescriptor descriptor;

        public Goal(MojoDescriptor mojoDescriptor) {
            this.descriptor = mojoDescriptor;
        }

        public void write(ConfluenceWikiWriter w) {

            w.appendBigHeading()
                    .appendAnchor(descriptor.getGoal(), descriptor.getFullGoalName())
                    .println();

            String description = (descriptor.getDescription() != null)
                    ? replaceHTML(descriptor.getDescription())
                    : "No description.";

            w.printQuote(description);

            w.printNewParagraph();

            writeAttributes( w);

            w.printNewParagraph();

            writeParameterTable( w);

        }

        public String getPageName(String parentName ) {
            return String.format( "%s - %s", parentName, descriptor.getGoal());
        }
        public Model.Page generatePage( ConfluenceService confluence,  Model.Page parent, String parentName ) throws Exception {
            
            final String goalName = getPageName( parentName );

            final CompletableFuture<Model.Page> result = 
                confluence.getOrCreatePage(parent.getSpace(), parent.getTitle(), goalName)
                .thenCompose( page -> {
                    final StringWriter writer = new StringWriter(100 * 1024);
    
                    ConfluenceWikiWriter w = new ConfluenceWikiWriter(writer);
    
                    write( w );
    
                    writer.flush();
    
                    return confluence.storePage(page, Storage.of(writer.toString(), Representation.WIKI));
                    
                })
            ;

            return result.get();
                
        }
        
        /**
         *
         * @param w ConfluenceWikiWriter
         */
        private void writeAttributes(ConfluenceWikiWriter w) {
            w.printNormalHeading("Mojo Attributes");

            String value = descriptor.getDeprecated();

            if (StringUtils.isNotEmpty(value)) {
                w.printBullet("This plugin goal has been deprecated: " + value);
            }

            if (descriptor.isProjectRequired()) {
                w.printBullet("Requires a Maven 2.0 project to execute.");
            }

            if (descriptor.isAggregator()) {
                w.printBullet("Executes as an aggregator plugin.");
            }

            if (descriptor.isDirectInvocationOnly()) {
                w.printBullet("Executes by direct invocation only.");
            }

            value = descriptor.getDependencyResolutionRequired();

            if (StringUtils.isNotEmpty(value)) {
                w.printBullet("Requires dependency resolution of artifacts in scope: {{" + value + "}}");
            }

            value = descriptor.getSince();
            if (StringUtils.isNotEmpty(value)) {
                w.printBullet("Since version: {{" + value + "}}");
            }

            value = descriptor.getPhase();
            if (StringUtils.isNotEmpty(value)) {
                w.printBullet("Automatically executes within the lifecycle phase: {{" + value + "}}");
            }

            value = descriptor.getExecutePhase();
            if (StringUtils.isNotEmpty(value)) {
                w.printBullet(
                        "Invokes the execution of the lifecycle phase {{" + value + "}} prior to executing itself.");
            }

            value = descriptor.getExecuteGoal();
            if (StringUtils.isNotEmpty(value)) {
                w.printBullet(
                        "Invokes the execution of this plugin's goal {{" + value + "}} prior to executing itself.");
            }

            value = descriptor.getExecuteLifecycle();
            if (StringUtils.isNotEmpty(value)) {
                w.printBullet("Executes in its own lifecycle: {{" + value + "}}");
            }

            if (descriptor.isOnlineRequired()) {
                w.printBullet("Requires that mvn runs in online mode.");
            }

            if (!descriptor.isInheritedByDefault()) {
                w.printBullet("Is NOT inherited by default in multi-project builds.");
            }
        }

        /**
         *
         * @param w ConfluenceWikiWriter
         */
        private void writeParameterTable(ConfluenceWikiWriter w) {
            List<Parameter> parameterList = descriptor.getParameters();

            //remove components and read-only parameters
            List<Parameter> list = filterParameters(parameterList);

            if (list != null && list.size() > 0) {
                writeParameterSummary(list, w);

                writeParameterDetails(list, w);
            }
        }

    };

    public abstract org.apache.maven.plugin.logging.Log getLog();


    final java.util.List<Goal> goals = new ArrayList<>();

    public  java.util.List<Goal> getGoals() {
        return goals;
    }

    public void generateGoalsPages(final ConfluenceService confluence,
                                   final Model.Page confluenceHome,
                                   final Map<String, Model.Page> varsToParentPageMap) {

        // GENERATE GOAL
        getLog().info(format("Get the right page to generate the %s pages under", PLUGIN_GOALS_VAR));

        Model.Page goalsParentPage = confluenceHome;

        if (varsToParentPageMap.containsKey(PLUGIN_GOALS_VAR)) {
            goalsParentPage = varsToParentPageMap.get(PLUGIN_GOALS_VAR);
        }

        getLog().info(format("Plugin Goals parentPage is: %s", goalsParentPage.getTitle()));

        for (Goal goal : goals) {
            try {
                getLog().info(format("- generating: %s", goal.getPageName(confluenceHome.getTitle()) ));
                var page = goal.generatePage(confluence, goalsParentPage, confluenceHome.getTitle());
                getLog().debug(format("page generated: %s", page.getTitle()));

            } catch (Exception ex) {
                getLog().warn(format("error generating page for goal [%s]", goal.descriptor.getGoal()), ex);
            }
        }

    }

    /**
     * 
     * @param destinationDirectory
     * @param pluginDescriptor
     * @throws IOException 
     */
    public void execute(File destinationDirectory, PluginDescriptor pluginDescriptor) throws IOException {
        
        throw new UnsupportedOperationException( "execute is not supported!");
    }

    /**
     * 
     * @param destinationDirectory
     * @param request
     * @throws GeneratorException 
     */
    @Override
    public void execute(File destinationDirectory, PluginToolsRequest request) throws GeneratorException {

        throw new UnsupportedOperationException( "execute is not supported!");

    }

    /**
     * 
     * @param writer Writer
     * @param pluginDescriptor Plugin Descriptor
     */
    protected void writeSummary(Writer writer, PluginDescriptor pluginDescriptor) {

        try( final ConfluenceWikiWriter w = new ConfluenceWikiWriter(writer) ) {

            w.printBiggerHeading("Description");
    
            Optional.ofNullable(pluginDescriptor.getDescription())
                .ifPresent(w::println);
    
            w.printNewParagraph();
        }

    }
    
    protected java.util.List<Goal> writeGoalsAsChildren( Writer writer, String parentName, List<MojoDescriptor> mojos ) {

        final java.util.List<Goal> result = new java.util.ArrayList<Goal>(mojos.size());
        
        try(final ConfluenceWikiWriter w = new ConfluenceWikiWriter(writer)) {

            w.printBiggestHeading("Plugin Goals");
    
            w.println("|| Name || Description ||");
            
            for (MojoDescriptor descriptor : mojos) {
                final Goal goal = new Goal(descriptor);
                
                w.print( '|' );
                w.printf( "[%s|%s]",goal.descriptor.getFullGoalName(),
                                      goal.getPageName(parentName) );		
                w.print('|');
                w.print(replaceHTML(goal.descriptor.getDescription()));
                w.println('|');
                
                result.add(goal);
                
                
            }
    
            w.printNewParagraph();
        }
        return result;
    }
    
    
    @SuppressWarnings("unused")
    private void writeGoals(Writer writer, List<MojoDescriptor> mojos) {

        ConfluenceWikiWriter w = new ConfluenceWikiWriter(writer);

        w.printBiggerHeading("Plugin Goals");

        for (MojoDescriptor descriptor : mojos) {
            w.appendBullet().printLinkToAnchor(descriptor.getGoal(), descriptor.getFullGoalName());
        }

        w.printNewParagraph();

        for (MojoDescriptor descriptor : mojos) {
            final Goal goal = new Goal(descriptor);
            goal.write(w);
        }

    }


    private List<Parameter> filterParameters(List<Parameter> parameterList) {
        List<Parameter> filtered = new ArrayList<Parameter>();

        if (parameterList != null) {
            for (Parameter parameter : parameterList) {

                if (parameter.isEditable()) {
                    String expression = parameter.getExpression();

                    if (expression == null || !expression.startsWith("${component.")) {
                        filtered.add(parameter);
                    }
                }
            }
        }

        return filtered;
    }

    /**
     *
     * @param parameterList list of parameters
     * @param w writer
     */
    private void writeParameterDetails(List<Parameter> parameterList, ConfluenceWikiWriter w) {
        w.printNormalHeading("Parameter Details");

        w.printNewParagraph();

        for (Parameter parameter : parameterList) {

            w.printSmallHeading(createAnchor(parameter.getName(), parameter.getName()));

            String description = parameter.getDescription();
            if (StringUtils.isEmpty(description)) {
                description = "No Description.";
            }

            w.println(replaceHTML(description));

            writeDetail("Deprecated", parameter.getDeprecated(), w);

            writeDetail("Type", parameter.getType(), w);

            writeDetail("Since", parameter.getSince(), w);

            if (parameter.isRequired()) {
                writeDetail("Required", "Yes", w);
            } else {
                writeDetail("Required", "No", w);
            }

            writeDetail("Expression", parameter.getExpression(), w);

            writeDetail("Default", parameter.getDefaultValue(), w);

        }

    }

    private void writeDetail(String param, String value, ConfluenceWikiWriter w) {
        if (StringUtils.isNotEmpty(value)) {
            w.printf("||%s|%s|\n", replaceHTML(param), replaceHTML(value));
        }
    }

    /**
     *
     * @param parameterList list of parameters
     * @param w writer
     */
    private void writeParameterSummary(List<Parameter> parameterList, ConfluenceWikiWriter w) {
        List<Parameter>  requiredParams = getParametersByRequired(true, parameterList);
        if (!requiredParams.isEmpty()) {
            writeParameterList("Required Parameters", requiredParams, w);
        }

        List<Parameter> optionalParams = getParametersByRequired(false, parameterList);
        if (!optionalParams.isEmpty()) {
            writeParameterList("Optional Parameters", optionalParams, w);
        }
    }

    /**
     *
     * @param title title
     * @param parameterList list of parameters
     * @param w writer
     */
    private void writeParameterList(String title, List<Parameter> parameterList, ConfluenceWikiWriter w) {
        w.printNormalHeading(title);

        w.printNewParagraph();

        w.printf("||%s||%s||%s||\n", "Name", "Type", "Description");

        for (Parameter parameter : parameterList) {

            int index = parameter.getType().lastIndexOf(".");

            w.print('|');

            w.print(createLinkToAnchor(parameter.getName(), parameter.getName()));

            w.print('|');

            w.print(parameter.getType().substring(index + 1));

            w.print('|');

            String description = parameter.getDescription();
            if (StringUtils.isEmpty(description)) {
                description = "No description.";
            }
            if (StringUtils.isNotEmpty(parameter.getDeprecated())) {
                description = "Deprecated. " + description;
            }

          
            w.print(replaceHTML(description).replace("\n\n", "\n"));

            if (StringUtils.isNotEmpty(parameter.getDefaultValue())) {
                w.printf(" Default value is %s", replaceHTML(parameter.getDefaultValue()));
            }

            w.println('|');
        }

    }

    /**
     *
     * @param required  true if required, false if optional
     * @param parameterList list of parameters
     * @return list of parameters
     */
    private List<Parameter> getParametersByRequired(boolean required, List<Parameter> parameterList) {
        if( parameterList == null) 
            return Collections.emptyList();
        
        final List<Parameter> list = new ArrayList<>();

        for (Parameter parameter : parameterList) {
            if (parameter.isRequired() == required) {
                list.add(parameter);
            }
        }

        return list;
    }

}
