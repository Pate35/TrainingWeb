package com.example.TrainingWeb.business.renegade.exceptions;

public class PieceIsOverlappingException extends IllegalArgumentException{
    public PieceIsOverlappingException(int x, int y) {
        super("Placed piece is overlapping with another piece! Placed at: [" + x + "," + y + "]");
    }
}
