package com.example.TrainingWeb.business.renegade.entity;

import lombok.Getter;

import java.util.Objects;

@Getter
public class UIPiece {

    int x;
    int y;
    int piece;

    public UIPiece(int x, int y, int piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIPiece uiPiece = (UIPiece) o;
        return piece == uiPiece.piece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece);
    }
}
