package io.github.isopov.jce;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
@Measurement(time = 1, iterations = 5)
@Warmup(time = 1, iterations = 5)
@Fork(value = 3)
public abstract class AbstractBenchmark {
    @Param
    public Provider provider;

    public void setup() throws Exception {
        provider.install();
    }
}
