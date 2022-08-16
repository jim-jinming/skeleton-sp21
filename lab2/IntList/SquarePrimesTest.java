package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimes1() {
        IntList lst = IntList.of(2, 6, 8, 10, 12);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 6 -> 8 -> 10 -> 12", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimes2() {
        IntList lst = IntList.of(1, 3, 5, 7, 9);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("1 -> 9 -> 25 -> 49 -> 9", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimes3() {
        IntList lst = IntList.of(2, 4, 6, 8, 10);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 4 -> 6 -> 8 -> 10", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimes4() {
        IntList lst = IntList.of(2, 6, 8, 10, 1);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 6 -> 8 -> 10 -> 1", lst.toString());
        assertTrue(changed);
    }
}
