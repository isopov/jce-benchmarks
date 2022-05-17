package io.github.isopov.jce;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class SignatureBenchmark  extends AbstractBenchmark{
    @Param
    public SignatureAlgorithm algorithm;

    private Signature signature;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private final byte[] text = "Sample String which we want to sign".getBytes(StandardCharsets.ISO_8859_1);
    private byte[] sign;

    @Setup
    public void setup() throws Exception {
        super.setup();
        // create a key pair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm.keyPairAlgorithm);
        KeyPair keyPair = kpg.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        signature = Signature.getInstance(algorithm.toString());
        signature.initSign(privateKey);
        signature.update(text);
        sign = this.signature.sign();
    }

    @Benchmark
    public byte[] sign() throws Exception {
        signature.initSign(privateKey);
        signature.update(text);
        return signature.sign();
    }

    @Benchmark
    public boolean verify() throws Exception {
        signature.initVerify(publicKey);
        signature.update(text);
        return signature.verify(sign);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + SignatureBenchmark.class.getSimpleName() + ".*")
                .build();

        new Runner(opt).run();
    }
}
