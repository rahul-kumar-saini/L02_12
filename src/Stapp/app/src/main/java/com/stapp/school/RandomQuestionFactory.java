package com.stapp.school;

import com.stapp.other.MultipleChoice;
import com.stapp.other.RandomMeanQuestion;
import com.stapp.other.RandomStdQuestion;
import com.stapp.other.RandomVarianceQuestion;

import java.util.ArrayList;
import java.util.HashMap;

/** Created by rahulkumar1998 on 2017-11-25. */
public class RandomQuestionFactory {

  private static HashMap<Object, Object> newQuestion(
      String questionString, ArrayList<String> choices, int index) {

    HashMap<Object, Object> question = new HashMap<>();

    question.put("QUESTION", questionString);
    question.put("CHOICES", choices);
    question.put("ANSWER", index);

    return question;
  }

  public static HashMap<Object, Object> createNewMeanQuestion() {
    MultipleChoice means = new RandomMeanQuestion(10);
    String question = means.questiondescription();
    ArrayList<String> choices = new ArrayList<>();
    choices.add(means.choices()[0]);
    choices.add(means.choices()[1]);
    choices.add(means.choices()[2]);
    choices.add(means.choices()[3]);
    int index = means.correctanswerindex();
    return newQuestion(question, choices, index);
  }

  public static HashMap<Object, Object> createNewVarianceQuestion() {
    MultipleChoice vari = new RandomVarianceQuestion(10);
    String question = vari.questiondescription();
    ArrayList<String> choices = new ArrayList<>();
    choices.add(vari.choices()[0]);
    choices.add(vari.choices()[1]);
    choices.add(vari.choices()[2]);
    choices.add(vari.choices()[3]);
    int index = vari.correctanswerindex();
    return newQuestion(question, choices, index);
  }

  public static HashMap<Object, Object> createNewStandardDeviationQuestion() {

    MultipleChoice stds = new RandomStdQuestion(10);
    String question = stds.questiondescription();
    ArrayList<String> choices = new ArrayList<>();
    choices.add(stds.choices()[0]);
    choices.add(stds.choices()[1]);
    choices.add(stds.choices()[2]);
    choices.add(stds.choices()[3]);
    int index = stds.correctanswerindex();
    return newQuestion(question, choices, index);
  }
}
