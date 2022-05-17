package io.github.isopov.jce;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SignatureTest {
    @ParameterizedTest
    @MethodSource("algorithmAndProviderVariants")
    void testProviderHasAlgorithm(Provider provider, SignatureAlgorithm algorithm) throws NoSuchAlgorithmException {
        provider.install();

        var signature = Signature.getInstance(algorithm.toString());
        String name = signature.getProvider().getName();
        assertTrue(
                provider.allNames().contains(name),
                provider + " has not name " + name
        );

        provider.remove();
    }

    @ParameterizedTest
    @MethodSource("algorithmAndProviderVariants")
    void testKeyPairAlgorithm(Provider provider, SignatureAlgorithm algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        provider.install();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm.keyPairAlgorithm);
        KeyPair keyPair = kpg.generateKeyPair();
        var privateKey = keyPair.getPrivate();
        var publicKey = keyPair.getPublic();

        var signature = Signature.getInstance(algorithm.toString());
        signature.initSign(privateKey);
        signature.initVerify(publicKey);

        provider.remove();
    }

    static Stream<Arguments> algorithmAndProviderVariants() {
        Stream.Builder<Arguments> argumentBuilder = Stream.builder();
        for (var provider : Provider.values()) {
            for (var algorithm : SignatureAlgorithm.values()) {
                argumentBuilder.add(Arguments.of(provider, algorithm));
            }
        }
        return argumentBuilder.build();
    }


}