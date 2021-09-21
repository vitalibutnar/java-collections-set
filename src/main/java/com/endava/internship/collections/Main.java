package com.endava.internship.collections;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class Main {

    public static void main(String[] args) {

        StudentSet<Student> students = new StudentSet<>();
        log.info(String.format("a new object was added: %b",
                students.add(new Student("Irina Fiodorova", LocalDate.parse("1996-01-01"), "JavaDev Intern"))
        ));
        log.info(String.format("a new object was added: %b",
                students.add(new Student("Sergiu Ivanov", LocalDate.parse("1989-01-01"), "JavaDev Intern"))
        ));
        log.info(String.format("a new object was added: %b",
                students.add(new Student("Vitali Butnar", LocalDate.parse("1992-01-01"), "JavaDev Intern"))
        ));
        log.info(String.format("Expected size is 3, actual is %d", students.size()));
        log.info(String.format("the same object was added: %b",
                students.add(new Student("Vitali Butnar", LocalDate.parse("1992-01-01"), "JavaDev Intern"))
        ));
        log.info(String.format("Expected size is 3, actual is %d", students.size()));
        log.info(String.format("an existing object was removed: %b",
                students.remove(new Student("Sergiu Ivanov", LocalDate.parse("1989-01-01"), "JavaDev Intern"))
        ));
        log.info(String.format("Expected size is 2, actual is %d", students.size()));
        log.info(String.format("a new object was removed: %b",
                students.remove(new Student("Sergiu", LocalDate.parse("1989-01-01"), "JavaDev Intern"))
        ));
        log.info(String.format("Expected size is 2, actual is %d", students.size()));
        students.clear();
        log.info(String.format("Expected size after clear is 0, actual is %d", students.size()));
        log.info(String.format("a new object was added: %b",
                students.add(new Student("Vitali Butnar", LocalDate.parse("1992-01-01"), "JavaDev Intern"))
        ));
        log.info(String.format("Expected size is 1, actual is %d", students.size()));

        StudentSet<Student> someOthersStudents = new StudentSet<>();
        someOthersStudents.add(new Student("Vitali Butnar", LocalDate.parse("1992-01-01"), "JavaDev Intern"));
        someOthersStudents.add(new Student("Irina Fiodorova", LocalDate.parse("1996-01-01"), "JavaDev Intern"));

        log.info(String.format("an other collections with only a new object was added: %b",
                students.addAll(someOthersStudents)
        ));
        log.info(String.format("Expected size is 2, actual is %d", students.size()));
        log.info(String.format("check, if the object contains in collection: expected true, actual %b",
                students.contains(new Student("Irina Fiodorova", LocalDate.parse("1996-01-01"), "JavaDev Intern"))
        ));
        log.info(String.format("check, if the object contains in collection: expected false, actual %b",
                students.contains(new Student("Irina not Fiodorova", LocalDate.parse("1996-01-01"), "JavaDev Intern"))
        ));
        students.clear();
        for (int i = 0; i < 30; i++) {
           students.add(new Student(String.valueOf(i),LocalDate.MAX, "test"));
        }
        log.info(String.format("Expected size is 30, actual is %d", students.size()));
    }
}