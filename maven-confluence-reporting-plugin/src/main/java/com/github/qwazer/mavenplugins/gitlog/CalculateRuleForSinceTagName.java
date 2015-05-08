package com.github.qwazer.mavenplugins.gitlog;

/**
 * Calculate rule for since tag name
 * @author ar
 * @since Date: 04.05.2015
 */
public enum CalculateRuleForSinceTagName {
    NO_RULE,
    SINCE_CURRENT_MAJOR_VERSION,
    SINCE_CURRENT_MINOR_VERSION,
    SINCE_PREVIOUS_PATCH_VERSION
}
