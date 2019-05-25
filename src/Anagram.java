import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Anagram {
	//並び替えた単語の辞書,元の辞書
	static HashMap<String, String> dictionary = new HashMap<String, String>();
	//並び替えた単語をアルファベット順にソートした配列
	static String[] sortArray;

	public static void main(String[] args) {
		String question = "";
		for (String word : args)
			question += word;
		System.out.println(question);
		question = insertSort(question);
		System.out.println(question);
		readFile();
		int index = binarySearch(question, 0, sortArray.length);
		System.out.println((index >= 0) ? dictionary.get(sortArray[index]) : "No Word!");
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

	static void insertSort() {
		for (int i = 1; i < sortArray.length; i++) {
			int j = i;
			while (j >= 1 && sortArray[j - 1].compareTo(sortArray[j]) > 0) {
				//swapする
				swap(j - 1, j);
				j--;
			}
		}
	}

	static void swap(char[] chars, int i, int j) {
		char tmp = chars[i];
		chars[i] = chars[j];
		chars[j] = tmp;
	}

	static void swap(int i, int j) {
		String tmp = sortArray[i];
		sortArray[i] = sortArray[j];
		sortArray[j] = tmp;
	}

	static void readFile() {
		try {
			//dictionaryファイルを指定
			File file = new File("src/dictionary.txt");
			//FileReaderは1文字ずつ, BufferReaderは1行ずつ
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String word;
			//nullまで読み込む
			while ((word = bufferedReader.readLine()) != null) {
				dictionary.put(insertSort(word), word);
			}

			//辞書の単語をアルファベット順に並び替えたものをさらにアルファベット順にソート
			sortArray = new String[dictionary.size()];
			dictionary.keySet().toArray(sortArray);
			insertSort();
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
			for (String word : sortArray) {
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
		static int binarySearch(String question, int left, int right) {
			if (left >= right)
				return -1;
			int mid = (left + right) / 2;
			int compare = question.compareTo(sortArray[mid]);
//			System.out.println(mid+"");
//			System.out.println(sortArray[mid]);
			if (compare == 0)
				return mid;
			else if (compare > 0)
				return binarySearch(question, mid + 1, right);
			else
				return binarySearch(question, left, mid);
		}
}
