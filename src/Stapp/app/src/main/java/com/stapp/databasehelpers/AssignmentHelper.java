package com.stapp.databasehelpers;

import com.stapp.database.ContextHelper;
import com.stapp.database.DatabaseDriver;
import com.stapp.school.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rahulkumar1998 on 2017-11-05.
 */

public class AssignmentHelper {

  private static DatabaseDriver databaseDriver;

  private static void openDatabase() {
    if (databaseDriver == null) {
      databaseDriver = new DatabaseDriver(ContextHelper.getStappContext());
    }
  }

  private static void closeDatabase() {
    databaseDriver.close();
    databaseDriver = null;
  }

  public static int getAssignmentId(String assignmentName, String className) {
    openDatabase();
    int Id = databaseDriver.getAssignmentId(assignmentName, className);
    closeDatabase();
    return Id;
  }

  public static String getAssignmentDueDate(String assignmentName, String className) {
    openDatabase();
    String dueDate = databaseDriver.getAssignmentDueDate(assignmentName, className);
    closeDatabase();
    return dueDate;
  }


  public static boolean assignmentExists(String assignmentName, String courseName) {
    openDatabase();
    boolean exists = databaseDriver.assignmentExists(assignmentName, courseName);
    closeDatabase();
    return exists;
  }

  public static boolean assignmentExists(int Id) {
    openDatabase();
    boolean exists = databaseDriver.assignmentExists(Id);
    closeDatabase();
    return exists;
  }

  public static int createAssignment(String assignmentName, String due, String courseName) {
    openDatabase();
    databaseDriver.insertAssignment(assignmentName, due, courseName);
    int Id = databaseDriver.getAssignmentId(assignmentName, courseName);
    closeDatabase();
    return Id;
  }

  public static List<Question> getQuestions(int assignmentId) {
      openDatabase();
      List<Question> questions = databaseDriver.getQuestions(assignmentId);
      closeDatabase();
      return questions;
  }

  /**
   * Insert a multiple choice question into the database
   * @param assignmentId
   * @param question the question to put
   * @param choices length <= 4, 4 choices only
   * @param correctIndex the index of the choice in choices that is the correct answer, < 4
   * @return id of the question
   */
  public static long insertMultipleChoiceQuestion(int assignmentId, String question, List<String> choices,
                                           int correctIndex) {
      openDatabase();
      if (choices.size() > 4) return -1;
      long id = databaseDriver.insertMultipleChoiceQuestion(assignmentId, question, choices.get(0),
              choices.get(1), choices.get(2), choices.get(3), choices.get(correctIndex));
      closeDatabase();
      return id;
  }
}