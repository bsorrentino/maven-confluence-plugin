package com.github.qwazer.mavenplugins.gitlog;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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
            case CURRENT_MAJOR_VERSION:
                minor = 0;
                patch = 0;
                break;
            case CURRENT_MINOR_VERSION:
                patch = 0;
                break;
            case LATEST_RELEASE_VERSION:
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
     * Also Maven DefaultArtifactVersion is not compatable with OSGI version format and with Semantic Versioning
     *
     * @param version
     * @return
     * @see DefaultArtifactVersion
     * @see <a href="http://semver.org/">Semantic Versioning</a>
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
            if (version.charAt(i - 1) == '.') {
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

        Map<ArtifactVersion, String> map = new HashMap<ArtifactVersion, String>();

        for (String versionTag : versionTagList) {
            map.put(parseArtifactVersion(versionTag), versionTag);
        }

        ArtifactVersion currentVersion = parseArtifactVersion(versionTagNamePart);

        List<ArtifactVersion> sortedList = new ArrayList<ArtifactVersion>(map.keySet());

        Collections.sort(sortedList);

        int index = Collections.binarySearch(sortedList, currentVersion, null);

        if (index >= 0) {
            return map.get(sortedList.get(index));
        }
        if (sortedList.size() > 0) {
            if (-index-2>=0) {
                return map.get(sortedList.get(-index - 2));
            }
            else {
                return null;
            }
        } else {
            return null;
        }
    }


    public static LinkedList<String> sortAndFilter(Collection<String> versionNameList,
                                                   String start,
                                                   String end) {

        final ArtifactVersion startVersion = parseArtifactVersion(start);
        final ArtifactVersion endVersion;
        if (end != null && !end.isEmpty()) {
            endVersion = parseArtifactVersion(end);
        } else {
            endVersion = null;
        }


        if (endVersion!=null && startVersion.compareTo(endVersion) > 0) {
            throw new IllegalArgumentException(
                    String.format("startVersion %s must be less or equals to endVersion %s",
                            startVersion, endVersion));

        }

        LinkedList<String> linkedList = new LinkedList<String>();

        Map<ArtifactVersion, String> map = new HashMap<ArtifactVersion, String>();

        for (String versionTag : versionNameList) {
            map.put(parseArtifactVersion(versionTag), versionTag);
        }


        List<ArtifactVersion> artifactVersionSet = new ArrayList<ArtifactVersion>(map.keySet());


        CollectionUtils.filter(artifactVersionSet, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                ArtifactVersion current = (ArtifactVersion) o;
                if (endVersion != null) {
                    if (startVersion.compareTo(current) <= 0
                            &&
                            endVersion.compareTo(current) >= 0) {
                        return true;
                    }
                } else {
                    if (startVersion.compareTo(current) <= 0) {
                        return true;
                    }
                }

                return false;
            }
        });

        Collections.sort(artifactVersionSet);
        for (ArtifactVersion artifactVersion : artifactVersionSet) {
            linkedList.add(map.get(artifactVersion));
        }
        return linkedList;

    }

}
