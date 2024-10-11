package deque;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    /*public void RandomTest1(){
        MaxArrayDeque<Integer> items = new MaxArrayDeque<>();
        int N = 1000;
        int operation = StdRandom.uniform(6);
        if(operation == 0){
            int value = StdRandom.uniform(1000);
            items.addFirst(value);
        }else if(operation == 1){
            int value = StdRandom.uniform(1000);
            items.addLast(value);
        }else if(operation == 2){
            items.removeFirst();
        }else if(operation == 3){
            items.removeLast();
        }else if(operation == 4){
            System.out.println(items.size());
        }else{
            items.printDeque();
        }

    }*/
    @Test
    public void maxWithoutComparatorTest() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new IntComparator());

        for (int i = 0; i < 5; i++) {
            mad.addLast(i);
        }

        assertEquals((Integer) 4, mad.max());
    }

    @Test
    public void maxWithComparatorTest() {
        MaxArrayDeque<String> mad = new MaxArrayDeque<>(new StringComparator());

        mad.addLast("Java is good!");
        mad.addLast("java is good");

        assertEquals("java is good", mad.max());
        assertEquals("Java is good!", mad.max(new StringLengthComparator()));
    }

    private static class IntComparator implements Comparator<Integer>{
        @Override
        public int compare(Integer i1, Integer i2){
            return i1 - i2;
        }
    }
    private static class StringLengthComparator implements Comparator<String>{
        @Override
        public int compare(String s1, String s2){
            return s1.length() - s2.length();
        }
    }
    private static class StringComparator implements Comparator<String>{
        @Override
        public int compare(String s1, String s2){
            return s1.charAt(0) - s2.charAt(0);
        }
    }

}