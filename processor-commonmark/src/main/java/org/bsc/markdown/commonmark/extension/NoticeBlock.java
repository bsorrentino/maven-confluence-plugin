package org.bsc.markdown.commonmark.extension;

import org.commonmark.node.CustomBlock;
import org.commonmark.node.Visitor;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class NoticeBlock extends CustomBlock {

    public enum Type {
        UNKNOWN,
        PANEL,
        NOTE,
        WARNING,
        INFO,
        TIP;

        public String getTagName() {
            return this.name().toLowerCase();
        }

        static Type fromString( String type ) {
            try {
                return Type.valueOf(type.toUpperCase());
            }
            catch( Exception e ) {
                return Type.UNKNOWN;
            }
        }
    }


    final Type type;
    final Optional<String> title;

    public Type getType() {
        return type;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public NoticeBlock(Type type, String title) {
        this.type = type;
        this.title = ofNullable(title);
    }
    public NoticeBlock(Type type) {
        this.type = type;
        this.title = Optional.empty();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
