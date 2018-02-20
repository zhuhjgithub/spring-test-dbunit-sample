package com.hainet.spring.test.dbunit.sample.domain.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.hainet.spring.test.dbunit.sample.domain.entity.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DatabaseSetup("/com/hainet/spring/test/dbunit/sample/domain/dao/PersonDaoTest/setup/person.xml")
public class PersonDaoTest {

    private final String ROOT = "/com/hainet/spring/test/dbunit/sample/domain/dao/PersonDaoTest";
    private final String SETUP = ROOT + "/setup";
    private final String EXPECTED = ROOT + "/expected";

    @Autowired
    private PersonDao dao;

    @Test
    public void findAllTest() {
        // Setup
        final Person[] expected = {
                new Person(1, "hainet"),
                new Person(2, "spring-test-dbunit")
        };

        // Exercise
        final List<Person> actual = dao.findAll();

        // Verify
        assertThat(actual, is(containsInAnyOrder(expected)));
    }

    @Test
    @DatabaseSetup(SETUP + "/personEmpty.xml")
    public void findAddTest_empty() {
        // Exercise
        final List<Person> actual = dao.findAll();

        // Verify
        assertThat(actual, is(emptyCollectionOf(Person.class)));
    }

    @Test
    @ExpectedDatabase(value = EXPECTED + "/insertTest.xml", table = "person")
    public void insertTest() {
        // Setup
        final Person person = new Person(3, "person");

        // Exercise
        dao.insert(person);
    }

    @Test
    @ExpectedDatabase(value = EXPECTED + "/updateTest.xml", table = "person")
    public void updateTest() {
        // Setup
        final Person person = new Person(1, "updated");

        // Exercise
        dao.update(person);
    }

    @Test
    @ExpectedDatabase(value = EXPECTED + "/deleteTest.xml", table = "person")
    public void deleteTest() {
        // Setup
        final Person person = new Person(1, "hainet");

        // Exercise
        dao.delete(person);
    }
}