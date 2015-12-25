package canfield;

import static org.junit.Assert.*;
import org.junit.Test;

/** Tests of the Game class.
 *  @author Peter Lee
 */

public class GameTest {

    /**
     * Example.
     */
    @Test
    public void testInitialScore() {
        Game g = new Game();
        g.deal();
        assertEquals(5, g.getScore());
    }

    private void assertGamesEqual(Game g1, Game g2) {
        assertEquals(g1.topReserve(), g2.topReserve());
        assertEquals(g1.getScore(), g2.getScore());
        assertEquals(g1.topWaste(), g2.topWaste());
        assertEquals(g1.topTableau(1), g2.topTableau(1));
        assertEquals(g1.topTableau(2), g2.topTableau(2));
        assertEquals(g1.topTableau(3), g2.topTableau(3));
        assertEquals(g1.topTableau(4), g2.topTableau(4));
        assertEquals(g1.tableauSize(1), g2.tableauSize(1));
        assertEquals(g1.tableauSize(2), g2.tableauSize(2));
        assertEquals(g1.tableauSize(3), g2.tableauSize(3));
        assertEquals(g1.tableauSize(4), g2.tableauSize(4));
        assertEquals(g1.topFoundation(1), g2.topFoundation(1));
        assertEquals(g1.topFoundation(2), g2.topFoundation(2));
        assertEquals(g1.topFoundation(3), g2.topFoundation(3));
        assertEquals(g1.topFoundation(4), g2.topFoundation(4));
        assertEquals(g1.foundationSize(1), g2.foundationSize(1));
        assertEquals(g1.foundationSize(2), g2.foundationSize(2));
        assertEquals(g1.foundationSize(3), g2.foundationSize(3));
        assertEquals(g1.foundationSize(4), g2.foundationSize(4));
    }

    /**
     * Tests the method undo.
     */
    @Test
    public void testUndo() {
        Game g = new Game();
        g.deal();
        Game answer = new Game(g);
        g.stockToWaste();
        g.undo();
        assertGamesEqual(g, answer);
        g = new Game();
        g.seed(3);
        g.deal();
        Game answer0 = new Game(g);
        g.tableauToFoundation(4);
        Game answer1 = new Game(g);
        g.reserveToTableau(3);
        Game answer2 = new Game(g);
        g.stockToWaste();
        Game answer3 = new Game(g);
        g.stockToWaste();
        Game answer4 = new Game(g);
        g.stockToWaste();
        Game answer5 = new Game(g);
        g.stockToWaste();
        Game answer6 = new Game(g);
        g.stockToWaste();
        Game answer7 = new Game(g);
        g.stockToWaste();
        Game answer8 = new Game(g);
        g.wasteToTableau(2);
        Game answer9 = new Game(g);
        g.undo();
        assertGamesEqual(g, answer8);
        g.undo();
        assertGamesEqual(g, answer7);
        g.undo();
        assertGamesEqual(g, answer6);
        g.undo();
        assertGamesEqual(g, answer5);
        g.undo();
        assertGamesEqual(g, answer4);
        g.undo();
        assertGamesEqual(g, answer3);
        g.undo();
        assertGamesEqual(g, answer2);
        g.undo();
        assertGamesEqual(g, answer1);
        g.undo();
        assertGamesEqual(g, answer0);
        g.undo();
        assertGamesEqual(g, answer0);

    }
}
