package org.bsc.maven.confluence.plugin;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpc;
import org.codehaus.swizzle.confluence.Page;
import org.junit.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.String.format;
import static org.apache.maven.it.util.FileUtils.*;
import static org.apache.maven.it.util.ResourceExtractor.simpleExtractResources;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class TemplateVariablesInPagesTest {

    @SuppressWarnings("WeakerAccess")
    public interface Handler {
        Map<String, String> getPage(String tokken, String space, String page);
        Map<String, Object> getServerInfo(String tokken);
        List<?> getChildren(String tokken, String parentId);
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

        when(handler.getChildren(tokken, "0")).thenReturn(new Vector());
        String homePageName = "Hello Plugin";
        when(handler.getChildren(tokken, DigestUtils.md5Hex(homePageName))).thenReturn(new Vector());

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

        final HashMap<String, Map> titleToPage = new HashMap<String, Map>();
        Hashtable<String, String> homePage = new Hashtable<String, String>();
        homePage.put("title", "Fake Root");
        homePage.put("id", "0");
        homePage.put("space", "DOC");
        titleToPage.put("Fake Root", homePage);

        when(handler.getPage(anyString(), anyString(), anyString())).then(new Answer<Hashtable>() {
            @Override
            public Hashtable answer(InvocationOnMock invocationOnMock) throws Throwable {
                String title = (String)invocationOnMock.getArguments()[2];
                if (titleToPage.containsKey(title)) {
                    return (Hashtable) titleToPage.get(title);
                }
                return new Hashtable();
            }
        });

        when(handler.storePage(anyString(), any(Hashtable.class))).then(new Answer<Map<String,Object>>() {
            @Override
            public Map<String, Object> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
                Map<String, String> pageMap = (Map) invocationOnMock.getArguments()[1];
                hashtable.putAll(pageMap);
                Page page = new Page(pageMap);
                fileWrite(results + "/" + page.getTitle(), page.getContent());
                hashtable.put("id",DigestUtils.md5Hex(page.getTitle()));
                hashtable.put("space", "DOC");
                titleToPage.put(page.getTitle(), hashtable);
                return hashtable;
            }
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
        testLaunchingMaven(testDir, new ArrayList<String>() {}, "clean", "package", "confluence-reporting:deploy");
        assertTrue(fileExists(testDir.getAbsolutePath() + "/results/Hello Plugin"));
        assertTrue(fileExists(testDir.getAbsolutePath() + "/results/Hello Plugin - Summary"));

        String pluginGoalsPath = testDir.getAbsolutePath() + "/results/Hello Plugin - Goals";
        assertTrue(fileExists(pluginGoalsPath));
        assertThat(fileRead(pluginGoalsPath), not(containsString("${plugin.goals}")));

        String pluginsSummaryPath = testDir.getAbsolutePath() + "/results/Hello Plugin - PluginsSummary";
        assertTrue(fileExists(pluginsSummaryPath));
        String pluginsSummary = fileRead(pluginsSummaryPath);
        assertThat(pluginsSummary, not(containsString("${plugin.summary}")));
    }
}
