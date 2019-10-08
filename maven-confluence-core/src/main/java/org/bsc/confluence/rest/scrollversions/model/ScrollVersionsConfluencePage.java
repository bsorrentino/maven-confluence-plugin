package org.bsc.confluence.rest.model;

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
public class ScrollVersionsConfluencePage {

    private String urlPath;
    private Long id;
    private Long parentId;
    private Long lastModificationDate;
    private String lastModifier;
    private Long creationDate;

}
