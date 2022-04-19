package ssvv.lab1;

import static org.junit.Assert.assertTrue;

import domain.Student;
import domain.Tema;
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

        List<String> assignmentIds = new ArrayList<>();
        service.getAllTeme().forEach(a -> assignmentIds.add(a.getID()));
        assignmentIds.forEach(id -> service.deleteTema(id));
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


// ======================== Add assignment ==============================


    @Test
    public void addAssignment_valid_shouldSucceed(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 3, 3));

        Tema assignment = service.findTema("bezos");
        Assert.assertNotNull(assignment);
    }

    @Test(expected = ValidationException.class)
    public void addAssignment_idNull_shouldThrowError(){
        service.addTema(new Tema(null, "Amazon Web Services HW 2", 3, 3));
    }

    @Test(expected = ValidationException.class)
    public void addAssignment_idEmpty_shouldThrowError(){
        service.addTema(new Tema("", "Amazon Web Services HW 2", 3, 3));
    }

    @Test(expected = ValidationException.class)
    public void addAssignment_descriptionEmpty_shouldThrowError(){
        service.addTema(new Tema("bezos", "", 3, 3));
    }

    @Test
    public void addAssignment_deadlineLowerValidEdgeCase_shouldSucceed(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 1, 3));
    }

    @Test
    public void addAssignment_deadlineHigherValidEdgeCase_shouldSucceed(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 14, 14));
    }

    @Test(expected = ValidationException.class)
    public void addAssignment_deadlineLowerInvalidEdgeCase_shouldThrowError(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 0, 3));
    }

    @Test(expected = ValidationException.class)
    public void addAssignment_deadlineHigherInvalidEdgeCase_shouldThrowError(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 15, 3));
    }

    @Test
    public void addAssignment_primireLowerValidEdgeCase_shouldSucceed(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 1, 1));
    }

    @Test
    public void addAssignment_primireHigherValidEdgeCase_shouldSucceed(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 14, 14));
    }

    @Test(expected = ValidationException.class)
    public void addAssignment_primireLowerInvalidEdgeCase_shouldThrowError(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 1, 0));
    }

    @Test(expected = ValidationException.class)
    public void addAssignment_primireHigherInvalidEdgeCase_shouldThrowError(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 1, 15));
    }

    @Test
    public void addAssignment_duplicateId_shouldNotSave(){
        service.addTema(new Tema("bezos", "Amazon Web Services HW 2", 3, 3));
        service.addTema(new Tema("bezos", "Amazon Web Services HW 3", 3, 3));

        List<Tema> assignments = new ArrayList<>();
        service.getAllTeme().forEach(assignments::add);

        Assert.assertEquals(1, assignments.size());
    }
}
