# minhash

This is a simple implementation of MinHash and b-bit MinHash.

## How to use

### MinHash

```java
import com.example.mikkaru.minhash.MinHash;

int k = 64;
MinHash mh = new MinHash(k);
int[] features1 = {0, 1, 2, 3, 4, 5, 6, 7};
int[] features2 = {0, 1, 2, 3, 4, 5, 9};
MinHash.Signature s1 = mh.calculateSignature(features1);
MinHash.Signature s2 = mh.calculateSignature(features2);

double sim = s1.calculateSimilarity(s2);
System.out.println(sim); // => 0.640625. Actual jaccard similarity is 6/9 = 0.66666...
```

### b-bit MinHash

```java
import com.example.mikkaru.minhash.BBitMinHash;

int k = 256;
int b = 4;
BBitMinHash mh = new BBitMinHash(k, b);
int[] features1 = {0, 1, 2, 3, 4, 5, 6, 7};
int[] features2 = {0, 1, 2, 3, 4, 5, 9};
BBitMinHash.Signature s1 = mh.calculateSignature(features1);
BBitMinHash.Signature s2 = mh.calculateSignature(features2);

double sim = s1.calculateSimilarity(s2);
System.out.println(sim); // => 0.598214. Actual jaccard similarity is 6/9 = 0.66666...
```

Specialized implementation of b-bit MinHash with b = 1.  
This implementation is more efficient than that of non specialized version with b = 1.

```java
import com.example.mikkaru.minhash.OneBitMinHash;

int k = 256;
OneBitMinHash mh = new OneBitMinHash(k);
int[] features1 = {0, 1, 2, 3, 4, 5, 6, 7};
int[] features2 = {0, 1, 2, 3, 4, 5, 9};
OneBitMinHash.Signature s1 = mh.calculateSignature(features1);
OneBitMinHash.Signature s2 = mh.calculateSignature(features2);

double sim = s1.calculateSimilarity(s2);
System.out.println(sim); // => 0.640625. Actual jaccard similarity is 6/9 = 0.66666...
```

###
