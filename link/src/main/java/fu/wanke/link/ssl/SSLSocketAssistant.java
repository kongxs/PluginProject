package fu.wanke.link.ssl;



import android.util.Log;

import java.net.SocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import fu.wanke.link.Loger;


public class SSLSocketAssistant {

    private static final String TAG = SSLSocketAssistant.class.getName();

    public SSLSocketAssistant() {
    }

    public static SSLSocket createSSLSocket() throws SSLSocketException {
        SSLSocket sslSocket = null;
        KeyStore ks = null;
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            // If mTrustStore is null, the system default store will be used
            trustManagerFactory.init(ks);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // No client authentication
//            TrustManager[] trustManagers = new TrustManager[] {new M()};
//            System.out.println("trustManagers -=--- " + trustManagers.length);
            
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket();
            sslSocket.setUseClientMode(true);
            // CONNECT_FAIL almost certainly means a timeout
        } catch (Throwable e) {
            throw new SSLSocketException(e);
        }
        return sslSocket;
    }

    public static void connect(SSLSocket socket, SocketAddress sslAddr, int iTimeout, String ip) throws Exception {
        if (socket == null || sslAddr == null || iTimeout <= 0) {
            return;
        }

        long start = System.currentTimeMillis();
        socket.connect(sslAddr, iTimeout);
        long connect = System.currentTimeMillis();
        // If there is no exception here, the server certificate is trusted
        socket.startHandshake();
        // verify host name
        HostnameVerifier hostnameVerifier = new JdHostnameVerifier();

        if (!hostnameVerifier.verify(ip, socket.getSession())) {
            //X509Certificate cert = (X509Certificate) unverifiedHandshake.peerCertificates().get(0);
            throw new SSLPeerUnverifiedException("Hostname " + " not verified.");
        }

        long finish = System.currentTimeMillis();

        Loger.get().debug("ssl","SSL connection establish time costs ************************"
                + "\n tcp connect  costs : " + (connect - start)
                + "\n ssl handshake costs : " + (finish - connect)
                + "\n total costs : " + (finish - start)
                + "\n *****************************************");
    }
}
