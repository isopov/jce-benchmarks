package io.github.isopov.jce;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CipherTest {
    @ParameterizedTest
    @MethodSource("algorithmAndProviderVariants")
    void testAlgorithm(Provider provider, CipherAlgorithm algorithm) throws GeneralSecurityException {
        provider.install();

        var cipher = Cipher.getInstance(algorithm.name);
        String name = cipher.getProvider().getName();
        assertTrue(
                provider.allNames().contains(name),
                provider + " has not name " + name
        );

        provider.remove();
    }

    static Stream<Arguments> algorithmAndProviderVariants() {
        Stream.Builder<Arguments> argumentBuilder = Stream.builder();
        for (var provider : Provider.values()) {
            for (var algorithm : CipherAlgorithm.values()) {
                argumentBuilder.add(Arguments.of(provider, algorithm));

            }
        }
        return argumentBuilder.build();
    }

    @ParameterizedTest
    @MethodSource("algorithmProviderAndArgumentVariants")
    public void testBenchmark(Provider provider, CipherAlgorithm algorithm, CipherArgument argument) throws Exception {
        var benchmark = new CipherBenchmark();
        benchmark.provider = provider;
        benchmark.algorithm = algorithm;
        benchmark.argument = argument;
        assertDoesNotThrow(benchmark::setup);
        assertDoesNotThrow(benchmark::encrypt);
        switch (argument) {
            case ARRAY -> assertArrayEquals(CipherBenchmark.TEXT_TO_ENCRYPT.getBytes(), (byte[]) benchmark.decrypt());
            case HEAP_BUFFER, DIRECT_BUFFER -> {
                var bytes = new byte[CipherBenchmark.TEXT_TO_ENCRYPT.length()];
                ((ByteBuffer) benchmark.decrypt()).flip().get(bytes);
                assertArrayEquals(CipherBenchmark.TEXT_TO_ENCRYPT.getBytes(), bytes);
            }
            default -> throw new IllegalStateException();
        }

        //to cleanup state
        provider.remove();
    }

    static Stream<Arguments> algorithmProviderAndArgumentVariants() {
        Stream.Builder<Arguments> argumentBuilder = Stream.builder();
        for (var provider : Provider.values()) {
            for (var algorithm : CipherAlgorithm.values()) {
                for (var argument : CipherArgument.values()) {
                    argumentBuilder.add(Arguments.of(provider, algorithm, argument));
                }
            }
        }
        return argumentBuilder.build();
    }

}