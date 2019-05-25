import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Anagram {
	//元の辞書
	static List<String> dictionary = new ArrayList<String>();
	//アルファベットを並び替えた辞書
	static List<String> sortDictionary = new ArrayList<String>();
	//問題の組み合わせをListで表現
	static List<String> questions = new ArrayList<>();

	public static void main(String[] args) {
		//引数にとった文字をアルファベット順にする
		args = insertSort(args);
		//べき集合を作る(アルファベット順に保管されている)
		power_set(args);
		descend();
		//System.out.println(questions.get(0));
		readFile();
		//System.out.println(dictionary.get(0));
		//System.out.println(dictionary.get(1));
		int index = -1;
		for (String word : sortDictionary) {
			for (String question : questions) {
				//System.out.println(question);
				if (word.equals(question)) {
					//一致したら抜ける
					index = sortDictionary.indexOf(word);
					break;
				}
			}
		}
		System.out.println((index >= 0) ? dictionary.get(index) : "No Word!");
	}

	//immutableなので新しい場所に作られるので参照を戻り値で返す必要がある
	static String fixCharacter(String word) {
		//小文字に直す
		word = word.toLowerCase();
		//QuはQに置き換える
		word = word.replace("qu", "q");
		return word;
	}

	//参照のコピーが渡るので注意が必要
	static String insertSort(String word) {
		word = fixCharacter(word);
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

	static String[] insertSort(String[] args) {
		//拡張for文は一度変数に要素のコピーをとって実行しているイメージなので初期化などはできないので注意
		for (int i = 0; i < args.length; i++) {
			args[i] = fixCharacter(args[i]);
		}
		for (int i = 1; i < args.length; i++) {
			int j = i;
			while (j >= 1 && args[j - 1].compareTo(args[j]) > 0) {
				//swapする
				swap(args, j - 1, j);
				j--;
			}
		}
		return args;
	}

	static void swap(char[] chars, int i, int j) {
		char tmp = chars[i];
		chars[i] = chars[j];
		chars[j] = tmp;
	}

	static void swap(String[] args, int i, int j) {
		String tmp = args[i];
		args[i] = args[j];
		args[j] = tmp;
	}

	static void swap(int i, int j) {
		String tmp = questions.get(i);
		questions.set(i, questions.get(j));
		questions.set(j, tmp);
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
	}

	//べき集合を作る
	//0 -> 0 01 -> 0 01 02 012 -> 0 01 02 012 03 013 023 0123
	static void power_set(String args[]) {
		//参照渡しではなくディープコピーしたい
		questions.add("");
		for (String alphabet : args) {
			List<String> cpQuestion = new ArrayList<String>(questions);
			for (String word : cpQuestion) {
				questions.add(word + alphabet);
			}
		}
	}

	//questionsを長さが長いものからざっくり並べたい
	static void descend() {
		int size = questions.size();
		int i = 0;
		while (i < size - (i + 1)) {
			swap(i, size - (i + 1));
			i++;
		}
	}
}