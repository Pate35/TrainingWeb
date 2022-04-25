package com.example.TrainingWeb.business.renegade.exceptions;

public class PieceOutOfFieldException extends IllegalArgumentException{
    public PieceOutOfFieldException(int x, int y) {
        super("Placed piece is not inside Field! Placed at: [" + x + "," + y + "]");
    }
}
