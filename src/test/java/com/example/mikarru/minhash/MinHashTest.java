package com.example.mikkaru.minhash;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MinHashTest {
  private static final double DELTA = 1.0e-2;

  @Test
  public void similarityTest() {
    int k = 10000; // use large k to minimize error rate
    MinHash mh = new MinHash(k);

    int[] f1 = {0, 1, 2};
    int[] f2 = {0, 1, 2};
    MinHash.Signature s1 = mh.calculateSigneture(f1);
    MinHash.Signature s2 = mh.calculateSigneture(f2);
    assertEquals(1.0, s1.calculateSimilarity(s2), DELTA);

    int[] f3 = {0, 1, 2, 3, 4, 5, 6};
    int[] f4 = {0, 1, 2};
    MinHash.Signature s3 = mh.calculateSigneture(f3);
    MinHash.Signature s4 = mh.calculateSigneture(f4);
    assertEquals((double)3 / (3 + 4), s3.calculateSimilarity(s4), DELTA);

    int[] f5 = {0, 1, 2, 3, 4, 5, 6};
    int[] f6 = {0, 1, 2, 7, 8, 9, 10};
    MinHash.Signature s5 = mh.calculateSigneture(f5);
    MinHash.Signature s6 = mh.calculateSigneture(f6);
    assertEquals((double)3 / (3 + 4 + 4), s5.calculateSimilarity(s6), DELTA);

    int[] f7 = {};
    int[] f8 = {0, 1, 2};
    MinHash.Signature s7 = mh.calculateSigneture(f7);
    MinHash.Signature s8 = mh.calculateSigneture(f8);
    assertEquals((double)0 / 3, s7.calculateSimilarity(s8), DELTA);

    int[] f9 = {};
    int[] f10 = {};
    MinHash.Signature s9 = mh.calculateSigneture(f9);
    MinHash.Signature s10 = mh.calculateSigneture(f10);
    assertEquals(1.0, s9.calculateSimilarity(s10), DELTA);
  }
}
