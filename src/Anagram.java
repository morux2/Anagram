import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Anagram {
	//元の辞書
	static List<String> dictionary = new ArrayList<String>();
	//アルファベットを並び替えた辞書
	static List<String> sortDictionary = new ArrayList<String>();

	public static void main(String[] args) {
		String question = "";
		for (String word : args)
			question += word;
		System.out.println(question);
		question = insertSort(question);
		System.out.println(question);
		readFile();
		//System.out.println(dictionary.get(0));
		//System.out.println(dictionary.get(1));
		int index = -1;
		for (String word : sortDictionary) {
			if (word.equals(question)) {
				//一致したら抜ける
				index = sortDictionary.indexOf(word);
				break;
			}
		}
		System.out.println((index >= 0) ? dictionary.get(index) : "No Word!");
	}

	//参照のコピーが渡るので注意が必要
	static String insertSort(String word) {
		//小文字に直す
		word = word.toLowerCase();
		//QuはQに置き換える
		word = word.replace("qu", "q");
		//char配列に変換
		char[] chars = word.toCharArray();
		for (int i = 1; i < chars.length; i++) {
			int j = i;
			while (j >= 1 && chars[j - 1] > chars[j]) {
				//swapする
				swap(chars, j - 1, j);
				j--;
			}
		}
		return new String(chars);
	}

	static void swap(char[] chars, int i, int j) {
		char tmp = chars[i];
		chars[i] = chars[j];
		chars[j] = tmp;
	}

	//Hashを使うと二分探索がやりにくいのでリストにした(Hashの順番でリストができてしまった)
	static void readFile() {
		try {
			//dictionaryファイルを指定
			File file = new File("src/dictionary.txt");
			//FileReaderは1文字ずつ, BufferReaderは1行ずつ
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String word;
			//nullまで読み込む
			while ((word = bufferedReader.readLine()) != null) {
				dictionary.add(word);
				sortDictionary.add(insertSort(word));
			}
			//リソースの開放
			bufferedReader.close();
			//ファイルが見つからなければエラー
		} catch (FileNotFoundException e) {
			System.out.println(e);
			//readLineでエラーが出た時
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		//debug用にoutputStreamに書き込み
		try {
			File file = new File("src/sortDictionary.txt");
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(file));
			for (String word : sortDictionary) {
				bufferWriter.write(word);
				//改行コードをOS似合わせて自動で判断して出力
				bufferWriter.newLine();
			}
			bufferWriter.close();
			//ファイルが見つからなければエラー
		} catch (FileNotFoundException e) {
			System.out.println(e);
			//writeでエラーが出た時
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
}
