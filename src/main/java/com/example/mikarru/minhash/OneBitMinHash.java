package com.example.mikarru.minhash;

import java.io.Serializable;
import java.util.Random;

import net.openhft.hashing.LongHashFunction;

public class OneBitMinHash {
  public static class ExactSignature implements Serializable {
    private int k;
    private long[] bitVec;
    private int featureSize;

    ExactSignature(int k, long[] bitVec, int featureSize) {
      this.k = k;
      this.bitVec = bitVec;
      this.featureSize = featureSize;
    }

    /**
     * Calculate estimated jaccard similarity.
     *
     * @param that signature with which to compare.
     * @return the estimated jaccard similarity.
     */
    public double calculateSimilarity(ExactSignature that) {
      if (getK() != that.getK()) {
        String msg = "Incomportable signatures: this.k = " +
          getK() + ", that.k = " + that.getK();
        throw new IllegalArgumentException(msg);
      }

      if (getFeatureSize() == 0 ^ that.getFeatureSize() == 0) {
        return 0.0;
      }
      if (getFeatureSize() == 0 && that.getFeatureSize() == 0) {
        return 1.0;
      }

      double r1 = (double)      getFeatureSize() / Integer.MAX_VALUE;
      double r2 = (double) that.getFeatureSize() / Integer.MAX_VALUE;

      double a1 = (1.0 - r1) / (2.0 - r1);
      double a2 = (1.0 - r2) / (2.0 - r2);

      double r1Div = r1 / (r1 + r2);
      double r2Div = r2 / (r1 + r2);

      double c1 = a1 * r2Div + a2 * r1Div;
      double c2 = a1 * r1Div + a2 * r2Div;

      double p = calculateP(that);
      return (p - c1) / (1.0 - c2);
    }

    private double calculateP(ExactSignature that) {
      int matched = 0;
      for (int i = 0; i < bitVec.length; ++i) {
        long xor = bitVec[i] ^ that.bitVec[i];
        matched += (64 - Long.bitCount(xor));
      }
      return (double) matched / k;
    }

    /**
     * Get number of hash functions from which this signature is calculated.
     */
    public int getK() {
      return k;
    }

    public int getFeatureSize() {
      return featureSize;
    }
  }

  public static class Signature implements Serializable {
    private int k;
    private long[] bitVec;

    Signature(int k, long[] bitVec) {
      this.k = k;
      this.bitVec = bitVec;
    }

    /**
     * Calculate estimated jaccard similarity.
     *
     * @param that signature with which to compare.
     * @return the estimated jaccard similarity.
     */
    public double calculateSimilarity(Signature that) {
      if (getK() != that.getK()) {
        String msg = "Incomportable signatures: this.k = " +
          getK() + ", that.k = " + that.getK();
        throw new IllegalArgumentException(msg);
      }
      double p = calculateP(that);
      return 2.0 * p - 1.0;
    }

    private double calculateP(Signature that) {
      int matched = 0;
      for (int i = 0; i < bitVec.length; ++i) {
        long xor = bitVec[i] ^ that.bitVec[i];
        matched += (64 - Long.bitCount(xor));
      }
      return (double) matched / k;
    }

    /**
     * Get number of hash functions from which this signature is calculated.
     */
    public int getK() {
      return k;
    }
  }

  private int k;
  private int arraySize;
  private LongHashFunction[] hashFuncs;

  /**
   * Constructs a OneBitMinHash with default hashSeed (0).
   *
   * @param k number of hash functions. k must be positive and multiples of 64.
   */
  public OneBitMinHash(int k) {
    this(k, 0);
  }

  /**
   * Constructs a OneBitMinHash.
   *
   * @param k number of hash functions. k must be positive and multiples of 64.
   * @param hashSeed seed of hash functions.
   */
  public OneBitMinHash(int k, long hashSeed) {
    if (k <= 0 || k % 64 != 0) {
      throw new IllegalArgumentException("k must be positive and multiples of 64");
    }
    this.k = k;

    this.arraySize = k / 64;

    Random r = new Random(hashSeed);
    hashFuncs = new LongHashFunction[k];
    for (int i = 0; i < k; ++i) {
      hashFuncs[i] = LongHashFunction.murmur_3(r.nextLong());
    }
  }

  /**
   * Calculate Signature from features.
   *
   * @param features features.
   * @return calculated signature.
   */
  public Signature calculateSignature(int[] features) {
    long[] bitVec = calculateBitVec(features);
    return new Signature(k, bitVec);
  }

  public ExactSignature calculateExactSignature(int[] features) {
    long[] bitVec = calculateBitVec(features);
    return new ExactSignature(k, bitVec, features.length);
  }

  private long[] calculateBitVec(int[] features) {
    long[] bitVec = new long[arraySize];
    int arrIndex = 0;
    int bitIndex = 0;
    for (int i = 0; i < k; ++i) {
      long min = Long.MAX_VALUE;
      for (int j = 0; j < features.length; ++j) {
        long hash = hashFuncs[i].hashInt(features[j]);
        if (hash < min) {
          min = hash;
        }
      }
      bitVec[arrIndex] |= ((min & 0x1L) << bitIndex);
      bitIndex++;
      if (bitIndex == 64) {
        arrIndex++;
        bitIndex = 0;
      }
    }
    return bitVec;
  }
}
