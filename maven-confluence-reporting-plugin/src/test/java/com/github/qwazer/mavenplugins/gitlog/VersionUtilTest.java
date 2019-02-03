package com.github.qwazer.mavenplugins.gitlog;

import static java.util.Arrays.asList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.junit.Test;


public class VersionUtilTest {


    @Test
    public void testCalculateVersionTagNamePartPatch() throws Exception {
        String version = "1.2.3";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.LATEST_RELEASE_VERSION);
        assertThat("1.2.2", equalTo(part));
    }

    @Test
    public void testCalculateVersionTagNamePartPatchSame() throws Exception {
        String version = "1.2.0";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.LATEST_RELEASE_VERSION);
        assertThat("1.2.0", equalTo(part));
    }

    @Test
    public void testCalculateVersionTagNamePartMinor() throws Exception {
        String version = "1.2.1";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.CURRENT_MINOR_VERSION);
        assertThat("1.2.0", equalTo(part));
    }

    @Test
    public void testCalculateVersionTagNamePartMinorZeroPatch() throws Exception {
        String version = "1.2.0";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.CURRENT_MINOR_VERSION);
        assertThat("1.2.0", equalTo(part));
    }

    @Test
    public void testCalculateVersionTagNamePartMajor() throws Exception {
        String version = "1.2.0";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.CURRENT_MAJOR_VERSION);
        assertThat("1.0.0", equalTo(part));
    }

    @Test
    public void testCalculateVersionTagNamePartMajorZero() throws Exception {
        String version = "2.0.0";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.CURRENT_MAJOR_VERSION);
        assertThat("2.0.0", equalTo(part));
    }


    @Test
    public void testParseArtifactVersionSimple() throws Exception {
        String version = "2.1.10";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(2, equalTo(artifactVersion.getMajorVersion()));
        assertThat(1, equalTo(artifactVersion.getMinorVersion()));
        assertThat(10, equalTo(artifactVersion.getIncrementalVersion()));

    }

    @Test
    public void testParseArtifactVersionSimple2() throws Exception {
        String version = "v25.1.9010";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(25, equalTo(artifactVersion.getMajorVersion()));
        assertThat(1, equalTo(artifactVersion.getMinorVersion()));
        assertThat(9010, equalTo(artifactVersion.getIncrementalVersion()));

    }

    @Test
    public void testParseArtifactVersionSimple3() throws Exception {
        String version = "saasa v254545.11.9010";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(254545, equalTo(artifactVersion.getMajorVersion()));
        assertThat(11, equalTo(artifactVersion.getMinorVersion()));
        assertThat(9010, equalTo(artifactVersion.getIncrementalVersion()));

    }

    @Test
    public void testParseArtifactVersionPrefix() throws Exception {
        String version = "asa-2.1.10";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(2, equalTo(artifactVersion.getMajorVersion()));
        assertThat(1, equalTo(artifactVersion.getMinorVersion()));
        assertThat(10, equalTo(artifactVersion.getIncrementalVersion()));

    }

    @Test
    public void testParseArtifactVersionPrefix2() throws Exception {
        String version = "V 2.1.10";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(2, equalTo((artifactVersion.getMajorVersion())));
        assertThat(1, equalTo((artifactVersion.getMinorVersion())));
        assertThat(10, equalTo((artifactVersion.getIncrementalVersion())));


    }

    @Test
    public void testParseArtifactVersionSuffix() throws Exception {
        String version = "2.1.10-V";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(2, equalTo((artifactVersion.getMajorVersion())));
        assertThat(1, equalTo((artifactVersion.getMinorVersion())));
        assertThat(10, equalTo((artifactVersion.getIncrementalVersion())));

    }

    @Test
    public void testParseArtifactVersionIncomplete() throws Exception {
        String version = "2.1";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(2, equalTo((artifactVersion.getMajorVersion())));
        assertThat(1, equalTo((artifactVersion.getMinorVersion())));
        assertThat(0, equalTo((artifactVersion.getIncrementalVersion())));

    }

    @Test
    public void testParseArtifactVersionSuffix2() throws Exception {
        String version = "2.1.10 v";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(2, equalTo((artifactVersion.getMajorVersion())));
        assertThat(1, equalTo((artifactVersion.getMinorVersion())));
        assertThat(10, equalTo((artifactVersion.getIncrementalVersion())));
    }

    @Test
    public void testParseArtifactVersionSuffix3() throws Exception {
        String version = "2.v";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(2, equalTo((artifactVersion.getMajorVersion())));
        assertThat(0, equalTo((artifactVersion.getMinorVersion())));
        assertThat(0, equalTo((artifactVersion.getIncrementalVersion())));
    }

    @Test
    public void testParseArtifactVersionSuffix4() throws Exception {
        String version = "25 version of progamm";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(25, equalTo((artifactVersion.getMajorVersion())));
        assertThat(0, equalTo((artifactVersion.getMinorVersion())));
        assertThat(0, equalTo((artifactVersion.getIncrementalVersion())));
    }
    @Test
    public void testParseArtifactVersionMix() throws Exception {
        String version = "this is version 25.9 of progamm";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertThat(25, equalTo((artifactVersion.getMajorVersion())));
        assertThat(9, equalTo((artifactVersion.getMinorVersion())));
        assertThat(0, equalTo((artifactVersion.getIncrementalVersion())));
    }

    @Test
    public void testAddSuffixIfNeeded() throws Exception {
        String s = VersionUtil.addSuffixDelimeterIfNeeded("1.2.3v");
        assertThat("1.2.3-v", equalTo(s));
    }

    @Test
    public void testAddSuffixIfNeeded2() throws Exception {
        String s = VersionUtil.addSuffixDelimeterIfNeeded("1.2.3.v");
        assertThat("1.2.3-v", equalTo((s)));
    }

    @Test
    public void testFindNearestVersionTagsBefore() throws Exception {

        List<String> list = asList("1.0.0", "1.1.0", "2.0.0");
        String version = "1.2.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("1.1.0", equalTo((foundVersion)));

    }

    @Test
    public void testFindNearestVersionTagsBeforeRC() throws Exception {

        List<String> list = asList("1.0.0-RC1", "1.0.0-RC2", "1.0.0-RC3");
        String version = "1.0.0-RC2";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("1.0.0-RC2", equalTo((foundVersion)));

    }

    @Test
    public void testFindNearestVersionTagsBeforeRC3() throws Exception {

        List<String> list = asList("1.0.0-RC1", "1.0.0-RC2", "1.0.0-RC4");
        String version = "1.0.0-RC3";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("1.0.0-RC2", equalTo((foundVersion)));

    }

    @Test
    public void testFindNearestVersionTagsBeforeMC1() throws Exception {

        List<String> list = asList("1.0.0-M01", "1.0.0-RC2", "1.0.0-RC4");
        String version = "1.0.0-RC3";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("1.0.0-RC2", equalTo((foundVersion)));

    }

    @Test
    public void testFindNearestVersionTagsBeforeMC2() throws Exception {

        List<String> list = asList("1.0.0-M01", "1.0.0-M2", "1.0.0-RC4");
        String version = "1.0.0-RC3";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("1.0.0-M2", equalTo((foundVersion)));

    }

    @Test
    public void testFindNearestVersionTagsBefore1() throws Exception {

        List<String> list = asList("1.0.0");
        String version = "1.2.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("1.0.0", equalTo((foundVersion)));

    }

    @Test
    public void testFindNearestVersionTagsBeforeNull() throws Exception {

        List<String> list = asList();
        String version = "1.2.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat(null, equalTo((foundVersion)));


    }

    @Test
    public void testFindNearestVersionTagsBefore12() throws Exception {

        List<String> list = asList("tagName");
        String version = "1.2.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("tagName", equalTo((foundVersion)));


    }

    @Test
    public void testFindNearestVersionTagsBeforeLeadingZero() throws Exception {

        List<String> list = asList("01.05", "01.03", "01.01");
        String version = "01.02";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("01.01", equalTo((foundVersion)));
    }


    @Test
    public void testFindNearestVersionTagsBeforeLeadingZeroPrefix() throws Exception {

        List<String> list = asList("v01.05", "v01.03", "v01.01");
        String version = "01.02";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("v01.01", equalTo((foundVersion)));
    }

    @Test
    public void testRemoveNonDigitPrefix() throws Exception {
        String s = "1.2.3";
        String r = VersionUtil.removeNonDigitPrefix(s);
        assertThat(s, equalTo((r)));
    }

    @Test
    public void testRemoveNonDigitPrefix2() throws Exception {
        String s = "abs-1.2.3";
        String r = VersionUtil.removeNonDigitPrefix(s);
        assertThat("1.2.3", equalTo((r)));
    }

    @Test
    public void testRemoveNonDigitPrefix3() throws Exception {
        String s = "";
        String r = VersionUtil.removeNonDigitPrefix(s);
        assertThat("", equalTo((r)));
    }

    @Test
    public void testFindNearestVersionTagsRealCase01() throws Exception {

        List<String> list = asList("7.17.1_deals_config_fix", "7.17.8", "7.18.0",
                "7.18.1", "7.18.2", "7.18.3", "build-6.2.14", "build-6.2.4", "build-6.2.5",
                "build-6.2.6", "build-6.2.7", "build-6.3", "build-6.4", "build-6.4.1",
                "build-6.6.29", "build-6.8", "build-7.6.122", "cummulative",
                "v7.18.4", "v7.19.0", "v7.19.1", "v7.19.2", "v7.19.3", "v7.19.4", "v7.19.5", "v7.20.0",
                "v7.20.1", "v7.20.2", "v7.20.3", "v7.20.4", "v7.20.5", "v7.20.6", "v8.0.0",
                "v8.0.1", "v8.0.2", "v8.0.3", "v8.0.4", "v8.0.5", "v8.0.6", "v8.0.7", "v8.0.9",
                "v8.1.1", "v8.1.2", "v8.1.3", "v8.2.0", "v8.2.1", "v8.2.2", "v8.2.3", "v8.2.4",
                "v8.2.5", "v8.3.0", "v8.3.1");
        String version = "8.1.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("v8.0.9", equalTo((foundVersion)));


    }

    @Test
    public void testFindNearestVersionTagsRealCase02() throws Exception {

        List<String> list = asList("7.17.1_deals_config_fix", "7.17.8", "7.18.0",
                "7.18.1", "7.18.2", "7.18.3", "build-6.2.14", "build-6.2.4", "build-6.2.5",
                "build-6.2.6", "build-6.2.7", "build-6.3", "build-6.4", "build-6.4.1",
                "build-6.6.29", "build-6.8", "build-7.6.122", "cummulative",
                "v7.18.4", "v7.19.0", "v7.19.1", "v7.19.2", "v7.19.3", "v7.19.4", "v7.19.5", "v7.20.0",
                "v7.20.1", "v7.20.2", "v7.20.3", "v7.20.4", "v7.20.5", "v7.20.6", "v8.0.0",
                "v8.0.1", "v8.0.2", "v8.0.3", "v8.0.4", "v8.0.5", "v8.0.6", "v8.0.7", "v8.0.9",
                "v8.1.1", "v8.1.2", "v8.1.3", "v8.2.0", "v8.2.1", "v8.2.2", "v8.2.3", "v8.2.4",
                "v8.2.5", "v8.3.0", "v8.3.1");
        String version = "8.2.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("v8.2.0", equalTo((foundVersion)));

    }

    @Test
    public void testFindNearestVersionTagsRealCase03() throws Exception {

        List<String> list = asList("1.0.0.M1", "1.0.0.M2", "1.0.0.RC1", "1.0.0.RC2",
                "1.0.0.RC3", "1.0.0.RC4", "1.0.0.RELEASE",
                "1.0.1.RELEASE", "1.0.2.RELEASE", "1.1.0.M2",
                "1.1.0.M3", "1.1.0.RC1", "1.1.0.RELEASE", "1.1.1.RELEASE",
                "1.1.2.RELEASE", "1.1.3.RELEASE", "1.1.4.RELEASE", "1.1.5.RELEASE",
                "1.2.0.M1", "1.2.0.RC1", "1.2.0.RELEASE", "1.2.1.RELEASE", "1.2.2.RELEASE",
                "1.2.3.RELEASE", "1.2.4.RELEASE", "1.2.5.RELEASE", "1.3.0.RELEASE", "1.3.1.RC1",
                "1.3.1.RC2", "1.3.1.RELEASE");
        String version = "1.3.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertThat("1.3.0.RELEASE", equalTo((foundVersion)));

    }

    @Test
    public void testSortAndFilter() throws Exception {
        List<String> list = asList("1.0.0.M1", "1.1.0.M2", "1.1.1.RELEASE","1.2.5.RELEASE" );
        LinkedList<String> linkedList =  VersionUtil.sortAndFilter(list, "1.1.0.M2", "1.1.1.RELEASE") ;
        assertThat(2, equalTo(linkedList.size()));
        assertThat(linkedList.contains("1.1.0.M2"), equalTo(true));
        assertThat(linkedList.contains("1.1.1.RELEASE"), equalTo(true));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSortAndFilterIllegal() throws Exception {
        List<String> list = asList("1.0.0.M1", "1.1.0.M2", "1.1.1.RELEASE","1.2.5.RELEASE" );
        /*LinkedList linkedList = */VersionUtil.sortAndFilter(list, "2.1.0.M2", "1.1.1.RELEASE") ;


    }
}