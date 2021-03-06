/*
	Project E-ICGD
	Contributors:
		Pierre Jeanjean
		Quentin Lacoste
		Florian Ouddane
		Anselme Revuz
*/

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.IllegalAccessException;
import java.lang.System;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.imageio.ImageIO;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Test {}

public class Tests {
	
	private static File mediawiki = new File("_result.mediawiki");
	private static File html = new File("_result.html");

	public static void main(String[] args) {
		if (args.length >= 2) {
			mediawiki = new File(args[0]);
			html = new File(args[1]);
		}
		
		System.out.println("Testing the files \"" + html.getName() + "\" and \"" + mediawiki.getName() + "\"");
		for (Method method : Tests.class.getMethods()) {
			if (method.getAnnotation(Test.class) != null) {
				try {
					if (!((Boolean) method.invoke(null)))
						System.exit(-20);
				} catch (InvocationTargetException e) {
					System.err.println("Couldn't test one of the files");
					e.printStackTrace();
					System.exit(-10);
				} catch (IllegalAccessException e) {
					System.err.println("An error occurred while launching " + method.getName());
					e.printStackTrace();
					System.exit(-11);
				}
			}
		}
	}

	@Test // Checks that each title starts with an uppercase letter
	public static boolean test_upperCaseForTitles() throws FileNotFoundException {
		Scanner lecteur = new Scanner(mediawiki);
		
		String toTest;
		
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

	@Test // Checks that each link is still working
	public static boolean test_links() throws FileNotFoundException {
		Scanner lecteur = new Scanner(mediawiki);
		String toTest, link;

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

	@Test // checks that each image is accessible
	public static boolean test_images() throws FileNotFoundException {
		Scanner lecteur = new Scanner(mediawiki);
		String toTest, image;

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
						ImageIO.read(new File(image));
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
	
	@Test // checks that the css stylesheet is accessible
	public static boolean test_css() throws FileNotFoundException {
		Scanner lecteur = new Scanner(html);
		String toTest, fichier = null;

		while (lecteur.hasNext()) {
			toTest = lecteur.nextLine();

			try {
				if (toTest.substring(0, 29).equals("<link rel=\"stylesheet\" href=\""))
					fichier = toTest.substring(29, toTest.length() - 2);
			}
			catch (IndexOutOfBoundsException ignored) {}
			
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
