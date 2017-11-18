package com.stapp.database;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.stapp.security.PasswordHelpers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by jr on 18/11/17.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseDriverTest {
    private DatabaseDriver db;
    @Before
    public void setUp() throws Exception {
        this.db = new DatabaseDriver(InstrumentationRegistry.getTargetContext());
        InitializeDatabase.initializeDatabase();
    }

    @After
    public void tearDown() throws Exception {
        InitializeDatabase.initializeDatabase();
        DatabaseDriverHelper.reinitializeDatabase();
        InitializeDatabase.initializeDatabase();
        this.db.close();
    }

    /**
     * Random string generator.
     * obtained from: https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
     */
    private String generateRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    // USER TESTS
    // INSERT
    @Test
    public void testInsertRole() {
        String randomRole = generateRandomString();
        db.insertRole(randomRole);
        assertTrue(db.roleExists(randomRole));
    }

    @Test
    public void testInsertUser() {
        // Insert role beforehand
        String randomRole = generateRandomString();
        int roleId = (int) db.insertRole(randomRole);

        String randomUsername = generateRandomString();
        String randomName = generateRandomString();
        String randomPass = generateRandomString();
        db.insertUser(randomUsername, randomName, randomPass, roleId);
        boolean correctUsername = randomName.equals(db.getName(randomUsername));
        boolean correctPassword = PasswordHelpers.comparePassword(db.getPassword(randomUsername), randomPass);
        boolean correctRoleId = roleId == db.getRoleIdGivenUsername(randomUsername);
        assertTrue(correctPassword && correctRoleId && correctUsername);
    }

    // UPDATE

    @Test
    public void testUpdatePassword() {
        String username = generateRandomString();
        String name = generateRandomString();
        String password = generateRandomString();
        String newPassword = generateRandomString();

        db.insertUser(username, name, password, 1);
        db.updatePassword(newPassword, username);
        assertEquals(PasswordHelpers.passwordHash(newPassword), db.getPassword(username));
    }

    @Test
    public void testUpdateName() {
        String username = generateRandomString();
        String name = generateRandomString();
        String password = generateRandomString();
        String newName = generateRandomString();

        db.insertUser(username, name, password, 1);
        db.updateName(newName, username);
        assertEquals(newName, db.getName(username));
    }

    @Test
    public void testUpdateRole() {
        String username = generateRandomString();
        String name = generateRandomString();
        String password = generateRandomString();
        String role = generateRandomString();
        String newRole = generateRandomString();

        int roleId = (int) db.insertRole(role);
        int newRoleId = (int) db.insertRole(newRole);


        db.insertUser(username, name, password, roleId);
        db.updateRole(newRoleId, username);

        assertEquals(newRoleId, db.getRoleIdGivenUsername(username));
    }

    // SELECT

    @Test
    public void testGetRoleNameGivenUsername() {
        String username = generateRandomString();
        String name = generateRandomString();
        String password = generateRandomString();
        String roleName = generateRandomString();

        int roleId = (int) db.insertRole(roleName);

        db.insertUser(username, name, password, roleId);

        assertEquals(roleName, db.getRoleNameGivenUsername(username));
    }

    @Test
    public void testGetRoleIdGivenRoleName() {
        String roleName = generateRandomString();

        int roleId = (int) db.insertRole(roleName);

        assertEquals(roleId, db.getRoleIdGivenRoleName(roleName));
    }

    @Test
    public void testGetRoleIdGivenUsername() {
        String username = generateRandomString();
        String name = generateRandomString();
        String password = generateRandomString();
        String roleName = generateRandomString();

        int roleId = (int) db.insertRole(roleName);

        db.insertUser(username, name, password, roleId);

        assertEquals(roleId, db.getRoleIdGivenUsername(username));
    }

    @Test
    public void testGetId() {
        String username = generateRandomString();
        String name = generateRandomString();
        String password = generateRandomString();
        String roleName = generateRandomString();

        int id = (int) db.insertUser(username, name, password, (int) db.insertRole(roleName));
        assertEquals(id, db.getId(username));
    }

    @Test
    public void testGetName() {
        String username = generateRandomString();
        String name = generateRandomString();
        String password = generateRandomString();
        String roleName = generateRandomString();

        db.insertUser(username, name, password, (int) db.insertRole(roleName));
        assertEquals(name, db.getName(username));
    }

    @Test
    public void testGetPassword() {
        String username = generateRandomString();
        String name = generateRandomString();
        String password = generateRandomString();
        String roleName = generateRandomString();

        db.insertUser(username, name, password, (int) db.insertRole(roleName));
        assertEquals(PasswordHelpers.passwordHash(password), db.getPassword(username));
    }

    @Test
    public void testUserExists() {
        String username = generateRandomString();
        String name = generateRandomString();
        String password = generateRandomString();
        String roleName = generateRandomString();

        db.insertUser(username, name, password, (int) db.insertRole(roleName));
        assertTrue(db.userExists(username));
    }

    @Test
    public void testRoleExistsRoleId() {
        String roleName = generateRandomString();
        int roleId = (int) db.insertRole(roleName);
        assertTrue(db.roleExists(roleId));
    }

    @Test
    public void testRoleExistsRoleName() {
        String roleName = generateRandomString();
        db.insertRole(roleName);
        assertTrue(db.roleExists(roleName));
    }


    // COURSE TESTS
    // INSERT
    @Test
    public void testInsertCourse() {
        // Insert a professor first
        String course = generateRandomString();
        String prof = generateRandomString();
        db.insertCourse(course, prof);
        List<String> courses = db.getProfCourses(prof);
        assertTrue(courses.contains(course));
    }

    public void testInsertStudentToCourse() {
        String course = generateRandomString();
        String student = generateRandomString();
        String prof = generateRandomString();
        db.insertCourse(course, prof);

        int id = (int) db.insertStudentToCourse(course, student);
        List<String> courses = db.getStudentCourseNames(student);
        assertTrue(courses.contains(course));
    }

    // UPDATE
    @Test
    public void testArchiveCourse() {
        // Insert a course
        String course = generateRandomString();
        String prof = generateRandomString();
        db.insertCourse(course, prof);

        boolean success = db.archiveCourse(course);
        assertTrue(success && !db.courseNotArchived(course));
    }

    @Test
    public void testRemoveStudentFromCourse() {
        String course = generateRandomString();
        String student = generateRandomString();
        String prof = generateRandomString();
        db.insertCourse(course, prof);

        // Insert student into course
        db.insertStudentToCourse(course, student);
        db.removeStudentFromCourse(course, student);
        assertFalse(db.getStudentCourseNames(student).contains(course));

    }
    // SELECT

    // ASSIGNMENT TESTS
    // INSERT
    // UPDATE
    // SELECT

    // QUESTION TESTS
    // INSERT
    // SELECT

    // ASSIGNMENT SUBMISSION TESTS
    // INSERT
    // UPDATE
    // SELECT

    /*
    @Test
    public void testInsertUser() throws Exception {
        db.insertUser("user", "name", "password", 1);
        assertEquals("name", db.getName("user"));
    }
    */

}