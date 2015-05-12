package com.github.qwazer.mavenplugins.gitlog;

/**
 * Calculate rule for since tag name
 * @author ar
 * @since Date: 04.05.2015
 */
public enum CalculateRuleForSinceTagName {
    NO_RULE,
    CURRENT_MAJOR_VERSION,
    CURRENT_MINOR_VERSION,
    PREVIOUS_PATCH_VERSION
}
