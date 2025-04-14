package com.gracjan_d.taskmanager;

import com.gracjan_d.taskmanager.entity.*;
import com.gracjan_d.taskmanager.exception.TaskNotFoundException;
import com.gracjan_d.taskmanager.repository.TaskJpaRepository;
import com.gracjan_d.taskmanager.service.TaskService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TaskServiceIntegrationTests {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskJpaRepository taskJpaRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup(){
        jdbcTemplate.execute("INSERT INTO tasks(title, description, priority, difficulty, type, status, prize, deadline) VALUES " +
                "('KONWERTER LICZB', 'Zadanie polegające na konwersji liczb w różnych systemach liczbowych.', 'MEDIUM', 'EASY', 'GENERAL', 'ACTIVE', 50, '2025-05-05'),\n" +
                "('ANALIZA DANYCH', 'Zadanie związane z analizą danych zebranych z różnych źródeł.', 'HIGH', 'HARD', 'GENERAL', 'ACTIVE', 100, '2025-06-10'),\n" +
                "('APLIKACJA MOBILNA', 'Projekt aplikacji mobilnej do zarządzania zadaniami.', 'LOW', 'MEDIUM', 'GENERAL', 'ACTIVE', 150, '2025-07-01'),\n" +
                "('STWORZENIE STRONY WWW', 'Zadanie polegające na stworzeniu prostego szablonu strony internetowej.', 'MEDIUM', 'EASY', 'GENERAL', 'ACTIVE', 80, '2025-08-15'),\n" +
                "('BŁĘDY W KODZIE', 'Wyszukiwanie i naprawianie błędów w istniejącym projekcie.', 'HIGH', 'HARD', 'GENERAL', 'ACTIVE', 120, '2025-05-20'),\n" +
                "('TESTY OPROGRAMOWANIA', 'Zadanie polegające na przeprowadzaniu testów jednostkowych aplikacji.', 'LOW', 'EASY', 'GENERAL', 'ACTIVE', 60, '2025-09-01'),\n" +
                "('AUTOMATYZACJA PROCESÓW', 'Zadanie związane z automatyzacją codziennych procesów w firmie.', 'MEDIUM', 'HARD', 'GENERAL', 'ACTIVE', 200, '2025-10-10'),\n" +
                "('ZARZĄDZANIE BAZĄ DANYCH', 'Optymalizacja zapytań w bazie danych.', 'HIGH', 'MEDIUM', 'GENERAL', 'ACTIVE', 90, '2025-07-30'),\n" +
                "('PRZYGOOTOWANIE RAPORTU', 'Zadanie polegające na przygotowaniu szczegółowego raportu z wyników analizy.', 'LOW', 'MEDIUM', 'GENERAL', 'ACTIVE', 45, '2025-08-05'),\n" +
                "('PROJEKT EDUKACYJNY', 'Tworzenie materiałów edukacyjnych dla studentów.', 'MEDIUM', 'EASY', 'GENERAL', 'ACTIVE', 55, '2025-09-20');");
    }

    @AfterEach
    public void cleanUp(){
        jdbcTemplate.execute("DELETE FROM tasks");
        jdbcTemplate.execute("ALTER TABLE tasks " +
                "ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    public void findAll(){
        List<Task> tasks = taskService.getAllTasks();
        tasks.forEach(System.out::println);

        assertEquals(10, tasks.size());
    }

    @ParameterizedTest
    @CsvSource({"3, 'APLIKACJA MOBILNA'",
                "4, 'STWORZENIE STRONY WWW'",
                "5, 'BŁĘDY W KODZIE'"})
    public void findById_ValidId(int id, String expectedTitle){
        assertEquals(expectedTitle, taskService.getTaskById(id).getTitle());
    }

    @Test
    public void findById_InvalidId(){
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(999));
    }

    @Test
    public void deleteTask(){
        assertNotNull(taskService.getTaskById(2));
        taskService.deleteTaskById(2);
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(2));
    }

    @Test
    public void deleteTask_taskNotExists(){
        assertFalse(taskJpaRepository.existsById(999));

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(999));
    }

    @Test
    public void createTask(){
        taskService.createTask(getTaskSample());

        assertEquals("Task for tests", taskService.getTaskById(11).getTitle());
    }

    private Task getTaskSample(){

        return new Task(0, "Task for tests", "Description example",
                Priority.MEDIUM, Difficulty.EASY, TaskType.DAILY, Status.ACTIVE,
                200, LocalDate.of(2025, 10, 10));
    }

}
