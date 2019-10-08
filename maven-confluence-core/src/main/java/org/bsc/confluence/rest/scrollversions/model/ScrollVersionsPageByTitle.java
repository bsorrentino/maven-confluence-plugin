package org.bsc.confluence.rest.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ScrollVersionsPageByTitle {

    private final String queryArg = "scrollPageTitle";
    private String value;

}
