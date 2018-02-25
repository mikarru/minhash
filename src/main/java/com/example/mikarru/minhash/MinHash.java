package com.example.mikkaru.minhash;

import java.io.Serializable;
import java.util.Random;

import net.openhft.hashing.LongHashFunction;


public class MinHash {
  public static class Signature implements Serializable {
    private final long[] mins;

    Signature (long[] mins) {
      this.mins = mins;
    }

    public double calculateSimilarity(Signature that) {
      if (getK() != that.getK()) {
        String msg = "Incomportable signatures: this.k = " +
          getK() + ", that.k = " + that.getK();
        throw new IllegalArgumentException(msg);
      }

      int k = getK();
      int matched = 0;
      for (int i = 0; i < k; ++i) {
        if (mins[i] == that.mins[i]) {
          matched++;
        }
      }
      return (double) matched / k;
    }

    public int getK() {
      return mins.length;
    }
  }

  private int k;
  private LongHashFunction[] hashFuncs;

  public MinHash(int k) {
    this(k, 0);
  }

  public MinHash(int k, long hashSeed) {
    if (k <= 0) {
      throw new IllegalArgumentException("k must be positive");
    }
    this.k = k;
    Random r = new Random(hashSeed);
    hashFuncs = new LongHashFunction[k];
    for (int i = 0; i < k; ++i) {
      hashFuncs[i] = LongHashFunction.murmur_3(r.nextLong());
    }
  }

  public Signature calculateSigneture(int[] features) {
    long[] mins = new long[k];
    for (int i = 0; i < k; ++i) {
      long min = Long.MAX_VALUE;
      for (int j = 0; j < features.length; ++j) {
        long hash = hashFuncs[i].hashInt(features[j]);
        if (hash < min) {
          min = hash;
        }
      }
      mins[i] = min;
    }
    return new Signature(mins);
  }
}
