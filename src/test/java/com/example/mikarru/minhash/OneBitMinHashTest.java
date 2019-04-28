package com.example.mikarru.minhash;

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
    OneBitMinHash.Signature s1 = mh.calculateSignature(f1);
    OneBitMinHash.Signature s2 = mh.calculateSignature(f2);
    assertEquals(1.0, s1.calculateSimilarity(s2), DELTA);

    int[] f3 = {0, 1, 2, 3, 4, 5, 6};
    int[] f4 = {0, 1, 2};
    OneBitMinHash.Signature s3 = mh.calculateSignature(f3);
    OneBitMinHash.Signature s4 = mh.calculateSignature(f4);
    assertEquals((double)3 / (3 + 4), s3.calculateSimilarity(s4), DELTA);

    int[] f5 = {0, 1, 2, 3, 4, 5, 6};
    int[] f6 = {0, 1, 2, 7, 8, 9, 10};
    OneBitMinHash.Signature s5 = mh.calculateSignature(f5);
    OneBitMinHash.Signature s6 = mh.calculateSignature(f6);
    assertEquals((double)3 / (3 + 4 + 4), s5.calculateSimilarity(s6), DELTA);

    int[] f7 = {};
    int[] f8 = {0, 1, 2};
    OneBitMinHash.Signature s7 = mh.calculateSignature(f7);
    OneBitMinHash.Signature s8 = mh.calculateSignature(f8);
    assertEquals((double)0 / 3, s7.calculateSimilarity(s8), DELTA);

    int[] f9 = {};
    int[] f10 = {};
    OneBitMinHash.Signature s9 = mh.calculateSignature(f9);
    OneBitMinHash.Signature s10 = mh.calculateSignature(f10);
    assertEquals(1.0, s9.calculateSimilarity(s10), DELTA);
  }

  @Test
  public void exactSimilarityTest() {
    int k = 1024 * 16; // use large k to minimize error rate
    OneBitMinHash mh = new OneBitMinHash(k);

    int[] f1 = {0, 1, 2};
    int[] f2 = {0, 1, 2};
    OneBitMinHash.ExactSignature s1 = mh.calculateExactSignature(f1);
    OneBitMinHash.ExactSignature s2 = mh.calculateExactSignature(f2);
    assertEquals(1.0, s1.calculateSimilarity(s2), DELTA);

    int[] f3 = {0, 1, 2, 3, 4, 5, 6};
    int[] f4 = {0, 1, 2};
    OneBitMinHash.ExactSignature s3 = mh.calculateExactSignature(f3);
    OneBitMinHash.ExactSignature s4 = mh.calculateExactSignature(f4);
    assertEquals((double)3 / (3 + 4), s3.calculateSimilarity(s4), DELTA);

    int[] f5 = {0, 1, 2, 3, 4, 5, 6};
    int[] f6 = {0, 1, 2, 7, 8, 9, 10};
    OneBitMinHash.ExactSignature s5 = mh.calculateExactSignature(f5);
    OneBitMinHash.ExactSignature s6 = mh.calculateExactSignature(f6);
    assertEquals((double)3 / (3 + 4 + 4), s5.calculateSimilarity(s6), DELTA);

    int[] f7 = {};
    int[] f8 = {0, 1, 2};
    OneBitMinHash.ExactSignature s7 = mh.calculateExactSignature(f7);
    OneBitMinHash.ExactSignature s8 = mh.calculateExactSignature(f8);
    assertEquals((double)0 / 3, s7.calculateSimilarity(s8), DELTA);

    int[] f9 = {};
    int[] f10 = {};
    OneBitMinHash.ExactSignature s9 = mh.calculateExactSignature(f9);
    OneBitMinHash.ExactSignature s10 = mh.calculateExactSignature(f10);
    assertEquals(1.0, s9.calculateSimilarity(s10), DELTA);
  }
}
