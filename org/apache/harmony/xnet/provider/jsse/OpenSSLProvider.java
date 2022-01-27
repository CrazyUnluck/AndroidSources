/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.harmony.xnet.provider.jsse;

import java.security.Provider;

public final class OpenSSLProvider extends Provider {
    public static final String PROVIDER_NAME = "AndroidOpenSSL";

    public OpenSSLProvider() {
        super(PROVIDER_NAME, 1.0, "Android's OpenSSL-backed security provider");

        // SSL Contexts
        put("SSLContext.SSL", OpenSSLContextImpl.class.getName());
        put("SSLContext.SSLv3", OpenSSLContextImpl.class.getName());
        put("SSLContext.TLS", OpenSSLContextImpl.class.getName());
        put("SSLContext.TLSv1", OpenSSLContextImpl.class.getName());
        put("SSLContext.TLSv1.1", OpenSSLContextImpl.class.getName());
        put("SSLContext.TLSv1.2", OpenSSLContextImpl.class.getName());
        put("SSLContext.Default", DefaultSSLContextImpl.class.getName());

        // Message Digests
        put("MessageDigest.SHA-1",
            "org.apache.harmony.xnet.provider.jsse.OpenSSLMessageDigestJDK$SHA1");
        put("Alg.Alias.MessageDigest.SHA1", "SHA-1");
        put("Alg.Alias.MessageDigest.SHA", "SHA-1");
        put("Alg.Alias.MessageDigest.1.3.14.3.2.26", "SHA-1");

        put("MessageDigest.SHA-256",
            "org.apache.harmony.xnet.provider.jsse.OpenSSLMessageDigestJDK$SHA256");
        put("Alg.Alias.MessageDigest.SHA256", "SHA-256");
        put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.1", "SHA-256");

        put("MessageDigest.SHA-384",
            "org.apache.harmony.xnet.provider.jsse.OpenSSLMessageDigestJDK$SHA384");
        put("Alg.Alias.MessageDigest.SHA384", "SHA-384");
        put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.2", "SHA-384");

        put("MessageDigest.SHA-512",
            "org.apache.harmony.xnet.provider.jsse.OpenSSLMessageDigestJDK$SHA512");
        put("Alg.Alias.MessageDigest.SHA512", "SHA-512");
        put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.3", "SHA-512");

        put("MessageDigest.MD5",
            "org.apache.harmony.xnet.provider.jsse.OpenSSLMessageDigestJDK$MD5");
        put("Alg.Alias.MessageDigest.1.2.840.113549.2.5", "MD5");

        // KeyPairGenerators
        put("KeyPairGenerator.RSA", OpenSSLRSAKeyPairGenerator.class.getName());
        put("Alg.Alias.KeyPairGenerator.1.2.840.113549.1.1.1", "RSA");

        put("KeyPairGenerator.DSA", OpenSSLDSAKeyPairGenerator.class.getName());

        // KeyFactory

        put("KeyFactory.RSA", OpenSSLRSAKeyFactory.class.getName());
        put("Alg.Alias.KeyFactory.1.2.840.113549.1.1.1", "RSA");

        // put("KeyFactory.DSA", OpenSSLDSAKeyFactory.class.getName());

        // Signatures
        put("Signature.MD5WithRSA", OpenSSLSignature.MD5RSA.class.getName());
        put("Alg.Alias.Signature.MD5WithRSAEncryption", "MD5WithRSA");
        put("Alg.Alias.Signature.MD5/RSA", "MD5WithRSA");
        put("Alg.Alias.Signature.1.2.840.113549.1.1.4", "MD5WithRSA");
        put("Alg.Alias.Signature.1.2.840.113549.2.5with1.2.840.113549.1.1.1", "MD5WithRSA");

        put("Signature.SHA1WithRSA", OpenSSLSignature.SHA1RSA.class.getName());
        put("Alg.Alias.Signature.SHA1WithRSA", "SHA1WithRSA");
        put("Alg.Alias.Signature.SHA1/RSA", "SHA1WithRSA");
        put("Alg.Alias.Signature.SHA-1/RSA", "SHA1WithRSA");
        put("Alg.Alias.Signature.1.2.840.113549.1.1.5", "SHA1WithRSA");
        put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.113549.1.1.1", "SHA1WithRSA");
        put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.113549.1.1.5", "SHA1WithRSA");
        put("Alg.Alias.Signature.1.3.14.3.2.29", "SHA1WithRSA");

        put("Signature.SHA256WithRSA", OpenSSLSignature.SHA256RSA.class.getName());
        put("Alg.Alias.Signature.SHA256WithRSAEncryption", "SHA256WithRSA");
        put("Alg.Alias.Signature.1.2.840.113549.1.1.11", "SHA256WithRSA");

        put("Signature.SHA384WithRSA", OpenSSLSignature.SHA384RSA.class.getName());
        put("Alg.Alias.Signature.SHA384WithRSAEncryption", "SHA384WithRSA");
        put("Alg.Alias.Signature.1.2.840.113549.1.1.12", "SHA384WithRSA");

        put("Signature.SHA512WithRSA", OpenSSLSignature.SHA512RSA.class.getName());
        put("Alg.Alias.Signature.SHA512WithRSAEncryption", "SHA512WithRSA");
        put("Alg.Alias.Signature.1.2.840.113549.1.1.13", "SHA512WithRSA");

        put("Signature.SHA1withDSA", OpenSSLSignature.SHA1DSA.class.getName());
        put("Alg.Alias.Signature.SHA/DSA", "SHA1withDSA");
        put("Alg.Alias.Signature.DSA", "SHA1withDSA");
        put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.10040.4.1", "SHA1withDSA");
        put("Alg.Alias.Signature.1.3.14.3.2.26with1.2.840.10040.4.3", "SHA1withDSA");
        put("Alg.Alias.Signature.DSAWithSHA1", "SHA1withDSA");
        put("Alg.Alias.Signature.1.2.840.10040.4.3", "SHA1withDSA");

        put("Signature.NONEwithRSA", OpenSSLSignatureRawRSA.class.getName());

        // SecureRandom
        /*
         * We have to specify SHA1PRNG because various documentation mentions
         * that algorithm by name instead of just recommending calling
         * "new SecureRandom()"
         */
        put("SecureRandom.SHA1PRNG", OpenSSLRandom.class.getName());
        put("SecureRandom.SHA1PRNG ImplementedIn", "Software");

        // Cipher
        put("Cipher.RSA/ECB/NoPadding", OpenSSLCipherRSA.Raw.class.getName());
        put("Alg.Alias.Cipher.RSA/None/NoPadding", "RSA/ECB/NoPadding");
        put("Cipher.RSA/ECB/PKCS1Padding", OpenSSLCipherRSA.PKCS1.class.getName());
        put("Alg.Alias.Cipher.RSA/None/PKCS1Padding", "RSA/ECB/PKCS1Padding");
    }
}
