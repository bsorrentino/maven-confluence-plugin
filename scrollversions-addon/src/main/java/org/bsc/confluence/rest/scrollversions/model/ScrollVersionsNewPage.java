package org.bsc.confluence.rest.scrollversions.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScrollVersionsNewPage {

    private ScrollVersionsTargetVersion targetVersion;
    private String scrollPageTitle;
    private String scrollPageId;
    private String spaceKey;
    private String pageType;
    private Boolean unversioned;
    private Boolean keepUnversioned;
    private Boolean masterPage;
    private String changeType;
    private Boolean available;
    private Boolean fallback;
    private Boolean unresolved;
    private Boolean nonMatchingVersion;
    private ScrollVersionsConfluencePage confluencePage;
    private ScrollVersionsConfluencePage masterConfluencePage;

}
