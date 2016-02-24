package me.hope.asd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import me.hope.asd.data.GamePlayer;

public class FileUtil {
	private static final String FILEPATH = "F:\\code\\workspace\\CharacterEnergySystem\\resource\\";
	private static final String TESTFILENAME = "a55f50ad-1007-419f-a920-4fad5727b7bf";

	public FileUtil() {
	}

	public static <T extends Object> void writeObject(T obj, File filepath)
			throws FileNotFoundException, IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filepath));
		objectOutputStream.writeObject(obj);
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	public static <T extends Object> void writeObject(T obj, String filepath)
			throws FileNotFoundException, IOException {
		writeObject(obj, createFile(filepath));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T readObject(File filepath)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filepath));
		Object object = objectInputStream.readObject();
		T result = null;
		if (object != null) {
			result = (T) object;
		}
		objectInputStream.close();
		return result;
	}
	public static <T extends Object> T readObject(String filepath)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		return readObject(createFile(filepath));
	}

	public static File createFile(String filepath) throws IOException {
		File file = new File(filepath);
		if (!(file.exists())) {
			file.createNewFile();
		}
		return file;
	}

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {

		try {
			GamePlayer gamePlayer1 = new GamePlayer(TESTFILENAME, 0, 150, 20);
			createFile(FILEPATH +TESTFILENAME);
			writeObject(gamePlayer1, FILEPATH +TESTFILENAME);
			GamePlayer gamePlayer = readObject(FILEPATH +TESTFILENAME);

			System.out.println(gamePlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
