package Server;

/***
 * This class is in charge of managing the questions.
 * It reads the questions and answers
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Question {

    private String questionText;
    private ArrayList<String> options;
    private String answer;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public static ArrayList<Question> readQuestions(String filename) {
        ArrayList<Question> questions = new ArrayList<Question>();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = "";
            int counter = 0;
            Question q = null;
            while ((line = reader.readLine()) != null) {
                if (counter % 5 == 0) {
                    q = new Question();
                    q.setQuestionText(line);
                } else {
                    if (line.charAt(0) == '*') {
                        q.getOptions().add(line.substring(1));
                        q.setAnswer(line.substring(1));
                    } else {
                        q.getOptions().add(line);
                    }
                }
                if (counter % 5 == 4) {
                    questions.add(q);
                }
                counter++;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public Question() {
        options = new ArrayList<String>();
    }

}
