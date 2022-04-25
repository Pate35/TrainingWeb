package com.example.TrainingWeb.business.renegade.exceptions;

public class PieceIsNotCapturingAnythingException extends IllegalArgumentException{
    public PieceIsNotCapturingAnythingException(int x, int y) {
        super("Placed piece is not capturing another piece! Placed at: [" + x + "," + y + "]");
    }
}
