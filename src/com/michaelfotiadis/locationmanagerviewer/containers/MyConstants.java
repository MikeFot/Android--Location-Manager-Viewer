package com.michaelfotiadis.locationmanagerviewer.containers;

import java.text.DecimalFormat;

/**
 * Class for storing constants used throughout the app
 * 
 * @author Michael Fotiadis
 * 
 */
public class MyConstants {

	public enum Broadcasts {
		BROADCAST_1("Brodacast_1"),
		BROADCAST_2("Broadcast_2"),
		BROADCAST_3("Broadcast_3"),
		BROADCAST_4("Broadcast_4"), 
		BROADCAST_5("Broadcast_5");

		private String text;
		Broadcasts(String description) {
			text = description;
		}

		public String getString() {
			return text;
		}

	}; 

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
	}; 

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
	}; 

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
	}; 

	public enum FragmentCode {
		FRAGMENT_CODE_0(6000),
		FRAGMENT_CODE_1(6001),
		FRAGMENT_CODE_2(6002),
		FRAGMENT_CODE_3(6003),
		FRAGMENT_CODE_4(6004);

		private int code;
		FragmentCode(int number) {
			code = number;
		}

		public int getCode() {
			return code;
		}
	}; 
	
	public static final DecimalFormat df = new DecimalFormat("#.00");

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static final String ACTION_AIRPLANE_MODE = "android.intent.action.AIRPLANE_MODE";

}
