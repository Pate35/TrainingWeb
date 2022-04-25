package com.example.TrainingWeb.presentation;

import com.example.TrainingWeb.business.MessageHelper;
import com.example.TrainingWeb.business.renegade.Piece;
import com.example.TrainingWeb.business.renegade.entity.UIPiece;
import com.example.TrainingWeb.business.renegade.exceptions.PieceIsNotCapturingAnythingException;
import com.example.TrainingWeb.business.renegade.exceptions.PieceIsOverlappingException;
import com.example.TrainingWeb.business.renegade.exceptions.PieceOutOfFieldException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RenegadeGUITest {

    @Mock
    private MessageHelper messageHelper;

    private RenegadeGUI renegadeGUI;

    @BeforeAll
    private static void staticInit() {
        mockStatic(MessageHelper.class);
    }

    @BeforeEach
    private void init() {
        renegadeGUI = new RenegadeGUI();
        renegadeGUI.generateBoard();
        renegadeGUI.setUpGame();
    }

    @Test
    public void generateField() {
        RenegadeGUI renegadeGUI = new RenegadeGUI();
        assertThat(renegadeGUI.getBoard()).isNull();
        renegadeGUI.generateBoard();
        assertThat(renegadeGUI.getBoard())
                .isNotNull()
                .hasDimensions(8, 8);
        assertThat(renegadeGUI.containsPiece(new UIPiece(3, 3, Piece.WHITE))).isTrue();
        assertThat(renegadeGUI.containsPiece(new UIPiece(4, 3, Piece.BLACK))).isTrue();
        assertThat(renegadeGUI.containsPiece(new UIPiece(3, 4, Piece.BLACK))).isTrue();
        assertThat(renegadeGUI.containsPiece(new UIPiece(4, 4, Piece.WHITE))).isTrue();
    }

    @Test
    public void insertWhitePiece() {
        assertThat(renegadeGUI.getBoard()).isNotNull();
        renegadeGUI.insertWhitePiece(4, 2);
        assertThat(renegadeGUI.containsPiece(new UIPiece(4, 2, Piece.WHITE))).isTrue();
        assertThat(renegadeGUI.containsPiece(new UIPiece(4, 3, Piece.WHITE))).isTrue();

        renegadeGUI.insertWhitePiece(2, 4);
        assertThat(renegadeGUI.containsPiece(new UIPiece(2, 4, Piece.WHITE))).isTrue();
        assertThat(renegadeGUI.containsPiece(new UIPiece(3, 4, Piece.WHITE))).isTrue();
    }

    @Test
    public void insertPiece_illegalArgument() {
        assertThatExceptionOfType(PieceOutOfFieldException.class)
                .isThrownBy(() -> renegadeGUI.insertWhitePiece(9, 5));
        assertThatExceptionOfType(PieceOutOfFieldException.class)
                .isThrownBy(() -> renegadeGUI.insertWhitePiece(5, 8));
        assertThatExceptionOfType(PieceOutOfFieldException.class)
                .isThrownBy(() -> renegadeGUI.insertWhitePiece(8, 8));

        assertThatExceptionOfType(PieceOutOfFieldException.class)
                .isThrownBy(() -> renegadeGUI.insertBlackPiece(9, 5));
        assertThatExceptionOfType(PieceOutOfFieldException.class)
                .isThrownBy(() -> renegadeGUI.insertBlackPiece(5, 8));
        assertThatExceptionOfType(PieceOutOfFieldException.class)
                .isThrownBy(() -> renegadeGUI.insertBlackPiece(8, 8));
    }

    @Test
    public void insertBlackPiece() {
        assertThat(renegadeGUI.getBoard()).isNotNull();
        renegadeGUI.insertBlackPiece(4, 5);
        assertThat(renegadeGUI.containsPiece(new UIPiece(4, 5, Piece.BLACK))).isTrue();
        assertThat(renegadeGUI.containsPiece(new UIPiece(4, 4, Piece.BLACK))).isTrue();

        renegadeGUI.insertBlackPiece(3, 2);
        assertThat(renegadeGUI.containsPiece(new UIPiece(3, 2, Piece.BLACK))).isTrue();
        assertThat(renegadeGUI.containsPiece(new UIPiece(3, 3, Piece.BLACK))).isTrue();
    }

    @Test
    public void overlappingMove() {
        assertThatExceptionOfType(PieceIsOverlappingException.class)
                .isThrownBy(() -> renegadeGUI.insertWhitePiece(4, 4));

        assertThatExceptionOfType(PieceIsOverlappingException.class)
                .isThrownBy(() -> renegadeGUI.insertBlackPiece(3, 3));

        renegadeGUI.insertWhitePiece(4, 2);
        assertThatExceptionOfType(PieceIsOverlappingException.class)
                .isThrownBy(() -> renegadeGUI.insertBlackPiece(4, 2));
    }

    @Test
    public void notPlaceableMove() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> renegadeGUI.insertWhitePiece(2, 2));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> renegadeGUI.insertWhitePiece(2, 3));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> renegadeGUI.insertBlackPiece(4, 2));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> renegadeGUI.insertBlackPiece(3, 5));
    }

    @Test
    public void capturingMove() {
        renegadeGUI.putPiece(new UIPiece(3, 2, Piece.WHITE));
        renegadeGUI.putPiece(new UIPiece(4, 2, Piece.BLACK));
        renegadeGUI.insertBlackPiece(2, 2);
    }

    @Test
    public void fixbug() {
        renegadeGUI.putPiece(new UIPiece(2, 3, Piece.BLACK));
        renegadeGUI.putPiece(new UIPiece(2, 2, Piece.WHITE));
        assertThat(renegadeGUI.getTurn()).isEqualTo(Piece.BLACK);
        assertThatExceptionOfType(PieceIsNotCapturingAnythingException.class)
                .isThrownBy(() ->  renegadeGUI.makeMove(new UIPiece(1, 2, Piece.BLACK)));
    }
}