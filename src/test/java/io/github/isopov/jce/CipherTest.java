package io.github.isopov.jce;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.crypto.Cipher;
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

    @ParameterizedTest
    @MethodSource("algorithmAndProviderVariants")
    public void testBenchmark(Provider provider, CipherAlgorithm algorithm) throws Exception {
        var benchmark = new CipherBenchmark();
        benchmark.provider = provider;
        benchmark.algorithm = algorithm;
        assertDoesNotThrow(benchmark::setup);
        assertDoesNotThrow(benchmark::encrypt);
        assertArrayEquals(CipherBenchmark.MESSAGE, benchmark.decrypt());
        //to cleanup state
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

}