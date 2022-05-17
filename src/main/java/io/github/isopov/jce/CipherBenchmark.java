package io.github.isopov.jce;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class CipherBenchmark extends AbstractBenchmark {
    @Param
    public CipherAlgorithm algorithm;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    static final byte[] MESSAGE = "Text to encrypt".getBytes();
    private byte[] encrypted;
    private SecretKey key;

    @Setup
    public void setup() throws Exception {
        super.setup();

        var keyGen = KeyGenerator.getInstance(algorithm.keyAlgorithm);
        keyGen.init(256);
        key = keyGen.generateKey();

        var parameterSpec = algorithm.parameterSpec();

        encryptCipher = Cipher.getInstance(algorithm.name);
        encryptCipher.init(ENCRYPT_MODE, key, parameterSpec);
        encrypted = encryptCipher.doFinal(MESSAGE);

        decryptCipher = Cipher.getInstance(algorithm.name);
        decryptCipher.init(DECRYPT_MODE, key, parameterSpec);
    }

    @Benchmark
    public byte[] encrypt() throws Exception {
        encryptCipher.init(ENCRYPT_MODE, key, algorithm.parameterSpec());
        return encryptCipher.doFinal(MESSAGE);
    }

    @Benchmark
    public byte[] decrypt() throws Exception {
        return decryptCipher.doFinal(encrypted);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + CipherBenchmark.class.getSimpleName() + ".*")
                .build();

        new Runner(opt).run();
    }

}
