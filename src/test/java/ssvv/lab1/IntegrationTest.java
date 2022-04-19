package ssvv.lab1;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IntegrationTest {
    StudentValidator studentValidator = new StudentValidator();
    TemaValidator temaValidator = new TemaValidator();
    String filenameStudent = "fisiere/StudentiTest.xml";
    String filenameTema = "fisiere/TemeTest.xml";
    String filenameNota = "fisiere/NoteTest.xml";

    StudentXMLRepo studentXMLRepository;
    TemaXMLRepo temaXMLRepository;
    NotaValidator notaValidator;
    NotaXMLRepo notaXMLRepository;
    Service service;

    @Before
    public void SetUp(){
        studentXMLRepository = new StudentXMLRepo(filenameStudent);
        temaXMLRepository = new TemaXMLRepo(filenameTema);
        notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @After
    public void TearDown(){
        List<String> gradeIds = new ArrayList<>();
        service.getAllNote().forEach(a -> gradeIds.add(a.getID()));
        gradeIds.forEach(id -> service.deleteNota(id));

        List<String> studentIds = new ArrayList<>();
        service.getAllStudenti().forEach(s -> studentIds.add(s.getID()));
        studentIds.forEach(id -> service.deleteStudent(id));

        List<String> assignmentIds = new ArrayList<>();
        service.getAllTeme().forEach(a -> assignmentIds.add(a.getID()));
        assignmentIds.forEach(id -> service.deleteTema(id));
    }

    @Test
    public void addStudent_valid_shouldSucceed(){
        service.addStudent(new Student("s1", "Bob Bobbington", 911, "bob@bobmail.com"));

        Student student = service.findStudent("s1");
        Assert.assertNotNull(student);
    }

    @Test
    public void addAssignment_valid_shouldSucceed(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 7, 3));

        Tema assignment = service.findTema("bezos");
        Assert.assertNotNull(assignment);
    }

    @Test
    public void addGrade_valid_shouldSucceed(){
        service.addStudent(new Student("s1", "Bob Bobbington", 911, "bob@bobmail.com"));
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 7, 3));
        service.addNota(new Nota("b1", "s1", "bezos", 9, LocalDate.of(2022, 4, 18)), "Birds fly");

        Nota assignment = service.findNota("b1");
        Assert.assertNotNull(assignment);
    }

    @Test
    public void integrationTest_valid_shouldSucceed(){
        service.addStudent(new Student("s1", "Bob Bobbington", 911, "bob@bobmail.com"));
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 7, 3));
        service.addNota(new Nota("b1", "s1", "bezos", 9, LocalDate.of(2022, 4, 18)), "Birds fly");

        Nota assignment = service.findNota("b1");
        Assert.assertNotNull(assignment);
    }
}
