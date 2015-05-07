package com.github.qwazer.mavenplugins.gitlog;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ar
 * @since Date: 04.05.2015
 */
public class VersionUtil {


    public static List<String> calculateTagNamesOfVersions(Collection<String> versions, String currentVersion, CalculateRuleForSinceTagName calculateRuleForSinceTagName) {
        String tagNamePart = calculateSinceVersionTagNamePart(currentVersion, calculateRuleForSinceTagName);
        List<String> resList = new ArrayList<String>();

        for (String s : versions) {
            if (s.contains(tagNamePart)) {
                resList.add(s);
            }
        }
        return resList;

    }


    public static String calculateSinceVersionTagNamePart(String version, CalculateRuleForSinceTagName calculateRuleForSinceTagName) {

        if (calculateRuleForSinceTagName.equals(CalculateRuleForSinceTagName.NO_RULE)) {
            return null;
        }
        ArtifactVersion artifactVersion = new DefaultArtifactVersion(version);
        if (ArtifactUtils.isSnapshot(version)) {
            artifactVersion = new DefaultArtifactVersion(StringUtils.substring(version, 0, version.length() - Artifact.SNAPSHOT_VERSION.length() - 1));
        }
        if (version.equals(artifactVersion.getQualifier())) {
            //  getLog().debug("The version is not in the regular format");  todo
        }
        int major = artifactVersion.getMajorVersion();
        int minor = artifactVersion.getMinorVersion();
        int patch = artifactVersion.getIncrementalVersion();

        switch (calculateRuleForSinceTagName) {
            case SINCE_PREV_MAJOR_RELEASE:
                major = major == 0 ? 0 : major - 1;
                minor = 0;
                patch = 0;
                break;
            case SINCE_PREV_MINOR_RELEASE:
                minor = minor == 0 ? 0 : minor - 1;
                patch = 0;
                break;
            case SINCE_PREV_PATCH_RELEASE:
                patch = patch == 0 ? 0 : patch - 1;
                break;
            default:
                throw new RuntimeException("cannot parse " + calculateRuleForSinceTagName);
        }

        return major + "." + minor + "." + patch;
    }

}
