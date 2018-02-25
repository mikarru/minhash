package com.example.mikkaru.minhash;

import java.io.Serializable;
import java.util.Random;

import net.openhft.hashing.LongHashFunction;

public class BBitMinHash {
  public static class Signature implements Serializable {
    private int k;
    private int b;
    private long[] bitVec;
    private int featureSize;

    Signature(int k, int b, long[] bitVec, int featureSize) {
      this.k = k;
      this.b = b;
      this.bitVec = bitVec;
      this.featureSize = featureSize;
    }

    public double calculateSimilarity(Signature that) {
      if (getK() != that.getK() || getB() != that.getB()) {
        String msg = "Incomportable signatures: ";
        msg += "(this.k, this.b) = (" + getK() + ", " + getB() +"), ";
        msg += "(that.k, that.b) = (" + that.getK() + ", " + that.getB() +")";
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

      double a1 = calculateA(r1);
      double a2 = calculateA(r2);

      double r1Div = r1 / (r1 + r2);
      double r2Div = r2 / (r1 + r2);

      double c1 = a1 * r2Div + a2 * r1Div;
      double c2 = a1 * r1Div + a2 * r2Div;

      double p = calculateP(that);
      return (p - c1) / (1.0 - c2);
    }

    private double calculateA(double r) {
      return (r * Math.pow(1.0 - r, 2 * getB() - 1)) / (1.0 - Math.pow(1.0 - r, 2 * getB()));
    }

    private double calculateP(Signature that) {
      long bitMask = calculateBitMask(getB());
      int matched = 0;
      for (int i = 0; i < bitVec.length; ++i) {
        long xor = bitVec[i] ^ that.bitVec[i];
        for (int j = 0; j < 64; j += getB()) {
          if (((xor >>> j) & bitMask) == 0L) {
            matched++;
          }
        }
      }
      return (double) matched / k;
    }

    public int getK() {
      return k;
    }

    public int getB() {
      return b;
    }

    public int getFeatureSize() {
      return featureSize;
    }
  }

  static boolean isPowerOfTwo(int n) {
    return Integer.bitCount(n) == 1;
  }

  static long calculateBitMask(int b) {
    long mask = 0L;
    for (int i = 0; i < b; ++i) {
      mask <<= 1;
      mask |= 0x1L;
    }
    return mask;
  }

  public BBitMinHash(int k, int b) {
    this(k, b, 0);
  }

  private int k;
  private int b;
  private int arraySize;
  private LongHashFunction[] hashFuncs;

  public BBitMinHash(int k, int b, long hashSeed) {
    if (k <= 0) {
      throw new IllegalArgumentException("k must be positive");
    }
    this.k = k;
    if (b <= 0 || b > 64 || !isPowerOfTwo(b)) {
      throw new IllegalArgumentException("b must be > 0 and <= 64 and power of 2");
    }
    this.b = b;

    int p = k * b;
    if (p % 64 != 0) {
      throw new IllegalArgumentException("k * b must be multiples of 64");
    }
    this.arraySize = p / 64;

    Random r = new Random(hashSeed);
    hashFuncs = new LongHashFunction[k];
    for (int i = 0; i < k; ++i) {
      hashFuncs[i] = LongHashFunction.murmur_3(r.nextLong());
    }
  }

  public Signature calculateSigneture(int[] features) {
    long[] bitVec = new long[arraySize];
    int arrIndex = 0;
    int bitIndex = 0;
    long mask = calculateBitMask(b);
    for (int i = 0; i < k; ++i) {
      long min = Long.MAX_VALUE;
      for (int j = 0; j < features.length; ++j) {
        long hash = hashFuncs[i].hashInt(features[j]);
        if (hash < min) {
          min = hash;
        }
      }
      bitVec[arrIndex] |= ((min & mask) << bitIndex);
      bitIndex += b;
      if (bitIndex == 64) {
        arrIndex++;
        bitIndex = 0;
      }
    }
    return new Signature(k, b, bitVec, features.length);
  }
}
