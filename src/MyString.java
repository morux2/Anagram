
public class MyString {
	int score;
	//アルファベット順に並び替えた
	String sortWord;
	//オリジナルの単語
	String originalWord;

	public MyString(String originalWord, String sortWord) {
		this.originalWord = originalWord;
		score = calculate(sortWord);
		this.sortWord = sortWord;
	}

	public MyString(String sortWord) {
		score = calculate(sortWord);
		this.sortWord = sortWord;
	}

	static int calculate(String target) {
		int score = 0;
		while (target.length() > 0) {
			switch (target.charAt(0)) {
			case 'j':
			case 'k':
			case 'q':
			case 'x':
			case 'z':
				score += 3;
				break;
			case 'c':
			case 'f':
			case 'h':
			case 'l':
			case 'm':
			case 'p':
			case 'v':
			case 'w':
			case 'y':
				score += 2;
				break;
			default:
				score += 1;
				break;
			}
			target = target.substring(1);
		}
		return score;
	}
}
