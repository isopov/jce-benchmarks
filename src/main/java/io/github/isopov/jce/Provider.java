package io.github.isopov.jce;

import com.amazon.corretto.crypto.provider.AmazonCorrettoCryptoProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.conscrypt.OpenSSLProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.Set;

public enum Provider {
    DEFAULT("SunEC", "SunRsaSign", "SunJCE") {
        void install() {
            //nothing
        }

        void remove() {
            //nothing
        }
    },
    CORRETO(AmazonCorrettoCryptoProvider.PROVIDER_NAME) {
        void install() {
            AmazonCorrettoCryptoProvider.install();
        }
    },
    BOUNCYCASTLE(BouncyCastleProvider.PROVIDER_NAME) {
        void install() {
            Security.insertProviderAt(new BouncyCastleProvider(), 1);
        }
    },
    CONSCRYPT("Conscrypt") {
        void install() {
            Security.insertProviderAt(new OpenSSLProvider(name), 1);
        }
    };

    final String name;
    final Set<String> additionalNames;

    Provider(String name) {
        this(name, new String[0]);
    }

    Provider(String name, String... additionalNames) {
        this.name = name;
        this.additionalNames = Set.of(additionalNames);
    }

    abstract void install();

    void remove() {
        Security.removeProvider(name);
    }

    Set<String> allNames() {
        if (additionalNames.isEmpty()) {
            return Set.of(name);
        }
        var result = new ArrayList<>(additionalNames);
        result.add(name);
        return Set.copyOf(result);
    }
}
