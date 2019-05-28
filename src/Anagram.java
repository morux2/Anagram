import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Anagram {
	//アルファベットを並び替えた辞書
	static List<MyString> sortDictionary = new ArrayList<>();
	//問題の組み合わせをListで表現
	static List<MyString> questions = new ArrayList<>();

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
			//スコアで全部のタイプをソート
			questions.sort(Comparator.comparing(MyString::getScore).reversed());
			MyString answer = null;
			//ラベル付きbreak文で抜ける
			exit: {
				for (MyString question : questions) {
					//questionのscoreより1大きいところまでざっくり進む、こうしないと通り過ぎてしまうことがある
					int start = binarySearch(question.score + 1, 0, sortDictionary.size() - 1);
					if (start == -1)
						continue;
					for (int i = start; i < sortDictionary
							.size(); i++) {
						MyString dictionary = sortDictionary.get(i);
						if (question.score > dictionary.score)
							break;
						if (dictionary.sortWord.equals(question.sortWord)) {
							answer = dictionary;
							//一致したら抜ける
							break exit;
						}
					}
				}
			}
			if (answer == null) {
				System.out.println("not found");
			} else {
				System.out.println(answer.sortWord);
				System.out.println(answer.originalWord);
			}
		}
		scanner.close();
	}

	static int binarySearch(int target, int left, int right) {
		if (left >= right)
			return -1;
		int mid = (left + right) / 2;
		int midScore = sortDictionary.get(mid).score;
		//System.out.println(left + " " + right + " " + mid + " " + midScore);
		if (target == midScore)
			return mid;
		else if (target < midScore)
			return binarySearch(target, mid + 1, right);
		else
			return binarySearch(target, left, mid);
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

	static void swap(List<MyString> target, int i, int j) {
		MyString tmp = target.get(i);
		target.set(i, target.get(j));
		target.set(j, tmp);
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
		//辞書をscore順が高い順にソート
		sortDictionary.sort(Comparator.comparing(MyString::getScore).reversed());
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
		questions.add(new MyString(""));
		for (char alphabet : chars) {
			//参照渡しではなくディープコピーしたい
			List<MyString> cpQuestion = new ArrayList<MyString>(questions);
			for (MyString word : cpQuestion) {
				questions.add(new MyString(word.sortWord + alphabet));
			}
		}
	}
	/*
	static void scoreQuickSort(List<MyString> target, int left, int right) {
		int i, j;
		int pivot;
		if (left >= right)
			return;
		i = left;
		j = right;
		pivot = target.get((left + right) / 2).score;
		do {
			while (pivot < target.get(i).score)
				i++;
			while (pivot > target.get(j).score)
				j--;
			if (i <= j) {
				swap(target, i, j);
				i++;
				j--;
			}
		} while (i <= j);
		scoreQuickSort(target, left, j);
		scoreQuickSort(target, i, right);
	}
	*/
}