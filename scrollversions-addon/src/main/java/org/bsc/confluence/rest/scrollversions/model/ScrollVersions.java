package org.bsc.confluence.rest.scrollversions.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.bsc.confluence.ConfluenceService;

import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public interface ScrollVersions {

    interface Model {

        interface MasterPage {

            long getMasterPageId();
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Version {
            private String id;
            private String name;
            private String precedingVersionId;
            private boolean archived;
            private boolean runtimeAccessible;
            private String color;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        class TargetVersion {
            private String versionId;
            private String id;
            private String name;
            private String i18nName;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        class Page  {

            private TargetVersion targetVersion;

            private String scrollPageTitle;
            private String plainConfluenceTitle;
            private String confluencePageTitle;
            private String scrollPageId;
            private String pageType;
            private String changeType;
            private long confluencePageId;
            private String spaceKey;
            private boolean available;
            private boolean converted;
            private boolean isDirty;
            private String includedScrollPageIds;
            private boolean cached;
            private Long lastModificationDate;
            private String lastModifier;

            public boolean isMasterPage() {
                return ofNullable(pageType)
                        .map( type -> type.equals("masterPage") )
                        .orElse(false);
            }
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        class ConfluencePage {

            private String urlPath;
            private Long id;
            private Long parentId;
            private Long lastModificationDate;
            private String lastModifier;
            private Long creationDate;

        }

        @Value(staticConstructor = "of")
        class PageResult implements ConfluenceService.Model.Page, MasterPage {
            ScrollVersions.Model.Page masterPage;
            List<Page> versionPages;

            private Page resolvePage() {
                return (versionPages.isEmpty() || versionPages.size() >  1 )
                        ? masterPage
                        : versionPages.get(0);
            }

            @Override
            public String getId() { return String.valueOf(resolvePage().getConfluencePageId()); }

            @Override
            public String getTitle() { return resolvePage().getConfluencePageTitle(); }

            @Override
            public String getSpace() { return masterPage.getSpaceKey(); }

            @Override
            public String getParentId() { { throw new UnsupportedOperationException("getParentId is not supported in scroll versions mode"); } }

            @Override
            public int getVersion() { return 2; }

            @Override
            public long getMasterPageId() { return masterPage.getConfluencePageId(); }
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        class NewPageResult implements ConfluenceService.Model.Page, MasterPage {

            private TargetVersion targetVersion;

            private String scrollPageTitle;
            private String scrollPageId;
            private String spaceKey;
            private String pageType;
            private boolean unversioned;
            private boolean keepUnversioned;
            private boolean masterPage;
            private String changeType;
            private boolean available;
            private boolean fallback;
            private boolean unresolved;
            private boolean nonMatchingVersion;

            private ConfluencePage confluencePage;
            private ConfluencePage masterConfluencePage;

            @Override
            public String getId() { return String.valueOf(confluencePage.id); }

            @Override
            public String getTitle() { return scrollPageTitle; }

            @Override
            public String getSpace() { return spaceKey; }

            @Override
            public String getParentId() { return String.valueOf(confluencePage.parentId); }

            @Override
            public int getVersion() { return 2; }

            @Override
            public long getMasterPageId() {
                return masterConfluencePage.getId();
            }

        }

    }
}
