package com.example.mikkaru.minhash;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OneBitMinHashTest {
  private static final double DELTA = 1.0e-2;

  @Test
  public void similarityTest() {
    int k = 1024 * 16; // use large k to minimize error rate
    OneBitMinHash mh = new OneBitMinHash(k);

    int[] f1 = {0, 1, 2};
    int[] f2 = {0, 1, 2};
    OneBitMinHash.Signature s1 = mh.calculateSigneture(f1);
    OneBitMinHash.Signature s2 = mh.calculateSigneture(f2);
    assertEquals(1.0, s1.calculateSimilarity(s2), DELTA);

    int[] f3 = {0, 1, 2, 3, 4, 5, 6};
    int[] f4 = {0, 1, 2};
    OneBitMinHash.Signature s3 = mh.calculateSigneture(f3);
    OneBitMinHash.Signature s4 = mh.calculateSigneture(f4);
    assertEquals((double)3 / (3 + 4), s3.calculateSimilarity(s4), DELTA);

    int[] f5 = {0, 1, 2, 3, 4, 5, 6};
    int[] f6 = {0, 1, 2, 7, 8, 9, 10};
    OneBitMinHash.Signature s5 = mh.calculateSigneture(f5);
    OneBitMinHash.Signature s6 = mh.calculateSigneture(f6);
    assertEquals((double)3 / (3 + 4 + 4), s5.calculateSimilarity(s6), DELTA);

    int[] f7 = {};
    int[] f8 = {0, 1, 2};
    OneBitMinHash.Signature s7 = mh.calculateSigneture(f7);
    OneBitMinHash.Signature s8 = mh.calculateSigneture(f8);
    assertEquals((double)0 / 3, s7.calculateSimilarity(s8), DELTA);

    int[] f9 = {};
    int[] f10 = {};
    OneBitMinHash.Signature s9 = mh.calculateSigneture(f9);
    OneBitMinHash.Signature s10 = mh.calculateSigneture(f10);
    assertEquals(1.0, s9.calculateSimilarity(s10), DELTA);
  }

  @Test
  public void exactSimilarityTest() {
    int k = 1024 * 16; // use large k to minimize error rate
    OneBitMinHash mh = new OneBitMinHash(k);

    int[] f1 = {0, 1, 2};
    int[] f2 = {0, 1, 2};
    OneBitMinHash.ExactSignature s1 = mh.calculateExactSigneture(f1);
    OneBitMinHash.ExactSignature s2 = mh.calculateExactSigneture(f2);
    assertEquals(1.0, s1.calculateSimilarity(s2), DELTA);

    int[] f3 = {0, 1, 2, 3, 4, 5, 6};
    int[] f4 = {0, 1, 2};
    OneBitMinHash.ExactSignature s3 = mh.calculateExactSigneture(f3);
    OneBitMinHash.ExactSignature s4 = mh.calculateExactSigneture(f4);
    assertEquals((double)3 / (3 + 4), s3.calculateSimilarity(s4), DELTA);

    int[] f5 = {0, 1, 2, 3, 4, 5, 6};
    int[] f6 = {0, 1, 2, 7, 8, 9, 10};
    OneBitMinHash.ExactSignature s5 = mh.calculateExactSigneture(f5);
    OneBitMinHash.ExactSignature s6 = mh.calculateExactSigneture(f6);
    assertEquals((double)3 / (3 + 4 + 4), s5.calculateSimilarity(s6), DELTA);

    int[] f7 = {};
    int[] f8 = {0, 1, 2};
    OneBitMinHash.ExactSignature s7 = mh.calculateExactSigneture(f7);
    OneBitMinHash.ExactSignature s8 = mh.calculateExactSigneture(f8);
    assertEquals((double)0 / 3, s7.calculateSimilarity(s8), DELTA);

    int[] f9 = {};
    int[] f10 = {};
    OneBitMinHash.ExactSignature s9 = mh.calculateExactSigneture(f9);
    OneBitMinHash.ExactSignature s10 = mh.calculateExactSigneture(f10);
    assertEquals(1.0, s9.calculateSimilarity(s10), DELTA);
  }
}
