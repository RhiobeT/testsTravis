import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Tests {

	public static void main(String[] args) {
		String fileName = "_result.mediawiki";
		String fileHtmlName = "_result.html";

		try {
			System.out.println("Testing the file \"" + fileName + "\"");
		
			if (!test_upperCaseForTitles(new Scanner(new File(fileName))))
				System.exit(-2);
			else if (!test_images(new Scanner(new File(fileName))))
				System.exit(-3);
			else if (!test_links(new Scanner(new File(fileName))))
				System.exit(-4);
			else if (!test_css(new Scanner(new File(fileHtmlName))))
				System.exit(-5);
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't test the file named \"" + fileName + "\"");
			e.printStackTrace();
			System.exit(-1);
		}

	}

	private static boolean test_upperCaseForTitles(Scanner lecteur) {
		String toTest = "";
		
		while (lecteur.hasNextLine()) {
			toTest = lecteur.nextLine();
			
			if (toTest.length() > 0) {
				if (toTest.charAt(0) == '=') {
					boolean firstIsMaj = true;
					int i = 0;
					
					while (i < toTest.length()) {
						if (Character.isLetter(toTest.charAt(i))) {
							firstIsMaj = Character
									.isUpperCase(toTest.charAt(i));
							break;
						}
						i++;
					}
					
					if (!firstIsMaj) {
						System.err.println(toTest + " should start with an upper case letter");
						return false;
					}
				}
			}
		}
		
		System.out.println("Upper case for titles: OK");
		return true;
	}

	private static boolean test_links(Scanner lecteur) {
		String toTest = "", link;

		while (lecteur.hasNext()) {
			toTest = lecteur.nextLine();

			for (int i = 0; i < toTest.length(); i++) {
				link = null;

				try {
					if (toTest.substring(i, i+5).equals("[http"))
						link = toTest.substring(i+1);
				}
				catch (IndexOutOfBoundsException ignored) {}
			
				if (link != null) {
					for (int j = 0; j < link.length(); j++) {
						if (link.charAt(j) == ' ') {
							link = link.substring(0, j);
							break;
						}
					}
					
					try {
						URL url = new URL(link);
						URLConnection connection = url.openConnection();
						connection.connect();
						
						if (connection.getHeaderFields().get(null).get(0).substring(9, 12).equals("404")) {
							System.err.println(link + " not found");
							return false;
						}
						
						((HttpURLConnection) connection).disconnect();
					} catch (UnknownHostException e) {
						System.err.println("Couldn't connect to " + link);
						return false;
					} catch (IOException e) {
						System.err.println(link + " doesn't seem to be a valid link");
						return false;
					}
				}
			}
		}
		
		System.out.println("Links: OK");
		return true;
	}

	private static boolean test_images(Scanner lecteur) {
		String toTest = "", image;

		while (lecteur.hasNext()) {
			toTest = lecteur.nextLine();

			for (int i = 0; i < toTest.length(); i++) {
				image = null;

				try {
					if (toTest.substring(i, i+7).equals("[Image:"))
						image = toTest.substring(i + 7);
				}
				catch (IndexOutOfBoundsException ignored) {}
			
				if (image != null) {
					for (int j = 0; j < image.length(); j++) {
						if (image.charAt(j) == '|') {
							image = image.substring(0, j);
						}
					}
					try {
						Image img = ImageIO.read(new File(image));
					} catch (IOException e) {
						System.err.println("Couldn't find " + image);
						return false;
					}
				}
			}
		}
		
		System.out.println("Images: OK");
		return true;
	}
	
	private static boolean test_css(Scanner lecteur) {
		String toTest = "", fichier;

		while (lecteur.hasNext()) {
			toTest = lecteur.nextLine();

			if (toTest.substring(0, 29).equals("<link rel=\"stylesheet\" href=\"")) {
				try {
					fichier = toTest.substring(29, toTest.length() - 2);
				}
				catch (IndexOutOfBoundsException ignored) {}
			}
			
			if (fichier != null) {
				File file = new File(fichier);
				if (file.exists() && file.isFile()){
					System.out.println("Css file: OK");
					return true;
				}
				else {
					System.err.println("Couldn't find " + fichier);
					return false;
				}
			}
		}
		
		System.out.println("Css file: OK");
		return true;
	}
}
