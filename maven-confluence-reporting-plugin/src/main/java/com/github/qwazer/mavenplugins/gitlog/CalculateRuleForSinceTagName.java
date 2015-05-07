package com.github.qwazer.mavenplugins.gitlog;

/**
 * Calculate rule for since tag name
 * @author ar
 * @since Date: 04.05.2015
 */
public enum CalculateRuleForSinceTagName {
    NO_RULE,
    SINCE_PREV_MAJOR_RELEASE,
    SINCE_PREV_MINOR_RELEASE,
    SINCE_PREV_PATCH_RELEASE
}
