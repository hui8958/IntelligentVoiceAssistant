package edu.hui.vassistant.supports;

import java.util.Random;

public class DefaultResponse {

	public  static String playSongReponse(String name) {

		Random r = new Random();
		int index = r.nextInt(3);
		switch (index) {
		case 0:
			return "Playing song " + name + ".";

		case 1:
			return "Playing song " + name + ", I wish you like it.";

		case 2:
			return "I think you like " + name + ".";
		default:
			return "";
		}

	}
	public static String getCameraSuccessSpeak() {
		Random r = new Random();
		int time = r.nextInt(3);
		switch (time) {
		case 0:

			return "Got it!";
		case 1:
			return "Nice smile!";
		case 2:
			return "Wonderful!";
		default:
			return "Good!";
		}
	}
	public static String getTranslateSuccessSpeak() {
		Random r = new Random();
		int time = r.nextInt(3);
		switch (time) {
		case 0:

			return "Ok, here you go!";
		case 1:
			return "I got it!";
		case 2:
			return "Translation result:";
		default:
			return "Good!";
		}
	}
	public static String getChatSuccessSpeak() {
		Random r = new Random();
		int time = r.nextInt(3);
		switch (time) {
		case 0:

			return "Ok, what do you want to talk about?";
		case 1:
			return "Em, here we go.";
		case 2:
			return "Okie.";
		default:
			return "Good!";
		}
	}
	public static String getChatFinishSpeak() {
		Random r = new Random();
		int time = r.nextInt(3);
		switch (time) {
		case 0:

			return "I'm glad to talk with you.";
		case 1:
			return "You are so nice.";
		case 2:
			return "Okie.";
		default:
			return "Good!";
		}
	}

}
