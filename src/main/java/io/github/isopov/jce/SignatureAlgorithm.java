package io.github.isopov.jce;

public enum SignatureAlgorithm {
    SHA1withECDSA("EC"),
    SHA256withECDSA("EC"),
    SHA512withECDSA("EC"),
    //MD5withRSA("RSA"), not available in corretto
    SHA1withRSA("RSA"),
    SHA256withRSA("RSA"),
    SHA512withRSA("RSA");

    final String keyPairAlgorithm;

    SignatureAlgorithm(String keyPairAlgorithm) {
        this.keyPairAlgorithm = keyPairAlgorithm;
    }
}
