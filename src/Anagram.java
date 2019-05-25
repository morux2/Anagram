import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Anagram {
	//アルファベットを並び替えた辞書
	static List<MyString> sortDictionary = new ArrayList<MyString>();
	//問題の組み合わせをListで表現
	static List<String> questions = new ArrayList<>();

	public static void main(String[] args) {
		//辞書の用意(スコアでソートされている)
		readFile();
		//問題はscannerから入力する
		Scanner scanner = new Scanner(System.in);
		//Cntl+Dで終了
		while (scanner.hasNext()) {
			String input = scanner.nextLine();
			//単語をアルファベット順に並び替え
			input = insertSort(input);
			//べき集合を作る(アルファベット順に保管されている)
			power_set(input);
			//組み合わせをある程度の精度で並び替え
			descend();
			MyString answer = null;
			//ラベル付きbreak文で抜ける
			exit: {
				for (MyString myString : sortDictionary) {
					for (String question : questions) {
						if (myString.sortWord.equals(question)) {
							answer = myString;
							//一致したら抜ける
							break exit;
						}
					}
				}
			}
			System.out.println(answer.sortWord);
			System.out.println(answer.originalWord);
		}
		scanner.close();
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

	static void qSwap(int i, int j) {
		String tmp = questions.get(i);
		questions.set(i, questions.get(j));
		questions.set(j, tmp);
	}

	static void dSwap(int i, int j) {
		MyString tmp = sortDictionary.get(i);
		sortDictionary.set(i, sortDictionary.get(j));
		sortDictionary.set(j, tmp);
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
				sortDictionary.add(new MyString(word, insertSort(word)));
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
		//score順が高い順にソート
		scoreQuickSort(0, sortDictionary.size() - 1);
		/*
		//debug用にoutputStreamに書き込み
		try {
			File file = new File("src/sortDictionary.txt");
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(file));
			for (MyString myString : sortDictionary) {
				bufferWriter.write(myString.score + " " + myString.sortWord);
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

	//べき集合を作る
	//0 -> 0 01 -> 0 01 02 012 -> 0 01 02 012 03 013 023 0123
	static void power_set(String question) {
		//一度要素をクリア
		questions.clear();
		char[] chars = question.toCharArray();
		questions.add("");
		for (char alphabet : chars) {
			//参照渡しではなくディープコピーしたい
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
			qSwap(i, size - (i + 1));
			i++;
		}
	}

	static void scoreQuickSort(int left, int right) {
		int i, j;
		int pivot;
		if (left >= right)
			return;
		i = left;
		j = right;
		pivot = sortDictionary.get((left + right) / 2).score;
		do {
			while (pivot < sortDictionary.get(i).score)
				i++;
			while (pivot > sortDictionary.get(j).score)
				j--;
			if (i <= j) {
				dSwap(i, j);
				i++;
				j--;
			}
		} while (i <= j);
		scoreQuickSort(left, j);
		scoreQuickSort(i, right);
	}
}