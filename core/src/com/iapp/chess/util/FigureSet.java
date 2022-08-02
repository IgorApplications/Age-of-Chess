package com.iapp.chess.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.iapp.chess.model.Color;

public class FigureSet {

    private final TextureAtlas isometric;
    private final TextureAtlas royal;
    private final TextureAtlas standard;
    private FigureSetType type;
    private boolean flippedBlack, flippedWhite;
    private TextureAtlas.AtlasRegion
            whitePawn, blackPawn,
            whiteRook, blackRook,
            whiteKnight, blackKnight,
            whiteBishop, blackBishop,
            whiteQueen, blackQueen,
            whiteKing, blackKing;

    public FigureSet(TextureAtlas isometric, TextureAtlas royal, TextureAtlas standard) {
        this.isometric = isometric;
        this.royal = royal;
        this.standard = standard;
    }

    public FigureSetType getType() {
        return type;
    }

    public TextureAtlas.AtlasRegion getPawn(Color color) {
        return color == Color.BLACK ? blackPawn : whitePawn;
    }

    public TextureAtlas.AtlasRegion getRook(Color color) {
        return color == Color.BLACK ? blackRook : whiteRook;
    }

    public TextureAtlas.AtlasRegion getKnight(Color color) {
        return color == Color.BLACK ? blackKnight : whiteKnight;
    }

    public TextureAtlas.AtlasRegion getBishop(Color color) {
        return color == Color.BLACK ? blackBishop : whiteBishop;
    }

    public TextureAtlas.AtlasRegion getQueen(Color color) {
        return color == Color.BLACK ? blackQueen: whiteQueen;
    }

    public TextureAtlas.AtlasRegion getKing(Color color) {
        return color == Color.BLACK ? blackKing : whiteKing;
    }

    public boolean isFlipped(Color color) {
        return (flippedBlack && color == Color.BLACK)
                || (flippedWhite && color == Color.WHITE);
    }

    public void updateFlippedUpper(boolean flipped, Color upper) {
        if (upper == Color.BLACK) {
            if (flipped == flippedBlack) return;
            flippedBlack = flipped;
            blackPawn.flip(false, true);
            blackRook.flip(false, true);
            blackKnight.flip(true, true);
            blackBishop.flip(false, true);
            blackQueen.flip(false, true);
            blackKing.flip(false, true);
        } else {
            if (flipped == flippedWhite) return;
            flippedWhite = flipped;
            whitePawn.flip(false, true);
            whiteRook.flip(false, true);
            whiteKnight.flip(true, true);
            whiteBishop.flip(false, true);
            whiteQueen.flip(false, true);
            whiteKing.flip(false, true);
        }
    }

    public void updateSetType(FigureSetType type) {
        if (this.type == type) return;

        this.type = type;
        if (type == FigureSetType.ISOMETRIC) {
            initAtlasRegion(isometric);
        } else if (type == FigureSetType.ROYAL) {
            initAtlasRegion(royal);
        } else if (type == FigureSetType.STANDARD) {
            initAtlasRegion(standard);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void initAtlasRegion(TextureAtlas figures) {
        if (flippedBlack) updateFlippedUpper(false, Color.BLACK);
        if (flippedWhite) updateFlippedUpper(false, Color.WHITE);

        whitePawn = figures.findRegion("white_pawn");
        whiteRook = figures.findRegion("white_rook");
        whiteKnight = figures.findRegion("white_knight");
        whiteBishop = figures.findRegion("white_bishop");
        whiteQueen = figures.findRegion("white_queen");
        whiteKing = figures.findRegion("white_king");

        blackPawn = figures.findRegion("black_pawn");
        blackRook = figures.findRegion("black_rook");
        blackKnight = figures.findRegion("black_knight");
        blackBishop = figures.findRegion("black_bishop");
        blackQueen = figures.findRegion("black_queen");
        blackKing = figures.findRegion("black_king");
    }

    public enum FigureSetType {
        ISOMETRIC, ROYAL, STANDARD
    }
}
