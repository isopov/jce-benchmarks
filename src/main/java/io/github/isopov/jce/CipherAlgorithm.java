package io.github.isopov.jce;

import javax.crypto.spec.GCMParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public enum CipherAlgorithm {
    //TODO Any other cipher supported in all providers?
    AESGCMNoPadding("AES/GCM/NoPadding", "AES") {
        final RandomGenerator randomGenerator = RandomGeneratorFactory.getDefault().create();

        @Override
        AlgorithmParameterSpec parameterSpec() {
            byte[] iv = new byte[12];
            randomGenerator.nextBytes(iv);
            return new GCMParameterSpec(128, iv);
        }
    };

    final String name;
    final String keyAlgorithm;

    CipherAlgorithm(String name, String keyAlgorithm) {
        this.name = name;
        this.keyAlgorithm = keyAlgorithm;
    }

    abstract AlgorithmParameterSpec parameterSpec();
}
