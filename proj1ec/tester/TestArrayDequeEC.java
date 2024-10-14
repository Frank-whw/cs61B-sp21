package tester;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void RandomTest(){
        int N = 100;
        StudentArrayDeque<Integer> std = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> right = new ArrayDequeSolution<>();
        for (int i = 0; i < N; i++) {
            int operation = StdRandom.uniform(4);
            if (operation == 0) {//Test the function of addFirst
                int value = StdRandom.uniform(100);
                std.addFirst(value);
                right.addFirst(value);
                assertEquals("After addFirst, first one is not equal!", right.get(0), std.get(0));
            } else if (operation == 1) {
                int value = StdRandom.uniform(100);
                std.addLast(value);
                right.addLast(value);
                assertEquals("After addLast, last one is not equal!",
                        right.get(right.size() - 1), std.get(std.size() - 1));
            } else if (operation == 2) {
                int expect = right.removeFirst();
                int actual = std.removeFirst();
                assertEquals("removed firstOne is not equal", expect, actual);
            } else if (operation == 3) {
                int expect = right.removeLast();
                int actual = std.removeLast();
                assertEquals("removed lastOne is not equal", expect, actual);
            }
        }
    }

}