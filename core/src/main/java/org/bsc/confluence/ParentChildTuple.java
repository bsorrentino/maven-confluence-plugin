package org.bsc.confluence;


import java.util.Optional;

public class ParentChildTuple {
    private final ConfluenceService.Model.Page parent;
    private final ConfluenceService.Model.Page child;

    public ConfluenceService.Model.Page getParent() { return parent; }

    public Optional<ConfluenceService.Model.Page> getChild() { return Optional.ofNullable(child); }

    ParentChildTuple( ConfluenceService.Model.Page parent, ConfluenceService.Model.Page child) {
        this.parent = parent;
        this.child = child;
    }

    public static ParentChildTuple of(ConfluenceService.Model.Page parent, ConfluenceService.Model.Page child) {
        return new ParentChildTuple(parent, child);
    }
    
    @Deprecated
    public static ParentChildTuple of(ConfluenceService.Model.Page parent, Optional<ConfluenceService.Model.Page> child) {
        return new ParentChildTuple(parent, child.orElse(null));
    }
}
