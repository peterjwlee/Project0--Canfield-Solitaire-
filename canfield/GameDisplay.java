package canfield;

import ucb.gui.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

import java.io.InputStream;
import java.io.IOException;

/** A widget that displays a Pinball playfield.
 *  @author P. N. Hilfinger
 */
class GameDisplay extends Pad {

    /** Color of display field. */
    private static final Color BACKGROUND_COLOR = Color.green;

    /* Coordinates and lengths in pixels unless otherwise stated. */

    /** Preferred dimensions of the playing surface. */
    private static final int BOARD_WIDTH = 800, BOARD_HEIGHT = 600;

    /** Displayed dimensions of a card image. */
    private static final int CARD_HEIGHT = 125, CARD_WIDTH = 90;

    /** Coordinates of cards. */
    private static final int START_COORDINATE = 380,
        RESERVE_COORDINATE = 255, WASTE_XCOORDINATE = 200,
        WASTE_YCOORDINATE = 450, TABLEAU_YCOORDINATE = 255,
        STOCK_YCOORDINATE = 450;

    /** A graphical representation of GAME. */
    public GameDisplay(Game game) {
        _game = game;
        setPreferredSize(BOARD_WIDTH, BOARD_HEIGHT);
    }

    /** Return an Image read from the resource named NAME. */
    private Image getImage(String name) {
        InputStream in =
            getClass().getResourceAsStream("/canfield/resources/" + name);
        try {
            return ImageIO.read(in);
        } catch (IOException excp) {
            return null;
        }
    }

    /** Return an Image of CARD. */
    private Image getCardImage(Card card) {
        return getImage("playing-cards/" + card + ".png");
    }

    /** Return an Image of the back of a card. */
    private Image getBackImage() {
        return getImage("playing-cards/blue-back.png");
    }

    /** Draw CARD at X, Y on G. */
    private void paintCard(Graphics2D g, Card card, int x, int y) {
        if (card != null) {
            g.drawImage(getCardImage(card), x, y,
                        CARD_WIDTH, CARD_HEIGHT, null);
        }
    }

    /** Draw card back at X, Y on G. */
    private void paintBack(Graphics2D g, int x, int y) {
        g.drawImage(getBackImage(), x, y, CARD_WIDTH, CARD_HEIGHT, null);
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        Rectangle b = g.getClipBounds();
        g.fillRect(0, 0, b.width, b.height);
        paintCard(g, _game.topReserve(), 100, RESERVE_COORDINATE);
        for (int i = 1, start = START_COORDINATE;
             i <= 4; i += 1, start += 100) {
            paintCard(g, _game.topFoundation(i), start, 100);
        }
        paintCard(g, _game.topWaste(), WASTE_XCOORDINATE, WASTE_YCOORDINATE);
        for (int i = 1, start = START_COORDINATE;
             i <= 4; i += 1, start += 100) {
            paintCard(g, _game.topTableau(i), start, TABLEAU_YCOORDINATE);
        }
        paintBack(g, 100, STOCK_YCOORDINATE);
    }

    /** Game I am displaying. */
    private final Game _game;

}

