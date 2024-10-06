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
    public void testSquarePrimesSimple1() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimple2() {
        IntList lst = IntList.of(14, 15, 16, 19, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 361 -> 18", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesSimple3() {
        IntList lst = IntList.of(14, 15, 16, 1, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 1 -> 18", lst.toString());
        assertTrue(!changed);
    }
    @Test
    public void testSquarePrimesSimple4() {
        IntList lst = IntList.of(14, 15, 16, 12, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 12 -> 18", lst.toString());
        assertTrue(!changed);
    }
//    @Test
//    public void testSquarePrimesSimple5() {
//        IntList lst = IntList.of();
//        boolean changed = IntListExercises.squarePrimes(lst);
//        assertEquals("", lst.toString());
//        assertTrue(!changed);
//    }
    @Test
    public void testSquarePrimesSimple6() {
        IntList lst = IntList.of(2,3,7,11);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 9 -> 49 -> 121", lst.toString());
        assertTrue(changed);
    }
}
