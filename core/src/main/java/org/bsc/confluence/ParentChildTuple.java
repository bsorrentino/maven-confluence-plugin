package org.bsc.confluence;

import lombok.Value;

import java.util.Optional;

@Value(staticConstructor="of")
public class ParentChildTuple {
    ConfluenceService.Model.Page parent;
    Optional<ConfluenceService.Model.Page> child;
}
