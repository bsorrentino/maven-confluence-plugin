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
            case SINCE_CURRENT_MAJOR_VERSION:
                minor = 0;
                patch = 0;
                break;
            case SINCE_CURRENT_MINOR_VERSION:
                patch = 0;
                break;
            case SINCE_PREVIOUS_PATCH_VERSION:
                patch = patch == 0 ? 0 : patch - 1;
                break;
            default:
                throw new RuntimeException("cannot parse " + calculateRuleForSinceTagName);
        }

        return major + "." + minor + "." + patch;
    }

    protected static ArtifactVersion parseArtifactVersion(String version) {
        version = version.replaceAll("\\s", "");
        version = removeNonDigitPrefix(version);
        version = addSuffixDelimeterIfNeeded(version);
        ArtifactVersion artifactVersion = new DefaultArtifactVersion(version);
        if (ArtifactUtils.isSnapshot(version)) {
            artifactVersion = new DefaultArtifactVersion(StringUtils.substring(version, 0, version.length() - Artifact.SNAPSHOT_VERSION.length() - 1));
        }
        return artifactVersion;
    }

    /**
     * Maven DefaultArtifactVersion use '-' sign as delimeter from project version
     * So version like 1.2.3.RELEASE will not parsed properly without modifications
     * @see DefaultArtifactVersion
     * @param version
     * @return
     */
    protected static String addSuffixDelimeterIfNeeded(String version) {
        if (version.contains("-")) return version;
        int i = 0;
        for (; i < version.length(); i++) {
            if (!Character.isDigit(version.charAt(i)) &&
                    version.charAt(i) != '.') {
                break;
            }
        }
        if (i > 0) {
            if (version.charAt(i-1) == '.') {
                return version.substring(0, i - 1) + "-" + version.substring(i, version.length());
            } else {
                return version.substring(0, i) + "-" + version.substring(i, version.length());
            }
        } else
            return version;
    }

    protected static String removeNonDigitPrefix(String version) {
        if (version != null && !version.isEmpty()) {
            if (!Character.isDigit(version.charAt(0))) {
                Matcher matcher = Pattern.compile("\\d+").matcher(version);
                int i = 0;
                if (matcher.find()) {
                    i = matcher.start();
                }
                if (i > 0) {
                    version = StringUtils.substring(version, i, version.length());
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
