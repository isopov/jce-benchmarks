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

import java.nio.ByteBuffer;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class CipherBenchmark extends AbstractBenchmark {
    @Param
    public CipherAlgorithm algorithm;
    @Param
    public CipherArgument argument;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private Object sourceMessage;
    private Object encryptedMessage;
    static final int ENCRYPTED_SIZE = 31;
    static final String TEXT_TO_ENCRYPT = "Text to encrypt";
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
        decryptCipher = Cipher.getInstance(algorithm.name);
        decryptCipher.init(DECRYPT_MODE, key, parameterSpec);

        switch (argument) {
            case ARRAY -> {
                sourceMessage = TEXT_TO_ENCRYPT.getBytes();
                encryptedMessage = encryptCipher.doFinal((byte[]) sourceMessage);
            }
            case DIRECT_BUFFER -> {
                sourceMessage = ByteBuffer.allocateDirect(TEXT_TO_ENCRYPT.length());
                ((ByteBuffer) sourceMessage).put(TEXT_TO_ENCRYPT.getBytes());
                ((ByteBuffer) sourceMessage).flip();
                encryptedMessage = ByteBuffer.allocateDirect(ENCRYPTED_SIZE);
                int bar = encryptCipher.doFinal(((ByteBuffer) sourceMessage).duplicate(), (ByteBuffer) encryptedMessage);
                if (ENCRYPTED_SIZE != bar) {
                    throw new IllegalStateException(bar + "");
                }
                ((ByteBuffer) encryptedMessage).flip();
            }
            case HEAP_BUFFER -> {
                sourceMessage = ByteBuffer.allocate(TEXT_TO_ENCRYPT.length());
                ((ByteBuffer) sourceMessage).put(TEXT_TO_ENCRYPT.getBytes());
                ((ByteBuffer) sourceMessage).flip();
                encryptedMessage = ByteBuffer.allocate(ENCRYPTED_SIZE);
                int foo = encryptCipher.doFinal(((ByteBuffer) sourceMessage).duplicate(), (ByteBuffer) encryptedMessage);
                if (ENCRYPTED_SIZE != foo) {
                    throw new IllegalStateException(foo + "");
                }
                ((ByteBuffer) encryptedMessage).flip();
            }
            default -> throw new IllegalStateException();
        }
    }

    @Benchmark
    public Object encrypt() throws Exception {
        encryptCipher.init(ENCRYPT_MODE, key, algorithm.parameterSpec());
        switch (argument) {
            case ARRAY:
                return encryptCipher.doFinal((byte[]) sourceMessage);
            case DIRECT_BUFFER:
                var dBuf = ByteBuffer.allocateDirect(ENCRYPTED_SIZE);
                encryptCipher.doFinal(((ByteBuffer) sourceMessage).duplicate(), dBuf);
                return dBuf;
            case HEAP_BUFFER:
                var hBuf = ByteBuffer.allocate(ENCRYPTED_SIZE);
                encryptCipher.doFinal(((ByteBuffer) sourceMessage).duplicate(), hBuf);
                return hBuf;
            default:
                throw new IllegalStateException();
        }

    }

    @Benchmark
    public Object decrypt() throws Exception {
        switch (argument) {
            case ARRAY:
                return decryptCipher.doFinal((byte[]) encryptedMessage);
            case DIRECT_BUFFER:
                var dBuf = ByteBuffer.allocateDirect(TEXT_TO_ENCRYPT.length());
                decryptCipher.doFinal(((ByteBuffer) encryptedMessage).duplicate(), dBuf);
                return dBuf;
            case HEAP_BUFFER:
                var hBuf = ByteBuffer.allocate(TEXT_TO_ENCRYPT.length());
                decryptCipher.doFinal(((ByteBuffer) encryptedMessage).duplicate(), hBuf);
                return hBuf;
            default:
                throw new IllegalStateException();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + CipherBenchmark.class.getSimpleName() + ".*")
                .build();

        new Runner(opt).run();
    }

}
