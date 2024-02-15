package org.bsc.reporting.renderer

import org.apache.maven.plugin.testing.SilentLog
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author ar
 * @since Date: 11.07.2015
 */
class GitLogJiraIssuesRendererTest {
    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_initial_release_Major_rule() {
        val renderer = createRendererWithParams("12.0.0", CalculateRuleForSinceTagName.CURRENT_MAJOR_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("11.1.10", result)
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_not_initial_release_Major_rule() {
        val renderer = createRendererWithParams("12.0.1", CalculateRuleForSinceTagName.CURRENT_MAJOR_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("11.1.10", result)
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_not_initial_release2_Major_rule() {
        val renderer = createRendererWithParams("12.1.1", CalculateRuleForSinceTagName.CURRENT_MAJOR_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("11.1.10", result)
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_hotfix_release_Major_rule() {
        val renderer = createRendererWithParams("11.1.11", CalculateRuleForSinceTagName.CURRENT_MAJOR_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("10.1.9", result)
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_hotfix_release2_Major_rule() {
        val renderer = createRendererWithParams("11.0.2", CalculateRuleForSinceTagName.CURRENT_MAJOR_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("10.1.9", result)
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_initial_release_Minor_rule() {
        val renderer = createRendererWithParams("12.0.0", CalculateRuleForSinceTagName.CURRENT_MINOR_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("11.1.10", result)
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_not_initial_release_Minor_rule() {
        val renderer = createRendererWithParams("12.0.1", CalculateRuleForSinceTagName.CURRENT_MINOR_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("11.1.10", result)
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_hotfix_Minor_rule() {
        val renderer = createRendererWithParams("11.1.11", CalculateRuleForSinceTagName.CURRENT_MINOR_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("11.0.1", result)
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_initial_release_Latest_rule() {
        val renderer = createRendererWithParams("12.0.0", CalculateRuleForSinceTagName.LATEST_RELEASE_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("11.1.10", result)
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_not_initial_release_Latest_rule() {
        val renderer = createRendererWithParams("12.0.1", CalculateRuleForSinceTagName.LATEST_RELEASE_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("12.0.0", result)
    }

    @Test
    @Throws(Exception::class)
    fun testCalculateTagName_hotfix_Latest_rule() {
        val renderer = createRendererWithParams("11.1.11", CalculateRuleForSinceTagName.LATEST_RELEASE_VERSION)
        val result = calculateSinceTagName(renderer)
        assertEquals("11.1.10", result)
    }

    companion object {
        fun createRendererWithParams(currentVersion: String?,
                                     rule: CalculateRuleForSinceTagName?): GitLogJiraIssuesRenderer {
            return GitLogJiraIssuesRenderer(
                    null,
                    null,
                    null,
                    null,
                    currentVersion,
                    rule,
                    null,
                    null,
                    null,
                    SilentLog())
        }

        private fun calculateSinceTagName(renderer: GitLogJiraIssuesRenderer): String {
            val list = listOf("10.0.0", "10.1.9", "11.0.0", "11.0.1", "11.1.10", "12.0.0")
            renderer.overrideGitLogSinceTagNameIfNeeded(list)
            return renderer.gitLogSinceTagName
        }
    }
}
