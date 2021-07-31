package org.bsc.reporting.renderer;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.codehaus.plexus.util.StringUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;


class ParsedVersion implements Comparable<ParsedVersion> {
    final String string;
    final ArtifactVersion parsed;

    ParsedVersion( String version ) {
        this.string = version;
        this.parsed = VersionUtil.parseArtifactVersion(version);
    }

    @Override
    public int compareTo( ParsedVersion o) {
        return parsed.compareTo(o.parsed);
    }
}

/**
 * @author ar
 * @since Date: 04.05.2015
 */
public class VersionUtil {


    public static String calculateVersionTagNamePart(String version, CalculateRuleForSinceTagName calculateRuleForSinceTagName) {

        if (calculateRuleForSinceTagName.equals(CalculateRuleForSinceTagName.NO_RULE)) {
            return null;
        }
        final ArtifactVersion artifactVersion = parseArtifactVersion(version);
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
            if (!Character.isDigit(version.charAt(i)) && version.charAt(i) != '.') {
                break;
            }
        }
        if (i > 0) {
            final String endVer = version.substring(i);

            if (!endVer.isEmpty())
                return ((version.charAt(i - 1) == '.') ?
                    version.substring(0, i - 1) :
                    version.substring(0, i)) + "-" + endVer ;
        }

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

    /**
     *
     * @param tagNames
     * @param versionTagNamePart
     * @return
     */
    public static Collection<String> filterTagNamesByTagNamePart(Collection<String> tagNames, String versionTagNamePart) {
        return tagNames.stream().filter( tagName -> tagName.contains(versionTagNamePart) ).collect(toList());
    }


    /**
     *
     * @param versionTagList
     * @param versionTagNamePart
     * @return
     */
    public static Optional<String> findNearestVersionTagsBefore(Collection<String> versionTagList, String versionTagNamePart) {


        final ParsedVersion currentVersion = new ParsedVersion(versionTagNamePart);

        //the index of the search key, if it is contained in the list; otherwise, (-(insertion point) - 1).

        final Predicate<ParsedVersion> compare = ( v ) ->
                v.string.startsWith(currentVersion.string) ||
                        currentVersion.compareTo(v) >= 0;

        return versionTagList.stream()
                .map( ParsedVersion::new )
                .sorted( Comparator.reverseOrder() )
                .filter( compare )
                .findFirst()
                        .map( v -> v.string)

        ;
    }

    /**
     *
     * @param versionTagList
     * @param versionTagNamePart
     * @return
     */
    public static Collection<String>  removeTagWithVersion(Collection<String> versionTagList, String versionTagNamePart){

        final Map<ArtifactVersion, String> map =
                versionTagList.stream().collect( toMap( VersionUtil::parseArtifactVersion, identity() ));

        final ArtifactVersion currentVersion = parseArtifactVersion(versionTagNamePart);

        map.remove(currentVersion);

        return map.values();
    }

    /**
     *
     * @param versionNameList
     * @param start
     * @param end
     * @return
     */
    public static LinkedList<String> sortAndFilter(Collection<String> versionNameList,
                                                   String start,
                                                   String end)
    {

        final ArtifactVersion startVersion = parseArtifactVersion(start);
        final ArtifactVersion endVersion = (end != null && !end.isEmpty()) ? parseArtifactVersion(end) : null;

        if (endVersion!=null && startVersion.compareTo(endVersion) > 0) {
            throw new IllegalArgumentException(
                    format("startVersion %s must be less or equals to endVersion %s",
                            startVersion, endVersion));

        }

        final Map<ArtifactVersion, String> map =
                versionNameList.stream().collect( toMap( VersionUtil::parseArtifactVersion, identity() ));

        return map.keySet().stream().filter( current -> {

                if (endVersion != null) {
                    if (startVersion.compareTo(current) <= 0 && endVersion.compareTo(current) >= 0) {
                        return true;
                    }
                } else {
                    if (startVersion.compareTo(current) <= 0) {
                        return true;
                    }
                }

                return false;
        })
        .sorted()
        .map( artifactVersion -> map.get(artifactVersion) )
        .collect( toCollection( () -> new LinkedList<String>()));


    }

}
