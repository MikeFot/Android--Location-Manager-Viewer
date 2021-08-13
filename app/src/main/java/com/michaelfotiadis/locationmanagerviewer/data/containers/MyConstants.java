package com.michaelfotiadis.locationmanagerviewer.data.containers;

import java.text.DecimalFormat;

/**
 * Class for storing constants used throughout the app
 * 
 * @author Michael Fotiadis
 * 
 */
public class MyConstants {

	public static final DecimalFormat df = new DecimalFormat("#.00");
	;
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	;
	public static final String ACTION_AIRPLANE_MODE = "android.intent.action.AIRPLANE_MODE";
	;

	public enum Broadcasts {
		BROADCAST_NETWORK_STATE_CHANGED("Brodacast_1"), // Network State Changed
		BROADCAST_GPS_CHANGED("Broadcast_2"),
		BROADCAST_NMEA_CHANGED("Broadcast_3"), // NMEA Changed
		BROADCAST_NETWORK_CHANGED("Broadcast_4"),
		BROADCAST_PASSIVE_CHANGED("Broadcast_5"),
		BROADCAST_GPS_STATE_CHANGED("Broadcast_6");

		private String text;
		Broadcasts(String description) {
			text = description;
		}

		public String getString() {
			return text;
		}

	}

	;

	public enum Payloads {
		PAYLOAD_1("Payload_1"),
		PAYLOAD_2("Payload_2"),
		PAYLOAD_3("Payload_3"),
		PAYLOAD_4("Payload_4"),
		PAYLOAD_5("Payload_5");

		private String text;
		Payloads(String description) {
			text = description;
		}

		public String getString() {
			return text;
		}
	}

	;

	public enum Results {
		RESULT_1("Result_1"),
		RESULT_2("Result_2"),
		RESULT_3("Result_3");

		private String text;
		Results(String description) {
			text = description;
		}

		public String getString() {
			return text;
		}
	}

	public enum Requests {
		REQUEST_CODE_1(1),
		REQUEST_CODE_2(2),
		REQUEST_CODE_3(3);

		private int code;
		Requests(int number) {
			code = number;
		}

		public int getCode() {
			return code;
		}
	}

	public enum FragmentCode {
		FRAGMENT_CODE_0(6000),
		FRAGMENT_CODE_GPS(6001),
		FRAGMENT_CODE_NETWORK(6002),
		FRAGMENT_CODE_PASSIVE(6003),
		FRAGMENT_CODE_SATELLITES(6004),
		FRAGMENT_CODE_NMEA(6005);

		private int code;
		FragmentCode(int number) {
			code = number;
		}

		public int getCode() {
			return code;
		}
	}

}
