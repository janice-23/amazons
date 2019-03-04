package amazons;

import java.util.Iterator;
import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author JaniceNg
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        }
        Move bestmove = null;
        if (sense == 1) {
            int bestVal = -INFTY;
            for (Iterator<Move> whitemoves = board.legalMoves(WHITE);
                 whitemoves.hasNext(); ) {
                Move currmoveWhite = whitemoves.next();
                board.makeMove(currmoveWhite);
                int currVal =
                        findMove(board, depth - 1,
                                false, -1, alpha, beta);
                board.undo();
                if (currVal >= bestVal) {
                    bestmove = currmoveWhite;
                    bestVal = currVal;
                }
                alpha = Math.max(alpha, currVal);
                if (beta <= alpha) {
                    break;
                }
            }

            if (saveMove) {
                _lastFoundMove = bestmove;
            }

            return bestVal;

        } else {
            int bestVal = INFTY;
            for (Iterator<Move> blackmoves = board.legalMoves(BLACK);
                 blackmoves.hasNext(); ) {
                Move currmoveBlack = blackmoves.next();
                board.makeMove(currmoveBlack);
                int currVal = findMove(board, depth - 1,
                        false, 1, alpha, beta);
                board.undo();
                if (currVal <= bestVal) {
                    bestmove = currmoveBlack;
                    bestVal = currVal;
                }
                alpha = Math.min(alpha, currVal);
                if (beta <= alpha) {
                    break;
                }
                if (saveMove) {
                    _lastFoundMove = bestmove;
                }
            }
            return bestVal;
        }

    }




    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        int N = board.numMoves();
        return 1;
    }


    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        }
        Iterator<Move> whiteIterator = board.legalMoves(WHITE);
        Iterator<Move> blackIterator = board.legalMoves(BLACK);
        int white = 0;
        int black = 0;

        while (whiteIterator.hasNext()) {
            whiteIterator.next();
            white += 1;
        }
        while (blackIterator.hasNext()) {
            blackIterator.next();
            black += 1;
        }
        return white - black;
    }
}
