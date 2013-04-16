package com.deepslice.manager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkManager {

	public static String requestServer() throws Exception {
		String urlServer = "http://182.72.88.40:8080/DocomoServer/initialReq.action?version=1.5&clientOs=RIM%20OS&width=360&height=480&resendVarificationCode=false&msisdn=919391801281";
		String response = "";

		HttpURLConnection connection = null;

		URL url = new URL(urlServer);
		connection = (HttpURLConnection) url.openConnection();

		// Allow Inputs & Outputs
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		/*
		 * connection.setConnectTimeout(settings.CONNECT_TIMEOUT);
		 * connection.setReadTimeout(settings.READ_TIMEOUT);
		 */

		// Enable POST method
		connection.setRequestMethod("POST");

		/*
		 * OutputStreamWriter wr = new OutputStreamWriter(
		 * connection.getOutputStream()); // wr.write(data); wr.flush();
		 */

		InputStream is = connection.getInputStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		response = builder.toString();

		return response;
	}

}
