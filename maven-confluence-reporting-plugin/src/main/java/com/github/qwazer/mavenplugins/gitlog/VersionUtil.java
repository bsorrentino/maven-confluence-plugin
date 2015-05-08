package com.github.qwazer.mavenplugins.gitlog;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.codehaus.plexus.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ar
 * @since Date: 04.05.2015
 */
public class VersionUtil {


    public static String calculateVersionTagNamePart(String version, CalculateRuleForSinceTagName calculateRuleForSinceTagName) {

        if (calculateRuleForSinceTagName.equals(CalculateRuleForSinceTagName.NO_RULE)) {
            return null;
        }
        ArtifactVersion artifactVersion = parseArtifactVersion(version);
        int major = artifactVersion.getMajorVersion();
        int minor = artifactVersion.getMinorVersion();
        int patch = artifactVersion.getIncrementalVersion();

        switch (calculateRuleForSinceTagName) {
            case SINCE_PREV_MAJOR_RELEASE:
                if (minor == 0 && patch == 0) {
                    major = major == 0 ? 0 : major - 1;
                } else {
                    minor = 0;
                    patch = 0;
                }
                break;
            case SINCE_PREV_MINOR_RELEASE:
                if (patch == 0) {
                    minor = minor == 0 ? 0 : minor - 1;
                }
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

    private static ArtifactVersion parseArtifactVersion(String version) {
        version = removeNonDigitPrefix(version);
        ArtifactVersion artifactVersion = new DefaultArtifactVersion(version);
        if (ArtifactUtils.isSnapshot(version)) {
            artifactVersion = new DefaultArtifactVersion(StringUtils.substring(version, 0, version.length() - Artifact.SNAPSHOT_VERSION.length() - 1));
        }
        return artifactVersion;
    }

    protected static String removeNonDigitPrefix(String version) {
        if (version != null && !version.isEmpty()) {
            if (!Character.isDigit(version.charAt(0))) {
                Matcher matcher = Pattern.compile("\\d+").matcher(version);
                int i = 0;
                if (matcher.find()) {
                    i = matcher.end();
                }
                if (i > 0) {
                    version = StringUtils.substring(version, i - 1, version.length());
                }
            }
        }
        return version;
    }

    public static Collection<String> filterTagNamesByTagNamePart(Collection<String> tagNames, String versionTagNamePart) {
        List<String> list = new ArrayList<String>();
        for (String tagName : tagNames) {
            if (tagName.contains(versionTagNamePart)) {
                list.add(tagName);
            }
        }
        return list;
    }

    public static String findNearestVersionTagsBefore(Collection<String> versionTagList, String versionTagNamePart) {

        Map<ArtifactVersion, String> artifactVersionStringMap = new HashMap<ArtifactVersion, String>();

        for (String versionTag : versionTagList) {
            artifactVersionStringMap.put(parseArtifactVersion(versionTag), versionTag);
        }

        ArtifactVersion currentVersion = parseArtifactVersion(versionTagNamePart);

        List<ArtifactVersion> sortedList = new ArrayList<ArtifactVersion>(artifactVersionStringMap.keySet());

        Collections.sort(sortedList);

//        Comparator<ArtifactVersion> comparator = new Comparator<ArtifactVersion>() {
//            public int compare(ArtifactVersion a1, ArtifactVersion a2) {
//                return a1.compareTo(a2);
//            }
//        };

        int index = Collections.binarySearch(sortedList, currentVersion, null);

        if (index >= 0) {
            return artifactVersionStringMap.get(sortedList.get(index));
        }
        if (sortedList.size() > 0) {
            return artifactVersionStringMap.get(sortedList.get(-index - 2));
        } else {
            return null;
        }
    }

}
