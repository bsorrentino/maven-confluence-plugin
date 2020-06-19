package com.github.qwazer.mavenplugins.gitlog

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class VersionUtilTest {
    @Test
    @Throws(Exception::class)
    fun testCalculateVersionTagNamePartPatch() {
        val version = "1.2.3"
        val part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.LATEST_RELEASE_VERSION)
        assertEquals(part, "1.2.2")
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateVersionTagNamePartPatchSame() {
        val version = "1.2.0"
        val part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.LATEST_RELEASE_VERSION)
        assertEquals(part, "1.2.0")
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateVersionTagNamePartMinor() {
        val version = "1.2.1"
        val part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.CURRENT_MINOR_VERSION)
        assertEquals(part, "1.2.0")
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateVersionTagNamePartMinorZeroPatch() {
        val version = "1.2.0"
        val part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.CURRENT_MINOR_VERSION)
        assertEquals(part, "1.2.0")
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateVersionTagNamePartMajor() {
        val version = "1.2.0"
        val part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.CURRENT_MAJOR_VERSION)
        assertEquals(part, "1.0.0")
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateVersionTagNamePartMajorZero() {
        val version = "2.0.0"
        val part = VersionUtil.calculateVersionTagNamePart(version, CalculateRuleForSinceTagName.CURRENT_MAJOR_VERSION)
        assertEquals(part, "2.0.0")
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionSimple() {
        val version = "2.1.10"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(2, artifactVersion.majorVersion)
        assertEquals(1, artifactVersion.minorVersion)
        assertEquals(10, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionSimple2() {
        val version = "v25.1.9010"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(25, artifactVersion.majorVersion)
        assertEquals(1, artifactVersion.minorVersion)
        assertEquals(9010, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionSimple3() {
        val version = "saasa v254545.11.9010"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(254545, artifactVersion.majorVersion)
        assertEquals(11, artifactVersion.minorVersion)
        assertEquals(9010, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionPrefix() {
        val version = "asa-2.1.10"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(2, artifactVersion.majorVersion)
        assertEquals(1, artifactVersion.minorVersion)
        assertEquals(10, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionPrefix2() {
        val version = "V 2.1.10"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(2, artifactVersion.majorVersion)
        assertEquals(1, artifactVersion.minorVersion)
        assertEquals(10, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionSuffix() {
        val version = "2.1.10-V"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(2, artifactVersion.majorVersion)
        assertEquals(1, artifactVersion.minorVersion)
        assertEquals(10, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionIncomplete() {
        val version = "2.1"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(2, artifactVersion.majorVersion)
        assertEquals(1, artifactVersion.minorVersion)
        assertEquals(0, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionSuffix2() {
        val version = "2.1.10 v"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(2, artifactVersion.majorVersion)
        assertEquals(1, artifactVersion.minorVersion)
        assertEquals(10, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionSuffix3() {
        val version = "2.v"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(2, artifactVersion.majorVersion)
        assertEquals(0, artifactVersion.minorVersion)
        assertEquals(0, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionSuffix4() {
        val version = "25 version of progamm"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(25, artifactVersion.majorVersion)
        assertEquals(0, artifactVersion.minorVersion)
        assertEquals(0, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testParseArtifactVersionMix() {
        val version = "this is version 25.9 of progamm"
        val artifactVersion = VersionUtil.parseArtifactVersion(version)
        assertEquals(25, artifactVersion.majorVersion)
        assertEquals(9, artifactVersion.minorVersion)
        assertEquals(0, artifactVersion.incrementalVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testAddSuffixIfNeeded() {
        val s = VersionUtil.addSuffixDelimeterIfNeeded("1.2.3v")
        assertEquals("1.2.3-v", s)
    }

    @Test
    @Throws(Exception::class)
    fun testAddSuffixIfNeeded2() {
        val s = VersionUtil.addSuffixDelimeterIfNeeded("1.2.3.v")
        assertEquals("1.2.3-v", s)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsBefore() {
        val list = listOf("1.0.0", "1.1.0", "2.0.0")
        val version = "1.2.0"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("1.1.0", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsBeforeRC() {
        val list = listOf("1.0.0-RC1", "1.0.0-RC2", "1.0.0-RC3")
        val version = "1.0.0-RC2"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("1.0.0-RC2", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsBeforeRC3() {
        val list = listOf("1.0.0-RC1", "1.0.0-RC2", "1.0.0-RC4")
        val version = "1.0.0-RC3"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("1.0.0-RC2", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsBeforeMC1() {
        val list = listOf("1.0.0-M01", "1.0.0-RC2", "1.0.0-RC4")
        val version = "1.0.0-RC3"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("1.0.0-RC2", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsBeforeMC2() {
        val list = listOf("1.0.0-M01", "1.0.0-M2", "1.0.0-RC4")
        val version = "1.0.0-RC3"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("1.0.0-M2", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsBefore1() {
        val list = listOf("1.0.0")
        val version = "1.2.0"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("1.0.0", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsBeforeNull() {
        val version = "1.2.0"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(Collections.emptyList(), version)
        assertNull(foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsBefore12() {
        val list = listOf("tagName")
        val version = "1.2.0"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("tagName", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsBeforeLeadingZero() {
        val list = listOf("01.05", "01.03", "01.01")
        val version = "01.02"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("01.01", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsBeforeLeadingZeroPrefix() {
        val list = listOf("v01.05", "v01.03", "v01.01")
        val version = "01.02"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("v01.01", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveNonDigitPrefix() {
        val s = "1.2.3"
        val r = VersionUtil.removeNonDigitPrefix(s)
        assertEquals(s, r)
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveNonDigitPrefix2() {
        val s = "abs-1.2.3"
        val r = VersionUtil.removeNonDigitPrefix(s)
        assertEquals("1.2.3", r)
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveNonDigitPrefix3() {
        val s = ""
        val r = VersionUtil.removeNonDigitPrefix(s)
        assertEquals("", r)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsRealCase01() {
        val list = listOf("7.17.1_deals_config_fix", "7.17.8", "7.18.0",
                "7.18.1", "7.18.2", "7.18.3", "build-6.2.14", "build-6.2.4", "build-6.2.5",
                "build-6.2.6", "build-6.2.7", "build-6.3", "build-6.4", "build-6.4.1",
                "build-6.6.29", "build-6.8", "build-7.6.122", "cummulative",
                "v7.18.4", "v7.19.0", "v7.19.1", "v7.19.2", "v7.19.3", "v7.19.4", "v7.19.5", "v7.20.0",
                "v7.20.1", "v7.20.2", "v7.20.3", "v7.20.4", "v7.20.5", "v7.20.6", "v8.0.0",
                "v8.0.1", "v8.0.2", "v8.0.3", "v8.0.4", "v8.0.5", "v8.0.6", "v8.0.7", "v8.0.9",
                "v8.1.1", "v8.1.2", "v8.1.3", "v8.2.0", "v8.2.1", "v8.2.2", "v8.2.3", "v8.2.4",
                "v8.2.5", "v8.3.0", "v8.3.1")
        val version = "8.1.0"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("v8.0.9", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsRealCase02() {
        val list = listOf("7.17.1_deals_config_fix", "7.17.8", "7.18.0",
                "7.18.1", "7.18.2", "7.18.3", "build-6.2.14", "build-6.2.4", "build-6.2.5",
                "build-6.2.6", "build-6.2.7", "build-6.3", "build-6.4", "build-6.4.1",
                "build-6.6.29", "build-6.8", "build-7.6.122", "cummulative",
                "v7.18.4", "v7.19.0", "v7.19.1", "v7.19.2", "v7.19.3", "v7.19.4", "v7.19.5", "v7.20.0",
                "v7.20.1", "v7.20.2", "v7.20.3", "v7.20.4", "v7.20.5", "v7.20.6", "v8.0.0",
                "v8.0.1", "v8.0.2", "v8.0.3", "v8.0.4", "v8.0.5", "v8.0.6", "v8.0.7", "v8.0.9",
                "v8.1.1", "v8.1.2", "v8.1.3", "v8.2.0", "v8.2.1", "v8.2.2", "v8.2.3", "v8.2.4",
                "v8.2.5", "v8.3.0", "v8.3.1")
        val version = "8.2.0"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("v8.2.0", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testFindNearestVersionTagsRealCase03() {
        val list = listOf("1.0.0.M1", "1.0.0.M2", "1.0.0.RC1", "1.0.0.RC2",
                "1.0.0.RC3", "1.0.0.RC4", "1.0.0.RELEASE",
                "1.0.1.RELEASE", "1.0.2.RELEASE", "1.1.0.M2",
                "1.1.0.M3", "1.1.0.RC1", "1.1.0.RELEASE", "1.1.1.RELEASE",
                "1.1.2.RELEASE", "1.1.3.RELEASE", "1.1.4.RELEASE", "1.1.5.RELEASE",
                "1.2.0.M1", "1.2.0.RC1", "1.2.0.RELEASE", "1.2.1.RELEASE", "1.2.2.RELEASE",
                "1.2.3.RELEASE", "1.2.4.RELEASE", "1.2.5.RELEASE", "1.3.0.RELEASE", "1.3.1.RC1",
                "1.3.1.RC2", "1.3.1.RELEASE")
        val version = "1.3.0"
        val foundVersion = VersionUtil.findNearestVersionTagsBefore(list, version)
        assertEquals("1.3.0.RELEASE", foundVersion)
    }

    @Test
    @Throws(Exception::class)
    fun testSortAndFilter() {
        val list = listOf("1.0.0.M1", "1.1.0.M2", "1.1.1.RELEASE", "1.2.5.RELEASE")
        val linkedList = VersionUtil.sortAndFilter(list, "1.1.0.M2", "1.1.1.RELEASE")
        assertEquals(2, linkedList.size)
        assertTrue(linkedList.contains("1.1.0.M2"))
        assertTrue(linkedList.contains("1.1.1.RELEASE"))
    }

    @Test
    @Throws(Exception::class)
    fun testSortAndFilterIllegal() {
        assertThrows(IllegalArgumentException::class.java) {
            val list = listOf("1.0.0.M1", "1.1.0.M2", "1.1.1.RELEASE", "1.2.5.RELEASE")
            /*LinkedList linkedList = */VersionUtil.sortAndFilter(list, "2.1.0.M2", "1.1.1.RELEASE")
        }
    }
}