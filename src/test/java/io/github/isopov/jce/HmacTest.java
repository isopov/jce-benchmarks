package io.github.isopov.jce;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.crypto.Mac;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HmacTest {
    @ParameterizedTest
    @MethodSource("algorithmAndProviderVariants")
    void testGetInstance(Provider provider, HmacAlgorithm algorithm) throws NoSuchAlgorithmException {
        provider.install();

        var mac = Mac.getInstance(algorithm.toString());
        String name = mac.getProvider().getName();
        assertTrue(
                provider.allNames().contains(name),
                provider + " has no name " + name
        );

        provider.remove();
    }

    static Stream<Arguments> algorithmAndProviderVariants() {
        Stream.Builder<Arguments> argumentBuilder = Stream.builder();
        for (var provider : Provider.values()) {
            for (var algorithm : HmacAlgorithm.values()) {
                argumentBuilder.add(Arguments.of(provider, algorithm));
            }
        }
        return argumentBuilder.build();
    }


}