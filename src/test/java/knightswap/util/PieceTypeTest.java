package knightswap.util;

import knightswap.utils.PieceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTypeTest {
    @Test
    void testGetSymbol() {
        assertEquals('L', PieceType.LIGHT.getSymbol(), "LIGHT piece symbol should be 'L'.");
        assertEquals('D', PieceType.DARK.getSymbol(), "DARK piece symbol should be 'D'.");
    }

    @Test
    void testOpponentOfLight() {
        assertEquals(PieceType.DARK, PieceType.LIGHT.opponent(), "Opponent of LIGHT should be DARK.");
    }

    @Test
    void testOpponentOfDark() {
        assertEquals(PieceType.LIGHT, PieceType.DARK.opponent(), "Opponent of DARK should be LIGHT.");
    }

    @Test
    void testOpponentIsConsistent() {
        assertEquals(PieceType.LIGHT, PieceType.LIGHT.opponent().opponent(), "Opponent of opponent of LIGHT should be LIGHT.");
        assertEquals(PieceType.DARK, PieceType.DARK.opponent().opponent(), "Opponent of opponent of DARK should be DARK.");
    }
}