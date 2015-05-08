package com.github.qwazer.mavenplugins.gitlog;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;


public class VersionUtilTest {


    @Test
    public void testCalculateVersionTagNamePartPatch() throws Exception {
        String version = "1.2.3";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.SINCE_PREVIOUS_PATCH_VERSION);
        assertEquals("1.2.2", part);
    }

    @Test
    public void testCalculateVersionTagNamePartPatchSame() throws Exception {
        String version = "1.2.0";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.SINCE_PREVIOUS_PATCH_VERSION);
        assertEquals("1.2.0", part);
    }

    @Test
    public void testCalculateVersionTagNamePartMinor() throws Exception {
        String version = "1.2.1";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.SINCE_CURRENT_MINOR_VERSION);
        assertEquals("1.2.0", part);
    }

    @Test
    public void testCalculateVersionTagNamePartMinorZeroPatch() throws Exception {
        String version = "1.2.0";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.SINCE_CURRENT_MINOR_VERSION);
        assertEquals("1.2.0", part);
    }

    @Test
    public void testCalculateVersionTagNamePartMajor() throws Exception {
        String version = "1.2.0";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.SINCE_CURRENT_MAJOR_VERSION);
        assertEquals("1.0.0", part);
    }

    @Test
    public void testCalculateVersionTagNamePartMajorZero() throws Exception {
        String version = "2.0.0";
        String part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.SINCE_CURRENT_MAJOR_VERSION);
        assertEquals("2.0.0", part);
    }


    @Test
    public void testParseArtifactVersionSimple() throws Exception {
        String version = "2.1.10";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(2, artifactVersion.getMajorVersion());
        assertEquals(1, artifactVersion.getMinorVersion());
        assertEquals(10, artifactVersion.getIncrementalVersion());

    }

    @Test
    public void testParseArtifactVersionSimple2() throws Exception {
        String version = "v25.1.9010";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(25, artifactVersion.getMajorVersion());
        assertEquals(1, artifactVersion.getMinorVersion());
        assertEquals(9010, artifactVersion.getIncrementalVersion());

    }

    public void testParseArtifactVersionSimple3() throws Exception {
        String version = "saasa v254545.11.9010";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(254545, artifactVersion.getMajorVersion());
        assertEquals(11, artifactVersion.getMinorVersion());
        assertEquals(9010, artifactVersion.getIncrementalVersion());

    }

    @Test
    public void testParseArtifactVersionPrefix() throws Exception {
        String version = "asa-2.1.10";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(2, artifactVersion.getMajorVersion());
        assertEquals(1, artifactVersion.getMinorVersion());
        assertEquals(10, artifactVersion.getIncrementalVersion());

    }

    @Test
    public void testParseArtifactVersionPrefix2() throws Exception {
        String version = "V 2.1.10";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(2, artifactVersion.getMajorVersion());
        assertEquals(1, artifactVersion.getMinorVersion());
        assertEquals(10, artifactVersion.getIncrementalVersion());


    }

    @Test
    public void testParseArtifactVersionSuffix() throws Exception {
        String version = "2.1.10-V";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(2, artifactVersion.getMajorVersion());
        assertEquals(1, artifactVersion.getMinorVersion());
        assertEquals(10, artifactVersion.getIncrementalVersion());

    }

    @Test
    public void testParseArtifactVersionIncomplete() throws Exception {
        String version = "2.1";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(2, artifactVersion.getMajorVersion());
        assertEquals(1, artifactVersion.getMinorVersion());
        assertEquals(0, artifactVersion.getIncrementalVersion());

    }

    @Test
    public void testParseArtifactVersionSuffix2() throws Exception {
        String version = "2.1.10 v";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(2, artifactVersion.getMajorVersion());
        assertEquals(1, artifactVersion.getMinorVersion());
        assertEquals(10, artifactVersion.getIncrementalVersion());
    }

    @Test
    public void testParseArtifactVersionSuffix3() throws Exception {
        String version = "2.v";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(2, artifactVersion.getMajorVersion());
        assertEquals(0, artifactVersion.getMinorVersion());
        assertEquals(0, artifactVersion.getIncrementalVersion());
    }

    @Test
    public void testParseArtifactVersionSuffix4() throws Exception {
        String version = "25 version of progamm";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(25, artifactVersion.getMajorVersion());
        assertEquals(0, artifactVersion.getMinorVersion());
        assertEquals(0, artifactVersion.getIncrementalVersion());
    }
    @Test
    public void testParseArtifactVersionMix() throws Exception {
        String version = "this is version 25.9 of progamm";
        ArtifactVersion artifactVersion = VersionUtil.parseArtifactVersion(version);
        assertEquals(25, artifactVersion.getMajorVersion());
        assertEquals(9, artifactVersion.getMinorVersion());
        assertEquals(0, artifactVersion.getIncrementalVersion());
    }

    @Test
    public void testAddSuffixIfNeeded() throws Exception {
        String s = VersionUtil.addSuffixDelimeterIfNeeded("1.2.3v");
        assertEquals("1.2.3-v", s);
    }

    @Test
    public void testAddSuffixIfNeeded2() throws Exception {
        String s = VersionUtil.addSuffixDelimeterIfNeeded("1.2.3.v");
        assertEquals("1.2.3-v", s);
    }

    @Test
    public void testFindNearestVersionTagsBefore() throws Exception {

        List<String> list = asList("1.0.0", "1.1.0", "2.0.0");
        String version = "1.2.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertEquals("1.1.0", foundVersion);

    }

    @Test
    public void testFindNearestVersionTagsBeforeRC() throws Exception {

        List<String> list = asList("1.0.0-RC1", "1.0.0-RC2", "1.0.0-RC3");
        String version = "1.0.0-RC2";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertEquals("1.0.0-RC2", foundVersion);

    }

    @Test
    public void testFindNearestVersionTagsBeforeRC3() throws Exception {

        List<String> list = asList("1.0.0-RC1", "1.0.0-RC2", "1.0.0-RC4");
        String version = "1.0.0-RC3";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertEquals("1.0.0-RC2", foundVersion);

    }

    @Test
    public void testFindNearestVersionTagsBeforeMC1() throws Exception {

        List<String> list = asList("1.0.0-M01", "1.0.0-RC2", "1.0.0-RC4");
        String version = "1.0.0-RC3";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertEquals("1.0.0-RC2", foundVersion);

    }

    @Test
    public void testFindNearestVersionTagsBeforeMC2() throws Exception {

        List<String> list = asList("1.0.0-M01", "1.0.0-M2", "1.0.0-RC4");
        String version = "1.0.0-RC3";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertEquals("1.0.0-M2", foundVersion);

    }

    @Test
    public void testFindNearestVersionTagsBefore1() throws Exception {

        List<String> list = asList("1.0.0");
        String version = "1.2.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertEquals("1.0.0", foundVersion);

    }

    @Test
    public void testFindNearestVersionTagsBeforeNull() throws Exception {

        List<String> list = asList();
        String version = "1.2.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertEquals(null, foundVersion);


    }

    @Test
    public void testFindNearestVersionTagsBefore12() throws Exception {

        List<String> list = asList("tagName");
        String version = "1.2.0";

        String foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version);
        assertEquals("tagName", foundVersion);


    }

    @Test
    public void testRemoveNonDigitPrefix() throws Exception {
        String s = "1.2.3";
        String r = VersionUtil.removeNonDigitPrefix(s);
        assertEquals(s, r);
    }

    @Test
    public void testRemoveNonDigitPrefix2() throws Exception {
        String s = "abs-1.2.3";
        String r = VersionUtil.removeNonDigitPrefix(s);
        assertEquals("1.2.3", r);
    }

    @Test
    public void testRemoveNonDigitPrefix3() throws Exception {
        String s = "";
        String r = VersionUtil.removeNonDigitPrefix(s);
        assertEquals("", r);
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
        assertEquals("v8.0.9", foundVersion);


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
        assertEquals("v8.2.0", foundVersion);

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
        assertEquals("1.3.0.RELEASE", foundVersion);

    }


}