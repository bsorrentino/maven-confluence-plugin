package org.bsc.confluence.rest.scrollversions.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.bsc.confluence.ConfluenceService;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public interface ScrollVersions {

    interface Model {

        interface Result {

            ConfluenceService.Model.ID getMasterPageId();
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

        class PageResult implements ConfluenceService.Model.Page, Result {
            final ScrollVersions.Model.Page masterPage;
            final List<Page> versionPages;

            public Page getMasterPage() {
                return masterPage;
            }

            public List<Page> getVersionPages() {
                return versionPages;
            }


            public static PageResult of(ScrollVersions.Model.Page masterPage, List<Page> versionPage ) {
                return new PageResult(masterPage, versionPage);
            }
            
            private PageResult(ScrollVersions.Model.Page masterPage, List<Page> versionPages) {
                this.masterPage = masterPage;
                this.versionPages = versionPages;
            }

            private Page resolvePage() {
                return (versionPages.isEmpty() || versionPages.size() >  1 )
                        ? masterPage
                        : versionPages.get(0);
            }

            @Override
            public  ConfluenceService.Model.ID getId() {
                return ConfluenceService.Model.ID.of(resolvePage().getConfluencePageId());
            }

            @Override
            public String getTitle() { return masterPage.getConfluencePageTitle(); }

            @Override
            public String getSpace() { return masterPage.getSpaceKey(); }

            @Override
            public ConfluenceService.Model.ID getParentId() {
                throw new UnsupportedOperationException("getParentId is not supported in scroll versions mode");
            }

            @Override
            public int getVersion() { return 1; }

            @Override
            public ConfluenceService.Model.ID getMasterPageId() {
                return ConfluenceService.Model.ID.of(masterPage.getConfluencePageId());
            }
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        class NewPageResult implements ConfluenceService.Model.Page, Result {

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
            public ConfluenceService.Model.ID getId() { return ConfluenceService.Model.ID.of(confluencePage.id);  }

            @Override
            public String getTitle() { return scrollPageTitle; }

            @Override
            public String getSpace() { return spaceKey; }

            @Override
            public ConfluenceService.Model.ID getParentId() {
                return ConfluenceService.Model.ID.of(confluencePage.parentId);
            }

            @Override
            public int getVersion() { return 1; }

            @Override
            public ConfluenceService.Model.ID getMasterPageId() {
                return ConfluenceService.Model.ID.of(masterConfluencePage.getId());
            }

        }

    }
}
