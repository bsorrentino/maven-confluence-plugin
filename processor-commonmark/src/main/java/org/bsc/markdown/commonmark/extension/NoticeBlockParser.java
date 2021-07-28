package org.bsc.markdown.commonmark.extension;

import org.commonmark.internal.util.Parsing;
import org.commonmark.node.Block;
import org.commonmark.parser.block.*;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.ofNullable;

public class NoticeBlockParser extends AbstractBlockParser {

    private final NoticeBlock block;

    public NoticeBlockParser(NoticeBlock block  ) {
        this.block = block;
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean canContain(Block block) {
        return true;
    }

    @Override
    public NoticeBlock getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        int nextNonSpace = state.getNextNonSpaceIndex();
        if (isMarker(state, nextNonSpace)) {
            int newColumn = state.getColumn() + state.getIndent() + 1;
            // optional following space or tab
            if (Parsing.isSpaceOrTab(state.getLine().getContent(), nextNonSpace + 1)) {
                newColumn++;
            }
            return BlockContinue.atColumn(newColumn);
        } else {
            return BlockContinue.none();
        }
    }

    static final Pattern pattern = Pattern.compile("^>\\s+[*][*]([Ww]arning|[Nn]ote|[Ii]nfo|[Tt]ip)[:][*][*]\\s*(.*)$");

    private static Optional<Matcher> isStartedMarker(ParserState state, int index) {
        final CharSequence line = state.getLine().getContent();

        return ( state.getIndent() < Parsing.CODE_BLOCK_INDENT && index < line.length() ) ?
            ofNullable(pattern.matcher(line)).filter( m -> m.matches() ) :
            Optional.empty();
    }

    private static boolean isMarker(ParserState state, int index) {
        final CharSequence line = state.getLine().getContent();
        return state.getIndent() < Parsing.CODE_BLOCK_INDENT && index < line.length() && line.charAt(index) == '>';
    }

    public static class Factory extends AbstractBlockParserFactory {

        @Override
        public BlockStart tryStart(ParserState state, MatchedBlockParser matchedBlockParser) {
            final int nextNonSpace = state.getNextNonSpaceIndex();

            return isStartedMarker(state, nextNonSpace)
                    .map( m -> {
                        int newColumn = state.getColumn() + state.getIndent() + 1;
                        // optional following space or tab

                        if (Parsing.isSpaceOrTab(state.getLine().getContent(), nextNonSpace + 1)) {
                            newColumn++;
                        }
                        final NoticeBlock block =  new NoticeBlock( NoticeBlock.Type.fromString(m.group(1)), m.group(2)) ;

                        final NoticeBlockParser parser = new NoticeBlockParser( block );

                        return BlockStart.of(parser).atColumn(newColumn +m.end());
                    })
                    .orElseGet( () ->  BlockStart.none() );
        }
    }
}