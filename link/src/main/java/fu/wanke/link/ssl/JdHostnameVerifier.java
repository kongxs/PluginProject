package fu.wanke.link.ssl;

import android.text.TextUtils;


import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import fu.wanke.link.Constants;

public class JdHostnameVerifier implements HostnameVerifier{


	String [] HOSTS = new String[]{
			"11.40.67.232" ,//long
			"11.25.27.14" , //
			Constants.IP_LONG,
			Constants.IP_SHORT,
	};


	@Override
	public boolean verify(String hostname, SSLSession session) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(hostname)) {
			return false;
		}
		return Arrays.asList(HOSTS).contains(hostname);
	}

}
