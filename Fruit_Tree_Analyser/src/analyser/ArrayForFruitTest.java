package analyser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayForFruitTest {
    ArrayForFruit fruitArray;

    @BeforeEach
    void createArray(){
        fruitArray = new ArrayForFruit(3,3);
        fruitArray.array[0] = -1;
        fruitArray.array[1] = 1;
        fruitArray.array[2] = 2;
        fruitArray.array[3] = 3;
        fruitArray.array[4] = 4;
        fruitArray.array[5] = -1;
        fruitArray.array[6] = -1;
        fruitArray.array[7] = -1;
        fruitArray.array[8] = -1;
    }

    @Test
    void addFruitToArray() {
        fruitArray.addFruitToArray(2, 1);
        assertEquals(5, fruitArray.array[fruitArray.calculateArrayPosition(1, 2)], "addFruitToArray() not adding fruits to array correctly.");
    }

    @Test
    void addNonFruitToArray() {
        fruitArray.addNonFruitToArray(2, 1);
        assertEquals(-1, fruitArray.array[fruitArray.calculateArrayPosition(1, 2)], "addNonFruitToArray() not adding fruits to array correctly!");
    }

    @Test
    void calculateArrayPosition() {
        assertEquals(11, fruitArray.calculateArrayPosition(3, 2), "calculateArrayPosition() calculation not correct!");
    }

    @Test
    void unionFruitPixels() {
        fruitArray.unionFruitPixels();
        assertEquals(1, fruitArray.array[3], "unionFruitPixels() not union-ing same-set pixels correctly!");
    }

    @Test
    void pixelIsWhite() {
        assertTrue(fruitArray.pixelIsWhite(4), "pixelIsWhite() is not recognising white pixels correctly!");
    }

    @Test
    void atTopEdge() {
        assertTrue(fruitArray.atTopEdge(1), "atTopEdge() is not recognizing a pixel at the top edge of an image (2D array) correctly");
    }

    @Test
    void atRightEdge() {
        assertTrue(fruitArray.atRightEdge(5), "atRightEdge() is not recognizing a pixel at the right edge of an image (2D array) correctly");
    }

    @Test
    void atBottomEdge() {
        assertTrue(fruitArray.atBottomEdge(7), "atBottomEdge() is not recognizing a pixel at the bottom edge of an image (2D array) correctly");
    }

    @Test
    void atLeftEdge() {
        assertTrue(fruitArray.atLeftEdge(3), "atLeftEdge() is not recognizing a pixel at the left edge of an image (2D array) correctly");
    }

    @Test
    void unionPixels() {
        fruitArray.unionPixels(1,4);
        assertEquals(1, fruitArray.array[4], "unionPixels() is not unioning two pixels correctly!");
    }

    @Test
    void quickUnion() {
        fruitArray.quickUnion(fruitArray.array, 1,2);
        assertEquals(1, fruitArray.array[2], "quickUnion() is not working correctly!");
    }

    @Test
    void find() {
        fruitArray.unionFruitPixels();
        assertEquals(1, fruitArray.find(fruitArray.array, 4), "find() is not correctly finding a disjoint set's parent node!" );
    }
}