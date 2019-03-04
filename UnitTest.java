package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static amazons.Piece.WHITE;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** The suite of all JUnit tests for the enigma package.
 *  @author JaniceNg
 */
public class UnitTest {

    /**
     * Run the JUnit tests in this package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }


    /**
     * Tests basic correctness of put and get on the initialized board.
     */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);

    }

    /**
     * Tests proper identification of legal/illegal queen moves.
     */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
    }

    /**
     * Tests toString for initial board state and a smiling board state. :)
     */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
                    "   - - - B - - B - - -\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   B - - - - - - - - B\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   W - - - - - - - - W\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   - - - W - - W - - -\n";

    static final String TEST_BOARD =
                    "   - - - B - - B - - -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   B - - - - - - - - B\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   - - - - - B - - - -\n"
                            +
                    "   W - - - - - - - - W\n"
                            +
                    "   - - - - - - - - - -\n"
                            +
                    "   B B - - - - - - - -\n"
                            +
                    "   W - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    +
                    "   - S S S - - S S S -\n"
                    +
                    "   - S - S - - S - S -\n"
                    +
                    "   - S S S - - S S S -\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   - - W - - - - W - -\n"
                    +
                    "   - - - W W W W - - -\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   - - - - - - - - - -\n";


    /**
     * Tests testReachableIterator
     */
    @Test
    public void testReachableIterator() {
        Board testboard = new Board();
        Iterator reachableFrom = testboard.reachableFrom(Square.sq(0, 3), null);
        assertEquals(Square.sq(0, 4), reachableFrom.next());
    }




    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLE_FROM_TESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLE_TESTSQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLE_TESTSQUARES.size(), numSquares);
        assertEquals(REACHABLE_TESTSQUARES.size(), squares.size());
    }

    @Test
    public void testReachableFrom2() {
        Board b = new Board();
        buildBoard(b, LEGAL_TESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(0, 0), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(LEGALMOVE_TESTBOARD2.size(), numSquares);
        assertEquals(LEGALMOVE_TESTBOARD2.size(), squares.size());
    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, LEGAL_TESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(b.isLegal(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(4, numMoves);
    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves2() {
        Board b = new Board();
        buildBoard(b, START_TESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(b.isLegal(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(2176, numMoves);
    }


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHABLE_FROM_TESTBOARD = {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, W, E, E, B, E },
            { E, E, E, S, S, S, B, W, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
    };
    static final Piece[][] LEGAL_TESTBOARD  = {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { B, B, B, B, E, E, E, E, E, E },
            { W, E, E, B, E, E, E, E, E, E },
    };
    static final Piece[][] START_TESTBOARD = {
            { E, E, E, B, E, E, B, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { B, E, E, E, E, E, E, E, E, B },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { W, E, E, E, E, E, E, E, E, W },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, W, E, E, W, E, E, E },
    };

    static final Set<Square> REACHABLE_TESTSQUARES =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Set<Square> LEGALMOVE_TESTBOARD2 =
            new HashSet<>(Arrays.asList(
                    Square.sq(1, 0),
                    Square.sq(2, 0)));

    static final String TESTBOARD2 =
            "   - - - B - - B - - -\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   B - - - - - - - - B\n"
                    +
                    "   - - - - - - - - - -\n"
                    +
                    "   - - - - - B - - - -\n"
                    +
                    "   W - - - - - - - - W\n"
                    +
                    "   - B B B - - - - - -\n"
                    +
                    "   B B - B - - - - - -\n"
                    +
                    "   W - - B - - W - - -\n";


    static final Piece[][] LEGALMOVE_TESTBOARD = {
            { E, E, E, B, E, E, B, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { B, E, E, E, E, E, E, E, E, B },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { W, E, E, E, E, E, E, E, E, W },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, W, E, E, W, E, E, E },
    };

    @Test
    public void testIterator() {
        Board b = new Board();
        for (int i = 2; i < 100; i++) {
            b.put(BLACK, Square.sq(i));
        }
        Iterator<Move> b1 = b.legalMoves(BLACK);
        while (b1.hasNext()) {
            System.out.println(b1.hasNext());
            System.out.println(b1.next());
        }

    }

}















