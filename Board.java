package amazons;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;

import static amazons.Piece.*;




/** The state of an Amazons Game.
 *  @author JaniceNg
 */
class Board {
    /**
     * Creates a 2D array for board.
     */
    private Piece[][] board2d;

    /**
     * The number of squares on a side of the board.
     */
    static final int SIZE = 10;

    /**
     * Initializes a game board with SIZE squares on a side in the
     * initial position.
     */
    Board() {
        init();
    }

    /**
     * Initializes a copy of MODEL.
     */
    Board(Board model) {
        copy(model);
    }

    /**
     * Copies MODEL into me.
     */
    void copy(Board model) {
        this.board2d = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i += 1) {
            for (int j = 0; j < SIZE; j += 1) {
                this.board2d[i][j] = model.board2d[i][j];
            }
        }
        this._turn = model._turn;
        this._winner = model._winner;
    }

    /**
     * Clears the board to the initial position.
     */
    void init() {
        board2d = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i += 1) {
            for (int j = 0; j < SIZE; j += 1) {
                board2d[i][j] = Piece.EMPTY;
            }
        }
        for (int w : WHITELIST) {
            int col = w % 10;
            int row = w / 10;
            board2d[col][row] = Piece.WHITE;
        }
        for (int bl : BLACKLIST) {
            int col = bl % 10;
            int row = bl / 10;
            board2d[col][row] = Piece.BLACK;
        }
        _turn = WHITE;
        _winner = null;
    }

    /**
     * Return the Piece whose move it is (WHITE or BLACK).
     */
    Piece turn() {
        return _turn;
    }

    /**
     * Return the number of moves (that have not been undone) for this
     * board.
     */
    int numMoves() {
        return _moves;
    }

    /**
     * Return the winner in the current position, or null if the game is
     * not yet finished.
     */
    Piece winner() {
        Iterator<Move> iterator = legalMoves(_turn);
        if (!iterator.hasNext()) {
            _winner = _turn.opponent();
        }
        return _winner;
    }

    /**
     * Return the contents the square at S.
     */
    final Piece get(Square s) {
        return board2d[s.col()][s.row()];
    }

    /**
     * Return the contents of the square at (COL, ROW), where
     * 0 <= COL, ROW < 9.
     */
    /**
     * Return the contents of the square at (COL, ROW), where
     * 0 <= COL, ROW <= 9.
     */
    final Piece get(int col, int row) {
        return board2d[col][row];
    }

    /**
     * Return the contents of the square at COL ROW.
     */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /**
     * Set square S to P.
     */
    final void put(Piece p, Square s) {
        board2d[s.col()][s.row()] = p;
    }

    /**
     * Set square (COL, ROW) to P.
     */
    final void put(Piece p, int col, int row) {
        board2d[col][row] = p;
        _winner = null;
    }

    /**
     * Set square COL ROW to P.
     */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /**
     * Return true iff FROM - TO is an unblocked queen move on the current
     * board, ignoring the contents of ASEMPTY, if it is encountered.
     * For this to be true, FROM-TO must be a queen move and the
     * squares along it, other than FROM and ASEMPTY, must be
     * empty. ASEMPTY may be null, in which case it has no effect.
     */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (!from.isQueenMove(to)) {
            throw new NoSuchElementException("Not a move");
        }
        if (!get(to).equals(EMPTY) && to != asEmpty || to == null) {
            return false;
        } else {
            int direction = from.direction(to);
            Square next = from.nextsquare(direction, 1, 1);

            while (next != to) {
                if (!get(next).equals(EMPTY) && next != asEmpty) {
                    return false;
                } else {
                    next = next.nextsquare(direction, 1, 1);

                }
            }
        }
        return true;
    }


    /**
     * Return true iff FROM is a valid starting square for a move.
     */
    boolean isLegal(Square from) {
        if (from.equals(WHITE)) {
            return turn().equals(WHITE);
        }
        if (from.equals(BLACK)) {
            return turn().equals(BLACK);
        }
        return false;
    }

    /**
     * Return true iff FROM-TO is a valid first part of move, ignoring
     * spear throwing.
     */
    boolean isLegal(Square from, Square to) {
        return from.isQueenMove(to);
    }

    /**
     * Return true iff FROM-TO(SPEAR) is a legal move in the current
     * position.
     */
    boolean isLegal(Square from, Square to, Square spear) {
        return isLegal(from, to) && to.isQueenMove(spear);
    }

    /**
     * Return true iff MOVE is a legal move in the current
     * position.
     */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to(), move.spear());
    }

    /**
     * Move FROM-TO(SPEAR), assuming this is a legal move.
     */
    void makeMove(Square from, Square to, Square spear) {
        Piece curr = get(from);
        put(EMPTY, from);
        put(curr, to);
        put(SPEAR, spear);
        _moves += 1;
        if (turn().equals(WHITE)) {
            _turn = BLACK;
        } else {
            _turn = WHITE;
        }
        Move moves = Move.mv(from, to, spear);
        _stack.push(moves);
    }

    /**
     * Move according to MOVE, assuming it is a legal move.
     */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /**
     * Undo one move.  Has no effect on the initial board.
     */
    void undo() {
        Move remove = _stack.pop();
        put(Piece.EMPTY, remove.spear());
        put(get(remove.to()), remove.from());
        put(Piece.EMPTY, remove.to());
        _moves -= 1;
        _turn = _turn.opponent();
    }


    /**
     * Return an Iterator over the Squares that are reachable by an
     * unblocked queen move from FROM. Does not pay attention to what
     * piece (if any) is on FROM, nor to whether the game is finished.
     * Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     * feature is useful when looking for Moves, because after moving a
     * piece, one wants to treat the Square it came from as empty for
     * purposes of spear throwing.)
     */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /**
     * Return an Iterator over all legal moves on the current board.
     */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /**
     * Return an Iterator over all legal moves on the current board for
     * SIDE (regardless of whose turn it is).
     */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /**
     * An iterator used by reachableFrom.
     */
    private class ReachableFromIterator implements Iterator<Square> {

        /**
         * Iterator of all squares reachable by queen move from FROM,
         * treating ASEMPTY as empty.
         */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = 0;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            Square next = _from.queenMove(_dir, _steps);
            toNext();
            return next;
        }


        /**
         * Advance _dir and _steps, so that the next valid Square is
         * _steps steps in direction _dir from _from.
         */
        private void toNext() {
            if (_dir < 1) {
                _dir = 0;
            }
            _steps += 1;
            Square moveTo = _from.queenMove(_dir, _steps);
            if (moveTo != null && isUnblockedMove(_from, moveTo, _asEmpty)) {
                return;
            } else {
                _steps = 0;
                _dir += 1;
                if (hasNext()) {
                    toNext();
                }
            }
        }






        /**
        * Starting square.
        */
        private Square _from;
        /**
        * Current direction.
        */
        private int _dir;
        /**
        * Current distance.
        */
        private int _steps;
        /**
        * Square treated as empty.
        */
        private Square _asEmpty;

    }


    /**
     * An iterator used by legalMoves.
     */
    private class LegalMoveIterator implements Iterator<Move> {
        /**
         * Current Spear.
         */
        private Square _spear;
        /**
         * Square it will land at.
         */
        private Square _to;
        /**
         * Starting square.
         */
        private Square _squarestart;
        /**
         * Current Piece Color iterating through .
         */
        private Piece _fromPiece;
        /**
         * Piece's next square position .
         */
        private Square _nextSquare;
        /**
         * Starting Squares Iterator .
         */
        private Iterator<Square> _startingSquares;
        /**
         * Moves from _start to consider, using Iterator .
         */
        private Iterator<Square> _pieceMoves;
        /**
         * Spear throws from _piece to consider, using Iterator .
         */
        private Iterator<Square> _spearThrows;
        /**
         * Current starting square.
         */
        private Square _start;
        /**


        /**
         * All legal moves for SIDE (WHITE or BLACK).
         */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;

            toNext();
        }

        @Override
        public boolean hasNext() {
            return _startingSquares.hasNext();
        }


        @Override
        public Move next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Move result = Move.mv(_start, _nextSquare, _spearThrows.next());
            toNext();
            return result;

        }

        /**
         * Advance so that the next valid Move is
         * _start-_nextSquare(sp), where sp is the next value of
         * _spearThrows.
         */
        private void toNext() {
            if (!hasNext()) {
                return;
            }
            if (_spearThrows.hasNext()) {
                return;
            }
            while (!_pieceMoves.hasNext()) {
                if (!hasNext()) {
                    return;
                }
                _start = _startingSquares.next();
                while (get(_start) != _fromPiece) {
                    if (!hasNext()) {
                        return;
                    }
                    _start = _startingSquares.next();
                }
                _pieceMoves = reachableFrom(_start, null);
            }
            _nextSquare = _pieceMoves.next();
            _spearThrows = reachableFrom(_nextSquare, _start);

        }
    }





    @Override
    public String toString() {
        String newstring = "";
        for (int i = SIZE - 1; i >= 0; i -= 1) {
            newstring += "   ";
            for (int j = 0; j < SIZE; j += 1) {
                if (j == SIZE - 1) {
                    newstring += board2d[j][i];
                } else {
                    newstring += board2d[j][i] + " ";
                }
            }
            newstring += "\n";
        }
        return newstring;
    }


    /**
     * An empty iterator for initialization.
     */
    private static final Iterator<Square> NO_SQUARES =
            Collections.emptyIterator();

    /**
     * Piece whose turn it is (BLACK or WHITE).
     */
    private Piece _turn;
    /**
     * Cached value of winner on this board, or EMPTY if it has not been
     * computed.
     */
    private Piece _winner;
    /**
     * Keep count of number of moves.
     */
    private int _moves;
    /**
     * Stack to keep track of queen and spear moves.
     */
    private Stack<Move> _stack = new Stack<>();
    /**
     * Initial white queens on Board.
     */
    private static final int [] BLACKLIST = {60, 69, 93, 96};
    /**
     * Initial white queens on Board.
     */
    private static final int [] WHITELIST = {3, 6, 30, 39};



}









