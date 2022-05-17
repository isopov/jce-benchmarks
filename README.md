On a laptop with 
```
$ java -version
openjdk version "17.0.3" 2022-04-19 LTS
OpenJDK Runtime Environment Corretto-17.0.3.6.1 (build 17.0.3+6-LTS)
OpenJDK 64-Bit Server VM Corretto-17.0.3.6.1 (build 17.0.3+6-LTS, mixed mode, sharing)
```
incomplete run with a single fork
```
Benchmark                       (algorithm)    (provider)   Mode  Cnt        Score        Error  Units
CipherBenchmark.decrypt     AESGCMNoPadding       DEFAULT  thrpt    5  4214935.788 ± 144479.311  ops/s
CipherBenchmark.decrypt     AESGCMNoPadding       CORRETO  thrpt    5  2315781.057 ±  34763.562  ops/s
CipherBenchmark.decrypt     AESGCMNoPadding  BOUNCYCASTLE  thrpt    5  1874305.893 ± 168999.854  ops/s
CipherBenchmark.decrypt     AESGCMNoPadding     CONSCRYPT  thrpt    5  1891528.698 ±  71080.118  ops/s
CipherBenchmark.encrypt     AESGCMNoPadding       DEFAULT  thrpt    5  2461553.226 ± 270172.128  ops/s
CipherBenchmark.encrypt     AESGCMNoPadding       CORRETO  thrpt    5  2332806.920 ±  44838.404  ops/s
CipherBenchmark.encrypt     AESGCMNoPadding  BOUNCYCASTLE  thrpt    5   638919.883 ±  32252.775  ops/s
CipherBenchmark.encrypt     AESGCMNoPadding     CONSCRYPT  thrpt    5  1589276.172 ±  31367.268  ops/s
HmacBenchmark.authenticate       HmacSHA256       DEFAULT  thrpt    5  1199891.667 ±   5026.875  ops/s
HmacBenchmark.authenticate       HmacSHA256       CORRETO  thrpt    5  1175700.028 ±  63082.417  ops/s
HmacBenchmark.authenticate       HmacSHA256  BOUNCYCASTLE  thrpt    5   799733.006 ±  61601.102  ops/s
HmacBenchmark.authenticate       HmacSHA256     CONSCRYPT  thrpt    5   341470.572 ± 118606.780  ops/s
SignatureBenchmark.sign     SHA256withECDSA       DEFAULT  thrpt    5     1575.215 ±    132.594  ops/s
SignatureBenchmark.sign     SHA256withECDSA       CORRETO  thrpt    5     1139.977 ±     19.726  ops/s
SignatureBenchmark.sign     SHA256withECDSA  BOUNCYCASTLE  thrpt    5      467.313 ±     29.691  ops/s
SignatureBenchmark.sign     SHA256withECDSA     CONSCRYPT  thrpt    5    38495.015 ±   6206.362  ops/s
SignatureBenchmark.sign       SHA256withRSA       DEFAULT  thrpt    5      872.613 ±    145.693  ops/s
SignatureBenchmark.sign       SHA256withRSA       CORRETO  thrpt    5     1794.809 ±    193.243  ops/s
SignatureBenchmark.sign       SHA256withRSA  BOUNCYCASTLE  thrpt    5      825.337 ±     24.632  ops/s
SignatureBenchmark.sign       SHA256withRSA     CONSCRYPT  thrpt    5     1779.308 ±      5.199  ops/s
SignatureBenchmark.verify   SHA256withECDSA       DEFAULT  thrpt    5      887.665 ±     16.053  ops/s
SignatureBenchmark.verify   SHA256withECDSA       CORRETO  thrpt    5     1279.282 ±    131.693  ops/s
SignatureBenchmark.verify   SHA256withECDSA  BOUNCYCASTLE  thrpt    5     1006.267 ±    308.475  ops/s
SignatureBenchmark.verify   SHA256withECDSA     CONSCRYPT  thrpt    5    18657.265 ±   2856.331  ops/s
SignatureBenchmark.verify     SHA256withRSA       DEFAULT  thrpt    5    24541.620 ±    113.764  ops/s
SignatureBenchmark.verify     SHA256withRSA       CORRETO  thrpt    5    56475.926 ±   4067.343  ops/s
SignatureBenchmark.verify     SHA256withRSA  BOUNCYCASTLE  thrpt    5    25627.597 ±   2089.206  ops/s
SignatureBenchmark.verify     SHA256withRSA     CONSCRYPT  thrpt    5    60672.508 ±   7478.417  ops/s

```


