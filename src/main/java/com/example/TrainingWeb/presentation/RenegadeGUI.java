package com.example.TrainingWeb.presentation;

import com.example.TrainingWeb.business.MessageHelper;
import com.example.TrainingWeb.business.audit.boundary.AuditBean;
import com.example.TrainingWeb.business.renegade.Piece;
import com.example.TrainingWeb.business.renegade.entity.UIPiece;
import com.example.TrainingWeb.business.renegade.exceptions.PieceIsNotCapturingAnythingException;
import com.example.TrainingWeb.business.renegade.exceptions.PieceIsOverlappingException;
import com.example.TrainingWeb.business.renegade.exceptions.PieceOutOfFieldException;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Named
@ViewScoped
public class RenegadeGUI implements Serializable {

    @EJB
    private AuditBean auditBean;

    @Getter
    @Setter
    private UIPiece[][] board;

    private int currentX;
    private int currentY;
    private int nextX;
    private int nextY;
    private int previousX;
    private int previousY;

    @Getter
    private int turn;

    @Getter
    private List<UIPiece> turnablePieces;

    @Getter
    private int captures;


    @PostConstruct
    private void init() {
        generateBoard();
        setUpGame();
    }

    public void setUpGame() {
        turn = Piece.BLACK;
        turnablePieces = new ArrayList<>();
    }

    public void generateBoard() {
        board = new UIPiece[8][8];
        fillUpAllSlots();
        setDefaultPieces();
    }

    private void fillUpAllSlots() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new UIPiece(j, i, 0);
            }
        }
    }

    private void setDefaultPieces() {
        putPiece(new UIPiece(3, 3, Piece.WHITE));
        putPiece(new UIPiece(4, 3, Piece.BLACK));
        putPiece(new UIPiece(3, 4, Piece.BLACK));
        putPiece(new UIPiece(4, 4, Piece.WHITE));
    }

    public void putPiece(UIPiece piece) {
        board[piece.getY()][piece.getX()] = piece;
    }

    public boolean containsPiece(UIPiece piece) {
        return board[piece.getY()][piece.getX()].equals(piece);
    }

    public void makeMove(UIPiece move) {
        captures = 0;
        turnablePieces.clear();

        if (turn == Piece.BLACK) {
            insertBlackPiece(move.getX(), move.getY());
        } else {
            insertWhitePiece(move.getX(), move.getY());
        }
        nextTurn();
    }

    private void nextTurn() {
        if (turn == Piece.BLACK) {
            turn = Piece.WHITE;
        } else {
            turn = Piece.BLACK;
        }
    }

    private int getEnemyPiece(int piece) {
        if (piece == Piece.BLACK) {
            return Piece.WHITE;
        }
        return Piece.BLACK;
    }

    public void insertWhitePiece(int x, int y) {
        currentX = x;
        currentY = y;
        checkValidMove();
        checkWhiteCapturingMove();
        board[currentY][currentX] = new UIPiece(currentX, currentY, Piece.WHITE);
    }

    public void insertBlackPiece(int x, int y) {
        currentX = x;
        currentY = y;
        checkValidMove();
        checkBlackCapturingMove();
        board[currentY][currentX] = new UIPiece(currentX, currentY, Piece.BLACK);
    }

    private void checkValidMove() {
        checkPieceInsideField();
        checkPieceNotOverlapping();
    }

    private void checkPieceInsideField() {
        if (currentY >= board.length || currentX >= board[0].length) {
            MessageHelper.addErrorMessage("Placed piece is not inside Field! Placed at: [" + currentX + "," + currentY + "]");
            throw new PieceOutOfFieldException(currentX, currentY);
        }
    }

    private void checkPieceNotOverlapping() {
        if (board[currentY][currentX].getPiece() != 0) {
            MessageHelper.addErrorMessage("Placed piece is overlapping with another piece! Placed at: [" + currentX + "," + currentY + "]");
            throw new PieceIsOverlappingException(currentX, currentY);
        }
    }

    private void checkWhiteCapturingMove() {
        setNextAndPreviousNumbers();
        checkPerpendicularLines(Piece.BLACK);
        setNextAndPreviousNumbers();
        checkDiagonalLines(Piece.BLACK);

        if (captures == 0) {
            MessageHelper.addErrorMessage("Placed piece is not capturing another piece! Placed at: [" + currentX + "," + currentY + "]");
            throw new PieceIsNotCapturingAnythingException(currentX, currentY);
        }
    }

    private void checkBlackCapturingMove() {
        setNextAndPreviousNumbers();
        checkPerpendicularLines(Piece.WHITE);
        setNextAndPreviousNumbers();
        checkDiagonalLines(Piece.WHITE);

        if (captures == 0) {
            MessageHelper.addErrorMessage("Placed piece is not capturing another piece! Placed at: [" + currentX + "," + currentY + "]");
            throw new PieceIsNotCapturingAnythingException(currentX, currentY);
        }
    }

    private void setNextAndPreviousNumbers() {
        setNextNumbers(currentX, currentY);
        setPreviousNumbers(currentX, currentY);
    }

    private void checkPerpendicularLines(int piece) {
        if (isPerpendicular(piece)) {

            setNextNumbers(nextX, nextY);
            setPreviousNumbers(previousX, previousY);

            checkDeepCapturingMovePerpendicular(piece);
        }
    }

    private void checkDiagonalLines(int piece) {
        if (isDiagonal(piece)) {

            setNextNumbers(nextX, nextY);
            setPreviousNumbers(previousX, previousY);

            checkDeepCapturingMoveDiagonal(piece);
        }
    }

    private void checkDeepCapturingMovePerpendicular(int piece) {
        checkCapturablePerpendicular(getEnemyPiece(piece));

        if (isPerpendicular(Piece.WHITE)) {
            setNextNumbers(nextX, nextY);
            setPreviousNumbers(previousX, previousY);
            checkDeepCapturingMovePerpendicular(piece);
        }
    }

    private void checkDeepCapturingMoveDiagonal(int piece) {
        checkCapturableDiagonal(getEnemyPiece(piece));

        if (isDiagonal(Piece.WHITE)) {
            setNextNumbers(nextX, nextY);
            setPreviousNumbers(previousX, previousY);
            checkDeepCapturingMoveDiagonal(piece);
        }
    }


    private void checkCapturablePerpendicular(int piece) {
        if (isPerpendicular(piece)) {
            capturePieces(piece);
            captures++;
        }
    }

    private void checkCapturableDiagonal(int piece) {
        if (isDiagonal(piece)) {
            capturePieces(piece);
            captures++;
        }
    }

    private void capturePieces(int capturingPiece) {
        addToTurnablePieces(capturingPiece);

        for (UIPiece piece : turnablePieces) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (row == piece.getY() && col == piece.getX()) {
                        board[row][col] = new UIPiece(col, row, capturingPiece);
                    }
                }
            }
        }
    }

    private void addToTurnablePieces(int capturingPiece) {
        List<UIPiece> tempTurnablePieces = new ArrayList<>();
        // check if same colour is in between so it shouldnt be capturable

        if (board[currentY][nextX].getPiece() == capturingPiece) {
            for (int i = nextX; i > currentX; i--) {
                if (board[currentY][i].getPiece() == 0) {
                    tempTurnablePieces.clear();
                } else {
                    tempTurnablePieces.add(board[currentY][i]);
                }
            }
            turnablePieces.addAll(tempTurnablePieces);
            tempTurnablePieces.clear();
        }

        if (board[currentY][previousX].getPiece() == capturingPiece) {
            tempTurnablePieces.clear();
            for (int i = previousX; i < currentX; i++) {
                if (board[currentY][i].getPiece() == 0) {
                    tempTurnablePieces.clear();
                } else {
                    tempTurnablePieces.add(board[currentY][i]);
                }
            }
            turnablePieces.addAll(tempTurnablePieces);
            tempTurnablePieces.clear();
        }

        if (board[nextY][currentX].getPiece() == capturingPiece) {
            tempTurnablePieces.clear();
            for (int i = nextY; i > currentY; i--) {
                if (board[i][currentX].getPiece() == 0) {
                    tempTurnablePieces.clear();
                } else {
                    tempTurnablePieces.add(board[i][currentX]);
                }
            }
            turnablePieces.addAll(tempTurnablePieces);
            tempTurnablePieces.clear();
        }

        if (board[previousY][currentX].getPiece() == capturingPiece) {
            tempTurnablePieces.clear();
            for (int i = previousY; i < currentY; i++) {
                if (board[i][currentX].getPiece() == 0) {
                    tempTurnablePieces.clear();
                }else {
                    tempTurnablePieces.add(board[i][currentX]);
                }
            }
            turnablePieces.addAll(tempTurnablePieces);
            tempTurnablePieces.clear();
        }

        if (board[previousY][previousX].getPiece() == capturingPiece) {
            tempTurnablePieces.clear();
            boolean evenNumber = ((previousY + previousX) % 2 == 0);
            for (int x = previousX; x < currentX; x++) {
                for (int y = previousY; y < currentY; y++) {
                    if (board[y][x].getPiece() == 0) {
                        tempTurnablePieces.clear();
                    } else {
                        if (evenNumber) {
                            if((x + y) % 2 == 0) {
                                tempTurnablePieces.add(board[y][x]);
                            }
                        } else {
                            if((x + y) % 2 == 1) {
                                tempTurnablePieces.add(board[y][x]);
                            }
                        }
                    }
                }
            }
            turnablePieces.addAll(tempTurnablePieces);
            tempTurnablePieces.clear();
        }

        if (board[previousY][nextX].getPiece() == capturingPiece) {
            tempTurnablePieces.clear();
            int sumForDiagonalSearch = previousY + nextX;
            for (int x = nextX; x > currentX; x--) {
                for (int y = previousY; y < currentY; y++) {
                    if (board[y][x].getPiece() == 0) {
                        tempTurnablePieces.clear();
                    } else {
                        if (x + y == sumForDiagonalSearch) {
                            tempTurnablePieces.add(board[y][x]);
                        }
                    }
                }
            }
            turnablePieces.addAll(tempTurnablePieces);
            tempTurnablePieces.clear();
        }

        if (board[nextY][previousX].getPiece() == capturingPiece) {
            tempTurnablePieces.clear();
            int sumForDiagonalSearch = nextY + previousX;
            for (int x = previousX; x < currentX; x++) {
                for (int y = nextY; y > currentY; y--) {
                    if (board[y][x].getPiece() == 0) {
                        tempTurnablePieces.clear();
                    } else {
                        if (x + y == sumForDiagonalSearch) {
                            tempTurnablePieces.add(board[y][x]);
                        }
                    }
                }
            }
            turnablePieces.addAll(tempTurnablePieces);
            tempTurnablePieces.clear();
        }

        if (board[nextY][nextX].getPiece() == capturingPiece) {
            tempTurnablePieces.clear();
            boolean evenNumber = ((nextY + nextX) % 2 == 0);
            for (int x = nextX; x > currentX; x--) {
                for (int y = nextY; y > currentY; y--) {
                    if (board[y][x].getPiece() == 0) {
                        tempTurnablePieces.clear();
                    } else {
                        if (evenNumber) {
                            if((x + y) % 2 == 0) {
                                tempTurnablePieces.add(board[y][x]);
                            }
                        } else {
                            if((x + y) % 2 == 1) {
                                tempTurnablePieces.add(board[y][x]);
                            }
                        }
                    }
                }
            }
            turnablePieces.addAll(tempTurnablePieces);
            tempTurnablePieces.clear();
        }

        if (turnablePieces.isEmpty()) {
            throw new PieceIsNotCapturingAnythingException(currentX, currentY);
        }
    }

    private boolean isPerpendicular(int toCapture) {
        return board[currentY][nextX].getPiece() == toCapture
                || board[currentY][previousX].getPiece() == toCapture
                || board[nextY][currentX].getPiece() == toCapture
                || board[previousY][currentX].getPiece() == toCapture;
    }


    private boolean isDiagonal(int toCapture) {
        return board[previousY][previousX].getPiece() == toCapture
                || board[previousY][nextX].getPiece() == toCapture
                || board[nextY][previousX].getPiece() == toCapture
                || board[nextY][nextX].getPiece() == toCapture;
    }


    private void setNextNumbers(int x, int y) {
        nextX = x != 7 ? new AtomicInteger(x).incrementAndGet() : 7;
        nextY = y != 7 ? new AtomicInteger(y).incrementAndGet() : 7;
    }

    private void setPreviousNumbers(int x, int y) {
        previousX = x != 0 ? new AtomicInteger(x).decrementAndGet() : 0;
        previousY = y != 0 ? new AtomicInteger(y).decrementAndGet() : 0;
    }


    public String getStyleClass(UIPiece piece) {
        if (piece.getPiece() == 0)
            return "piece";
        if (piece.getPiece() == 1)
            return "piece white";
        if (piece.getPiece() == 2)
            return "piece black";
        return null;
    }

    public String getStyleClass() {
        if (turn == 1)
            return "piece white";
        if (turn == 2)
            return "piece black";
        return null;
    }
}
