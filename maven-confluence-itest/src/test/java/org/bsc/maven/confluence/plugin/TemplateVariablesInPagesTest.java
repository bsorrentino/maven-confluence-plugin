package org.bsc.maven.confluence.plugin;

import static java.lang.String.format;
import static org.apache.maven.it.util.FileUtils.deleteDirectory;
import static org.apache.maven.it.util.FileUtils.fileExists;
import static org.apache.maven.it.util.FileUtils.fileRead;
import static org.apache.maven.it.util.FileUtils.fileWrite;
import static org.apache.maven.it.util.FileUtils.mkdir;
import static org.apache.maven.it.util.ResourceExtractor.simpleExtractResources;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.apache.xmlrpc.WebServer;
import org.codehaus.swizzle.confluence.Page;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class TemplateVariablesInPagesTest {

    public interface Handler {
        Map<String, String> getPage(String tokken, String space, String page);
        Map<String, Object> getServerInfo(String tokken);
        List<Map<String,String>> getChildren(String tokken, String parentId);
        String login(String username, String password);
        boolean logout(String tokken);
        Map<String, Object> storePage(String tokken, Hashtable<String, Object> page);
    }

    private static WebServer ws;
    private Handler handler;

    @BeforeClass
    public static void createWebServer() {
        ws = new WebServer( 19005 );
        ws.start();
    }

    @AfterClass
    public static void shutTheWebServerDown() {
        ws.shutdown();
    }

    @Before
    public void setUp() {
        ws.removeHandler( "confluence1" );
        handler = mock( Handler.class );
        ws.addHandler( "confluence1", handler );
        String tokken = "tokken";
        when(handler.login("admin", "password")).thenReturn(tokken);
        Map<String, Object> serverInfo = new Hashtable<String, Object>();
        serverInfo.put("baseUrl", "http://localhost:19005");
        when(handler.getServerInfo(tokken)).thenReturn(serverInfo);
        when(handler.logout(tokken)).thenReturn(true);

        when(handler.getChildren(tokken, "0")).thenReturn(new ArrayList<Map<String,String>>());
        String homePageName = "Hello Plugin";
        when(handler.getChildren(tokken, DigestUtils.md5Hex(homePageName))).thenReturn(new ArrayList<Map<String,String>>());

    }

    //@After
    public void teardown() throws IOException {
        String[] projects = new String[] {"simple-plugin-project"};
        for (String project : projects) {
            File file = ResourceExtractor.simpleExtractResources(getClass(), format("/%s/results", project));
            if (file != null) deleteDirectory(file);
        }
    }

    private Verifier testLaunchingMaven(File testBasedir, List<String> cliOptions, String... goals) throws IOException, VerificationException {
        final String results = testBasedir.getAbsolutePath() + "/results";
        mkdir(results);

        final HashMap<String, Map<String,String>> titleToPage = new HashMap<>();
        Map<String, String> homePage = new HashMap<String, String>();
        homePage.put("title", "Fake Root");
        homePage.put("id", "0");
        homePage.put("space", "DOC");
        titleToPage.put("Fake Root", homePage);

        when(handler.getPage(anyString(), anyString(), anyString())).then( ( invocationOnMock ) -> {

        		String title = (String)invocationOnMock.getArguments()[2];
                if (titleToPage.containsKey(title)) {
                    return titleToPage.get(title);
                }
                return Collections.emptyMap();
        });
        
        
        
        when(handler.storePage(anyString(), any(Hashtable.class))).then( ( invocationOnMock ) -> {
                Map<String, String> hashtable = new HashMap<>();
                Map<String, String> pageMap = (Map<String, String>)invocationOnMock.getArguments()[1];
                hashtable.putAll(pageMap);
                Page page = new Page(pageMap);

                String parentId = page.getParentId();
                if (parentId != null) {
                    String parentTitle = null;
                    
                    for (Map<String,String> map : titleToPage.values()) {
                        Page p = new Page(map);
                        if (parentId.equals(p.getId())) {
                            parentTitle = p.getTitle();
                            break;
                        }
                    }
                    if (parentTitle == null) {
                        throw new IllegalStateException(format("pageId '%s' not found", parentId));
                    }

                    fileWrite(results + "/" + parentTitle + "=>" + page.getTitle(), page.getContent());
                } else {
                    fileWrite(results + "/" + page.getTitle(), page.getContent());
                }

                hashtable.put("id",DigestUtils.md5Hex(page.getTitle()));
                hashtable.put("space", "DOC");
                titleToPage.put(page.getTitle(), hashtable);
                return hashtable;
            });
      

        when(handler.getChildren(anyString(), anyString())).then(( invocationOnMock ) -> {

        		String parentPageId = (String)invocationOnMock.getArguments()[1];
        		
                List<Map<String,String>> children = new ArrayList<>();
                
                for (Map<String,String> pageMap : titleToPage.values()) {
                    Page page = new Page(pageMap);
                    if (parentPageId.equals(page.getParentId())) {
                        children.add(pageMap);
                    }
                }
                return children;
            });

        Verifier verifier = new Verifier(testBasedir.getAbsolutePath());
        verifier.deleteArtifact("sample.plugin", "hello-maven-plugin", "1.0-SNAPSHOT", "jar");

        File settings = simpleExtractResources(TemplateVariablesInPagesTest.class, "/settings.xml");
        cliOptions.add("-s");
        cliOptions.add(settings.getAbsolutePath());
        verifier.setCliOptions(cliOptions);
        verifier.executeGoals(Arrays.asList(goals));
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
        verifier.setDebug(true);
        return verifier;
    }

    @Test
    public void shouldRenderTheIndexPage() throws IOException, VerificationException, InterruptedException {
        File testDir = simpleExtractResources(getClass(), "/simple-plugin-project");
        testLaunchingMaven(testDir, Collections.emptyList(), "clean", "package", "confluence-reporting:deploy");
        assertTrue(fileExists(testDir.getAbsolutePath() + "/results/Fake Root=>Hello Plugin"));
        assertTrue(fileExists(testDir.getAbsolutePath() + "/results/Hello Plugin=>Hello Plugin - Summary"));

        String pluginGoalsPath = testDir.getAbsolutePath() + "/results/Hello Plugin=>Hello Plugin - Goals";
        assertTrue(fileExists(pluginGoalsPath));
        assertThat(fileRead(pluginGoalsPath), not(containsString("${plugin.goals}")));

        String pluginsSummaryPath = testDir.getAbsolutePath() + "/results/Hello Plugin=>Hello Plugin - PluginsSummary";
        assertTrue(fileExists(pluginsSummaryPath));
        String pluginsSummary = fileRead(pluginsSummaryPath);
        assertThat(pluginsSummary, not(containsString("${plugin.summary}")));
    }

    @Test
    public void shouldPutTheGoalsAsChildrenOfGoalsPage() throws IOException, VerificationException, InterruptedException {
        File testDir = simpleExtractResources(getClass(), "/plugin-project-goals-in-subpage");
        testLaunchingMaven(testDir, Collections.emptyList(), "clean", "package", "confluence-reporting:deploy");
        assertTrue(fileExists(testDir.getAbsolutePath() + "/results/Fake Root=>Hello Plugin"));
        String pluginGoalsPath = testDir.getAbsolutePath() + "/results/Hello Plugin=>Hello Plugin - Goals";
        assertTrue(fileExists(pluginGoalsPath));

        String pluginGoalhelpPath = testDir.getAbsolutePath() + "/results/Hello Plugin - Goals=>Hello Plugin - help";
        assertTrue("help goal should be a subpage of Goals", fileExists(pluginGoalhelpPath));
        String pluginGoaltouchPath = testDir.getAbsolutePath() + "/results/Hello Plugin - Goals=>Hello Plugin - touch";
        assertTrue("touch goal should be a subpage of Goals", fileExists(pluginGoaltouchPath));

        assertFalse("help goal should not be under the home page",
                fileExists(testDir.getAbsolutePath() + "/results/Hello Plugin=>Hello Plugin - help"));
        assertFalse("touch goal should not be under the home page",
                fileExists(testDir.getAbsolutePath() + "/results/Hello Plugin=>Hello Plugin - touch"));
    }
}
