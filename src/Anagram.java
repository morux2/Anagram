public class Anagram {

	public static void main(String[] args) {
		String question = "";
		for (String word : args)
			question += word;
		System.out.println(question);
		question = insertSort(question);
		System.out.println(question);
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
}
