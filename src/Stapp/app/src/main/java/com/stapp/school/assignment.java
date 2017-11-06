package com.stapp.school;

import com.stapp.databasehelpers.AssignmentHelper;

import java.util.List;

/**
 * Created by wenboma on 05/11/2017.
 */

public class Assignment {

  private int id;
  private boolean validAssignment = false;
  private String assignmentName;
  private String courseName;

  public Assignment(int id) {
    if (AssignmentHelper.assignmentExists(id)) {
      this.id = id;
      validAssignment = true;
    }
  }

  public Assignment(String assignmentName, String due, String courseName) {
    if (!AssignmentHelper.assignmentExists(assignmentName, courseName)) {
      AssignmentHelper.createAssignment(assignmentName, due, courseName);
      this.id = AssignmentHelper.getAssignmentId(assignmentName, courseName);
      this.assignmentName = assignmentName;
      this.courseName = courseName;
      validAssignment = true;
    }
  }

  public boolean isValidAssignment() {
    return validAssignment;
  }

  /**
   * Creates a new multiple choice question for the assignment.
   * @param choices size() <= 4
   * @param correctIndex index of the correct answer in the choices list
   * @return true if question is inserted
   */
  public boolean insertMultipleChoiceQuestion(String question, List<String> choices, int correctIndex) {
      long id = AssignmentHelper.insertMultipleChoiceQuestion(this.id, question, choices, correctIndex);
      return id != -1;
  }

  public String getDueDate() {
      return AssignmentHelper.getAssignmentDueDate(assignmentName, courseName);
  }

  public List<Question> getQuestions() {
      return AssignmentHelper.getQuestions(this.id);
  }
}
