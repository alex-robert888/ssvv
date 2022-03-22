package ssvv.lab1;

import static org.junit.Assert.assertTrue;

import domain.Student;
import junit.framework.TestCase;
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
import validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {

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
        List<String> studentIds = new ArrayList<>();
        service.getAllStudenti().forEach(s -> studentIds.add(s.getID()));
        studentIds.forEach(id -> service.deleteStudent(id));
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        Assert.assertTrue(true);
    }

    @Test
    public void addStudent_valid_shouldSucceed(){
        service.addStudent(new Student("s1", "Bob Bobbington", 911, "bob@bobmail.com"));

        Student student = service.findStudent("s1");
        Assert.assertNotNull(student);
    }

    @Test(expected = ValidationException.class)
    public void addStudent_invalidId_shouldThrowError(){
        service.addStudent(new Student("", "Bob", 123, "asd@asd.com"));
    }

    @Test(expected = ValidationException.class)
    public void addStudent_nullId_shouldThrowError(){
        service.addStudent(new Student(null, "Bob", 123, "asd@asd.com"));
    }

    @Test(expected = ValidationException.class)
    public void addStudent_invalidGroup_shouldThrowError(){
        service.addStudent(new Student("asd", "Bob", -89, "asd@asd.com"));
    }

    @Test(expected = ValidationException.class)
    public void addStudent_nullEmail_shouldThrowError(){
        service.addStudent(new Student("asd", "Bob", 123, null));
    }

    @Test(expected = ValidationException.class)
    public void addStudent_nullName_shouldThrowError(){
        service.addStudent(new Student("asd", null, 123, "bob@bobmail.com"));
    }

    @Test(expected = ValidationException.class)
    public void addStudent_invalidEmail_shouldThrowError(){
        service.addStudent(new Student("asd", "Bob", 123, ""));
    }
}
