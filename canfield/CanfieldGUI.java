package canfield;

import ucb.gui.TopLevel;
import ucb.gui.LayoutSpec;

import java.awt.event.MouseEvent;
import java.awt.Point;

/** A top-level GUI for Canfield solitaire.
 *  @author Peter Lee
 */
class CanfieldGUI extends TopLevel {
    /** This is the point that saves
     * the first click for any move. */
    private Point firstClick;

    /** Coordinates of cards. */
    private static final int START_COORDINATE = 380,
            RESERVE_COORDINATE = 255, LOWEST_WASTE_XCOORDINATE = 200,
            LOWEST_WASTE_YCOORDINATE = 450, HIGHEST_WASTE_XCOORDINATE = 290,
            HIGHEST_WASTE_YCOORDINATE = 575,
            FOUNDATION_LOWXCOORDINATE = 380, FOUNDATION_HIGHXCOORD = 770,
            FOUNDATION_LOWYCOORD = 100, FOUNDATION_HIGHYCOORD = 225,
            TABLEAU_LOWXCOORD = 380, TABLEAU_HIGHXCOORD = 770,
            TABLEAU_LOWYCOORD = 255, TABLEAU_HIGHYCOORD = 380,
            RESERVE_LOWXCOORD = 100, RESERVE_HIGHXCOORD = 190,
            RESERVE_LOWYCOORD = 255, RESERVE_HIGHYCOORD = 380,
            STOCK_LOWXCOORD = 100, STOCK_HIGHXCOORD = 190,
            STOCK_LOWYCOORD = 450, STOCK_HIGHYCOORD = 575;


    /** A new window with given TITLE and displaying GAME. */
    CanfieldGUI(String title, Game game) {
        super(title, true);
        _game = game;
        addLabel("Sorry, no graphical interface yet",
                new LayoutSpec("y", 0, "x", 0));
        addButton("Quit", "quit", new LayoutSpec("y", 0, "x", 1));
        addButton("Undo", "undo", new LayoutSpec("y", 0, "x", 2));
        addButton("New Game", "deal", new LayoutSpec("y", 0, "x", 3));
        _display = new GameDisplay(game);
        add(_display, new LayoutSpec("y", 2, "width", 2));
        _display.setMouseHandler("click", this, "mouseClicked");
        display(true);
    }

    /** Respond to "Quit" button. */
    public void quit(String dummy) {
        System.exit(1);
    }

    /** Respond to "Undo" button.*/
    public void undo(String dummy) {
        _game.undo();
        _display.repaint();
    }

    /** Respond to "New Game" button.*/
    public void deal(String dummy) {
        _game.deal();
        _display.repaint();
    }

    /**
     * @param x a x-coordinate
     * @param y a y-coordinate
     * @return whether x and y coordinates
     * are in the waste pile.
     */
    private boolean isWastePile(int x, int y) {
        return LOWEST_WASTE_XCOORDINATE <= x
                && x <= HIGHEST_WASTE_XCOORDINATE
                && LOWEST_WASTE_YCOORDINATE <= y
                && y <= HIGHEST_WASTE_YCOORDINATE;
    }

    /**
     * @param x a x-coordinate
     * @param y a y-coordinate
     * @return whether x and y coordinates
     * are in the foundation pile.
     */
    private boolean isFoundationPile(int x, int y) {
        return FOUNDATION_LOWXCOORDINATE <= x
                && x <= FOUNDATION_HIGHXCOORD
                && FOUNDATION_LOWYCOORD <= y
                && y <= FOUNDATION_HIGHYCOORD;
    }

    /**
     * @param x a x-coordinate
     * @param y a y-coordinate
     * @return whether x and y coordinates
     * are in the tableau pile.
     */
    private boolean isTableauPile(int x, int y) {
        return TABLEAU_LOWXCOORD <= x
                && x <= TABLEAU_HIGHXCOORD
                && TABLEAU_LOWYCOORD <= y
                && y <= TABLEAU_HIGHYCOORD;
    }

    /**
     * @param x a x-coordinate
     * @param y a y-coordinate
     * @return whether x and y coordinates
     * are in the reserve pile.
     */
    private boolean isReservePile(int x, int y) {
        return RESERVE_LOWXCOORD <= x
                && x <= RESERVE_HIGHXCOORD
                && RESERVE_LOWYCOORD <= y
                && y <= RESERVE_HIGHYCOORD;
    }

    /** Action in response to mouse-clicking event EVENT. Please make sure
     * to do a precise first click or method will not execute.*/
    public synchronized void mouseClicked(MouseEvent event) {
        if (event.getID() != MouseEvent.MOUSE_CLICKED) {
            return;
        }
        int x = event.getX(), y = event.getY();
        if (STOCK_LOWXCOORD <= x && x <= STOCK_HIGHXCOORD
                && STOCK_LOWYCOORD <= y && y <= STOCK_HIGHYCOORD) {
            _game.stockToWaste();
        } else {
            if (firstClick != null && (isTableauPile(x, y)
                    || isFoundationPile(x, y))) {
                int firstX = (int) firstClick.getX(),
                        firstY = (int) firstClick.getY();
                if (isWastePile(firstX, firstY)) {
                    if (isFoundationPile(x, y)) {
                        _game.wasteToFoundation();
                    } else if (isTableauPile(x, y)) {
                        _game.wasteToTableau(pileTracker(x));
                    }
                } else if (isReservePile(firstX, firstY)) {
                    if (isFoundationPile(x, y)) {
                        _game.reserveToFoundation();
                    } else if (isTableauPile(x, y)) {
                        _game.reserveToTableau(pileTracker(x));
                    }
                } else if (isTableauPile(firstX, firstY)) {
                    if (isFoundationPile(x, y)) {
                        _game.tableauToFoundation(pileTracker(firstX));
                    } else if (isTableauPile(x, y)) {
                        _game.tableauToTableau(
                                pileTracker(firstX),
                                pileTracker(x)
                        );
                    }
                } else if (isFoundationPile(firstX, firstY)) {
                    if (isTableauPile(x, y)) {
                        _game.foundationToTableau(
                                pileTracker(firstX),
                                pileTracker(x)
                        );
                    }
                }
                firstClick = null;
            } else {
                saveClick(x, y);
            }
        }
        _display.repaint();
    }

    /**
     * @param x a x-coordinate
     * @param y a y-coordinate
     * Saves the x and y
     * coordinates of the click into firstClick.
     */
    private void saveClick(int x, int y) {
        if (isWastePile(x, y) || isReservePile(x, y)
                || isTableauPile(x, y) || isFoundationPile(x, y)) {
            firstClick = new Point(x, y);
        } else {
            firstClick = null;
        }
    }

    /**
     * @param x a pile number
     * @return the card pile number within
     * tableau and foundation and keeps track of
     * the card pile number as well.
     */
    private int pileTracker(int x) {
        return 1 + (x - START_COORDINATE) / 100;
    }

    /** The board widget. */
    private final GameDisplay _display;

    /** The game I am consulting. */
    private final Game _game;

}
