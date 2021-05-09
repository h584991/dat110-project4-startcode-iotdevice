package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

public class RestClient {

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log";

	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message
		
		AccessMessage amess = new AccessMessage(message);
		
		Gson gson = new Gson();
		
		String jsonbody = gson.toJson(amess);
		
		try (Socket s = new Socket("localhost", 8080)) {
			
			String httppostrequest = "POST " + logpath + " HTTP/1.1\r\n" + 
			        "Host: " + "localhost" + "\r\n" +
					"Content-type: application/json\r\n" + 
			        "Content-length: " + jsonbody.length() + "\r\n" +
					"Connection: close\r\n" + 
			        "\r\n" + 
					jsonbody + 
					"\r\n";
			
			OutputStream output = s.getOutputStream();
			
			PrintWriter pw = new PrintWriter(output, false);
			pw.print(httppostrequest);
			pw.flush();
			
			InputStream in = s.getInputStream();
			
			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;
			
			while (scan.hasNext()) {
				
				String nextline = scan.nextLine();
				
				if (!header) {
					jsonresponse.append(nextline);
				}
				
				if (nextline.isEmpty()) {
					header = false;
				}		

			}

			scan.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {

		AccessCode code = null;
		
		// TODO: implement a HTTP GET on the service to get current access code
		
		try (Socket s = new Socket("localhost", 8080)) {
			
			String httpgetrequest = "GET " + codepath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
					+ "Host: localhost\r\n" + "Connection: close\r\n" + "\r\n";
			
			OutputStream output = s.getOutputStream();
			
			PrintWriter pw = new PrintWriter(output, false);
			
			pw.print(httpgetrequest);
			pw.flush();
			
			InputStream in = s.getInputStream();
			
			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;
			
			while (scan.hasNext()) {
				
				String nextline = scan.nextLine();
				
				if (!header) {
					jsonresponse.append(nextline);
				}
				
				if (nextline.isEmpty()) {
					header = false;
				}
				
			}
			
			scan.close();
			
			Gson gson = new Gson();
			
			code = gson.fromJson(jsonresponse.toString(), AccessCode.class);
		}
		
		catch (IOException ex) {
			System.err.println(ex);
		}
		
		
		return code;
	}
}
