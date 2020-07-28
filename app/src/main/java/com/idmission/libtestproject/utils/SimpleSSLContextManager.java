package com.idmission.libtestproject.utils;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by sanketg on 14/02/19.
 */

public class SimpleSSLContextManager {

    public static final String TLS = "TLS";
    public static final String SSL = "SSL";

    public SimpleSSLContextManager() {
    }

    public static SSLContext getNonCheckingSSLContext(String protocol) {
        try {
            SSLContext e = SSLContext.getInstance(protocol);
            X509TrustManager x509tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            };
            TrustManager[] trustAllCerts = new TrustManager[]{x509tm};
            e.init((KeyManager[])null, trustAllCerts, (SecureRandom)null);
            return e;
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static SSLContext getNonCheckingSSLContext() {
        return getNonCheckingSSLContext("TLS");
    }

}
