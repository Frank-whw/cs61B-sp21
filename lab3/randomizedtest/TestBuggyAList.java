package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
  @Test//md when we use @Test, we shou  ld add parathesis under this line
  public void randomizedTest1(){
      AListNoResizing<Integer> L = new AListNoResizing<>();

      int N = 10000;
      for (int i = 0; i < N; i += 1) {
          int operationNumber = StdRandom.uniform(0, 4);//实际上是左闭右开
          if (operationNumber == 0) {
              // addLast
              int randVal = StdRandom.uniform(0, 100);
              L.addLast(randVal);
              //System.out.println("addLast(" + randVal + ")");
          } else if (operationNumber == 1) {
              // size
              int size = L.size();
              //System.out.println("size: " + size);
          }
          else if (operationNumber == 2){
              //getLast if the size is great than 0
              if(L.size() > 0) {
                  int getNum = L.getLast();
                  //System.out.println("getLast(" + getNum + ")");
              }
          }
          else{
              if(L.size() > 0) {
                  int removedNum = L.removeLast();
                  //System.out.println("removeLast(" + removedNum + ")");
              }
          }
      }
  }
}

