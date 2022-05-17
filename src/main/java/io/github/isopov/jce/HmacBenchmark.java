package io.github.isopov.jce;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacBenchmark  extends AbstractBenchmark{
    @Param
    public HmacAlgorithm algorithm;

    private Mac mac;
    private static final byte[] MESSAGE = "StringToAuthenticate".getBytes();

    @Setup
    public void setup() throws Exception {
        super.setup();
        SecretKeySpec key = new SecretKeySpec("SecretKey".getBytes(), algorithm.toString());
        mac = Mac.getInstance(algorithm.toString());
        mac.init(key);
    }

    @Benchmark
    public byte[] authenticate() {
        return mac.doFinal(MESSAGE);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + HmacBenchmark.class.getSimpleName() + ".*")
                .build();

        new Runner(opt).run();
    }
}
