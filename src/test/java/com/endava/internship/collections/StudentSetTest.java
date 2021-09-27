package com.endava.internship.collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StudentSetTest {

    private StudentSet<Student> testSet;
    private static Student student1;
    private static Student student2;
    private static Student student3;
    private static StudentUTM studentUTM1;
    private static StudentUTM studentUTM2;
    private static StudentUTM studentUTM3;

    @BeforeAll
    static void initialize() {
        student1 = new Student("Vitali Butnar", LocalDate.parse("1992-10-02"), "JavaDev Intern");
        student2 = new Student("Irina Fiodorova", LocalDate.parse("1995-01-01"), "JavaDev Intern");
        student3 = new Student("Sergiu Ivanov", LocalDate.parse("1989-01-01"), "JavaDev Intern");
        studentUTM1 = new StudentUTM("Vitali Butnar", LocalDate.parse("1992-10-02"), "JavaDev Intern", "FRT");
        studentUTM2 = new StudentUTM("Sergiu Ivanov", LocalDate.parse("1989-01-01"), "JavaDev Intern", "CIM");
        studentUTM3 = new StudentUTM("Irina Fiodorova", LocalDate.parse("1995-01-01"), "JavaDev Intern", "FCG");

    }

    @BeforeEach
    void setUp() {
        testSet = new StudentSet<>();
    }

    @Test
    public void testAdd() {
        testSet.add(student1);
        testSet.add(student2);
        testSet.add(studentUTM1);
        testSet.add(studentUTM2);

        assertAll(
                () -> assertThat(testSet).isNotEmpty(),
                () -> assertThat(testSet).contains(student1, student2, studentUTM1, studentUTM2),
                () -> assertThat(testSet).containsOnly(student1, student2, studentUTM1, studentUTM2),
                () -> assertThat(testSet).hasSize(4),
                () -> assertThat(testSet.toString()).isNotEmpty(),
                () -> assertThat(testSet.iterator().hasNext()).isTrue(),
                () -> assertThat(testSet.iterator().next()).isIn(student1, student2, studentUTM1, studentUTM2),
                () -> assertThat(testSet.add(student1)).isFalse()
        );
    }

    @Test
    public void testAddAll() {
        testSet.add(studentUTM1);
        testSet.add(student1);
        StudentSet<StudentUTM> utmStudentsSet = new StudentSet<>();
        utmStudentsSet.add(studentUTM1);
        utmStudentsSet.add(studentUTM2);
        utmStudentsSet.add(studentUTM3);
        StudentSet<Student> studentsSet = new StudentSet<>();
        studentsSet.add(student2);
        studentsSet.add(student3);

        testSet.addAll(studentsSet);
        testSet.addAll(utmStudentsSet);

        assertAll(
                () -> assertThat(testSet).hasSize(6),
                () -> assertThat(testSet).contains(student1, student2, student3, studentUTM1, studentUTM2, studentUTM3),
                () -> assertThat(testSet).containsOnly(student1, student2, student3, studentUTM1, studentUTM2, studentUTM3),
                () -> assertThat(testSet.addAll(studentsSet)).isFalse()
        );
    }

    @Test
    public void testRemove() {
        testSet.add(studentUTM1);
        testSet.add(studentUTM2);
        testSet.add(studentUTM3);

        testSet.remove(studentUTM1);
        testSet.remove(studentUTM3);
        testSet.remove(studentUTM2);

        assertAll(
                () -> assertThat(testSet).isEmpty(),
                () -> assertThat(testSet.toArray()).isEmpty(),
                () -> assertThat(testSet).hasSize(0),
                () -> assertThat(testSet).doesNotContain(studentUTM1, studentUTM2),
                () -> assertThat(testSet.iterator().hasNext()).isFalse(),
                () -> assertThat(testSet.add(studentUTM1)).isTrue(),
                () -> assertThat(testSet.remove(studentUTM2)).isFalse(),
                () -> assertThat(testSet.remove(student1)).isFalse()
        );
    }

    @Test
    public void testContains() {
        testSet.add(studentUTM1);
        testSet.add(student1);

        assertAll(
                () -> assertThat(testSet.contains(student1)).isTrue(),
                () -> assertThat(testSet.contains(student3)).isFalse(),
                () -> assertThat(testSet.contains(studentUTM2)).isFalse()
        );
    }

    @Test
    public void testToArray() {
        testSet.add(studentUTM1);
        testSet.add(studentUTM2);
        testSet.add(studentUTM3);

        assertAll(
                () -> assertThat(testSet.toArray()).isNotEmpty(),
                () -> assertThat(testSet.toArray()).hasSize(3),
                () -> assertThat(testSet.toArray(new Student[0])).hasSameClassAs(new Student[0])
        );
    }

    @Test
    public void testClear() {
        testSet.add(student1);
        testSet.add(studentUTM1);

        testSet.clear();

        assertAll(
                () -> assertThat(testSet).doesNotContain(student1, studentUTM1),
                () -> assertThat(testSet).isEmpty(),
                () -> assertThat(testSet).hasSize(0),
                () -> assertThat(testSet).isNotNull(),
                () -> assertThat(testSet.add(student1)).isTrue()
        );
    }

    @Test
    public void testIterator() {
        testSet.add(student1);
        testSet.add(studentUTM1);

        Iterator<Student> iterator = testSet.iterator();

        assertAll(
                () -> assertThat(iterator.hasNext()).isTrue(),
                () -> assertThat(iterator.next()).isNotNull(),
                () -> assertThat(iterator.next()).isIn(studentUTM1, student1)
        );
    }

    @Test
    public void testIteratorRemove() {
        testSet.add(studentUTM1);
        testSet.add(studentUTM2);
        testSet.add(studentUTM3);
        testSet.add(student1);
        testSet.add(student2);
        testSet.add(student3);

        Iterator<Student> iterator = testSet.iterator();
        iterator.next();
        Student tempStudent1 = iterator.next();
        iterator.remove();
        Student tempStudent2 = null;
        while (iterator.hasNext())
            tempStudent2 = iterator.next();
        Student tempStudent3 = tempStudent2;
        iterator.remove();

        assertAll(
                () -> assertThat(testSet).hasSize(4),
                () -> assertThat(testSet).doesNotContain(tempStudent1),
                () -> assertThat(testSet).doesNotContain(tempStudent3)
        );
    }

    private static class StudentUTM extends Student {
        private String faculty;


        StudentUTM(String name, LocalDate dateOfBirth, String details, String faculty) {
            super(name, dateOfBirth, details);
            this.faculty = faculty;
        }

        @Override
        public int hashCode() {
            return 5;
        }
    }
}
