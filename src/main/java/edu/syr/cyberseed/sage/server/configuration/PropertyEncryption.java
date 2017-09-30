package edu.syr.cyberseed.sage.server.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import java.security.Security;
import java.util.Properties;

//@Configuration
public class PropertyEncryption {

    private static final Logger logger = LoggerFactory.getLogger(PropertyEncryption.class);

    //@Bean
    Properties encryptedProperties() {

        Security.addProvider(new BouncyCastleProvider());
        StandardPBEStringEncryptor BCencryptor = new StandardPBEStringEncryptor();
        BCencryptor.setProviderName("BC");
        BCencryptor.setAlgorithm("PBEWITHSHA256AND256BITAES-GCM-BC");
        BCencryptor.setPassword(System.getenv("APPLICATION_KEY"));

        Properties props = new EncryptableProperties(BCencryptor);
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
        }
        catch (Exception e) {
            logger.error("Unable to load property file. Make sure the classpath -cp includes it.");
        }

        return props;
    }
}
