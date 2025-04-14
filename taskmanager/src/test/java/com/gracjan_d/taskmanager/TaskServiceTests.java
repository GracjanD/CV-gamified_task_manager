package com.gracjan_d.taskmanager;

import com.gracjan_d.taskmanager.entity.*;
import com.gracjan_d.taskmanager.exception.TaskNotFoundException;
import com.gracjan_d.taskmanager.repository.TaskJpaRepository;
import com.gracjan_d.taskmanager.service.TaskService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.Random.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TaskServiceTests {

    @Mock
    private TaskJpaRepository taskJpaRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void findById(){
        Task task = new Task();
        task.setTitle("Test");
        // arrange
        when(taskJpaRepository.findById(1)).thenReturn(Optional.of(task));

        // act
        Task givenTask = taskService.getTaskById(1);
        // assert
        assertSame(task, givenTask);

        verify(taskJpaRepository, times(1)).findById(1);
    }

    @Test
    public void findById_shouldThrowTaskNotFoundException(){
        // arrange
        when(taskJpaRepository.findById(1)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1));

        verify(taskJpaRepository, times(1)).findById(1);
    }

    @Test
    public void findAll(){
        List<Task> tasks = getSomeTasks();
        // arrange
        when(taskJpaRepository.findAll()).thenReturn(tasks);
        // act
        List<Task> givenTasks = taskService.getAllTasks();
        // assert
        assertAll(
                () -> assertEquals("Task 1", givenTasks.get(0).getTitle()),
                () -> assertEquals(tasks.size(), givenTasks.size()),
                () -> assertIterableEquals(tasks, givenTasks)
        );

        verify(taskJpaRepository, times(1)).findAll();
    }

    @Test
    public void findAll_emptyList(){
        List<Task> tasks = new ArrayList<>();
        // arrange
        when(taskJpaRepository.findAll()).thenReturn(tasks);
        // act
        List<Task> givenTasks = taskService.getAllTasks();
        // assert
        assertNotNull(givenTasks);
        assertTrue(givenTasks.isEmpty());

        verify(taskJpaRepository, times(1)).findAll();
    }

    @Test
    public void deleteTask(){
        // arrange
        int id = 1;
        when(taskJpaRepository.existsById(id)).thenReturn(true);
        // act
        taskService.deleteTaskById(id);
        // assert
        verify(taskJpaRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteTask_shouldThrowTaskNotFoundException(){
        // arrange
        int id = 1;
        when(taskJpaRepository.existsById(id)).thenReturn(false);
        // act & assert
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(id));

        verify(taskJpaRepository, times(0)).deleteById(id);
    }


    @Test
    public void createTask_typeGeneral(){

        // Arrange
        Task task = new Task(1, "Gym", "Do a training",
                Priority.MEDIUM, Difficulty.EASY, TaskType.GENERAL, Status.ACTIVE,
                5, LocalDate.of(2025,3,15));

        when(taskJpaRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task createdTask = taskService.createTask(task);

        // Asserts
        assertAll(
                () -> assertNotNull(createdTask, "CreatedTask should not be null"),
                () -> assertEquals(1, createdTask.getId(), "Id should be equal to: 1"),
                () -> assertEquals("Gym", createdTask.getTitle(), "Title should be equal to: \"Gym\""),
                () -> assertEquals("Do a training", createdTask.getDescription(), "Description should be equal to: \"Do a training\""),
                () -> assertEquals(Priority.MEDIUM, createdTask.getPriority(), "Priority should be equal to: MEDIUM"),
                () -> assertEquals(Difficulty.EASY, createdTask.getDifficulty(), "Difficulty should be equal to: EASY"),
                () -> assertEquals(LocalDate.of(2025, 3, 15), createdTask.getDeadline(), "Deadline should be equal to: 2025-03-15")
        );

        verify(taskJpaRepository,times(1)).save(any(Task.class));
    }

    @Test
    public void createTask_typeGeneral_nullDeadline(){

        // Arrange
        Task task = new Task(1, "Gym", Status.ACTIVE);
        task.setDeadline(null);
        when(taskJpaRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task newTask = taskService.createTask(task);

        // Assert
        assertNotNull(newTask);
        assertEquals(LocalDate.of(2999,1,1), newTask.getDeadline());
    }

    @Test
    public void createTask_typeWeekly(){

        // Arrange
        Task task = new Task(1, "Gym", "Do a training",
                Priority.MEDIUM, Difficulty.EASY, TaskType.WEEKLY, Status.ACTIVE,
                5, LocalDate.of(2025,3,15));
        LocalDate newDeadline = LocalDate.now().plusDays(7);
        when(taskJpaRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task createdTask = taskService.createTask(task);

        // Asserts
        assertEquals(newDeadline, createdTask.getDeadline(), "Deadline should be equal to: " + newDeadline);
        verify(taskJpaRepository,times(1)).save(any(Task.class));
    }

    @Test
    public void createTask_typeDaily(){

        // Arrange
        Task task = new Task(1, "Gym", "Do a training",
                Priority.MEDIUM, Difficulty.EASY, TaskType.DAILY, Status.ACTIVE,
                5, LocalDate.of(2025,3,15));
        LocalDate newDeadline = LocalDate.now().plusDays(1);
        when(taskJpaRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task createdTask = taskService.createTask(task);

        // Asserts
        assertEquals(newDeadline, createdTask.getDeadline(), "Deadline should be equal to: " + newDeadline);
        verify(taskJpaRepository,times(1)).save(any(Task.class));
    }

    @Test
    public void findAllByTaskStatusTest(){

        // arrange
        List<Task> tasks = getSomeTasks();
        List<Task> completedTasks = tasks.stream()
                .filter(task -> task.getStatus() == Status.COMPLETED)
                .toList();

        when(taskJpaRepository.countTasks()).thenReturn(tasks.size());
        when(taskJpaRepository.countByStatus(Status.COMPLETED)).thenReturn(completedTasks.size());

        // act
        int percent = taskService.getPercentOfTasksByStatus(Status.COMPLETED);

        // assert
        assertEquals(40, percent, "Should be 40%");

        verify(taskJpaRepository, times(1)).countTasks();
        verify(taskJpaRepository, times(1)).countByStatus(Status.COMPLETED);
    }

    @Test
    public void findAllByTaskStatusTest_allTasksAreCompleted(){

        // arrange
        List<Task> tasks = getSomeTasks();
        List<Task> completedTasks = tasks.stream()
                .filter(task -> task.getStatus() == Status.COMPLETED)
                .toList();

        when(taskJpaRepository.countTasks()).thenReturn(completedTasks.size());
        when(taskJpaRepository.countByStatus(Status.COMPLETED)).thenReturn(completedTasks.size());

        // act
        int percent = taskService.getPercentOfTasksByStatus(Status.COMPLETED);

        // assert
        assertEquals(100, percent);

        verify(taskJpaRepository, times(1)).countTasks();
        verify(taskJpaRepository, times(1)).countByStatus(Status.COMPLETED);
    }

    @Test
    public void findAllByTaskStatusTest_noTasksAreCompleted(){

        // arrange
        List<Task> tasks = getSomeTasks();

        when(taskJpaRepository.countTasks()).thenReturn(tasks.size());
        when(taskJpaRepository.countByStatus(Status.COMPLETED)).thenReturn(0);

        // act
        int percent = taskService.getPercentOfTasksByStatus(Status.COMPLETED);

        // assert
        assertEquals(0, percent);

        verify(taskJpaRepository, times(1)).countTasks();
        verify(taskJpaRepository, times(1)).countByStatus(Status.COMPLETED);
    }

    @Test
    public void completeTask(){
        // arrange
        Task task = new Task(1, "Task", "Description",
                Priority.MEDIUM, Difficulty.EASY, TaskType.GENERAL, Status.ACTIVE,
                5, LocalDate.of(2025,3,15));

        // act
        taskService.completeTask(task);

        // assert
        assertEquals(Status.COMPLETED, task.getStatus());
        verify(taskJpaRepository, times(1)).save(task);
    }

    @ParameterizedTest
    @CsvSource({"COMPLETED, 40",
                "ACTIVE, 60"})
    void percentOfTasksByStatus(String status, int expected){
        // arrange
        List<Task> tasks = getSomeTasks();
        Status taskStatus = Status.valueOf(status);

        when(taskJpaRepository.countTasks()).thenReturn(tasks.size());
        when(taskJpaRepository.countByStatus(taskStatus)).thenReturn(tasks.stream()
                .filter(task -> task.getStatus() == taskStatus)
                .toList().size());

        // act
        int percentTasks = taskService.getPercentOfTasksByStatus(taskStatus);

        // assert
        assertEquals(expected, percentTasks);
        verify(taskJpaRepository,times(1)).countTasks();
        verify(taskJpaRepository,times(1)).countByStatus(taskStatus);
    }

    @ParameterizedTest
    @CsvSource({"COMPLETED, 0",
                "ACTIVE, 0"})
    void percentOfTasksByStatus_EmptyList(String status, int expected){
        // arrange
        List<Task> tasks = new ArrayList<>();
        Status taskStatus = Status.valueOf(status);

        when(taskJpaRepository.countTasks()).thenReturn(tasks.size());

        // act
        int percentTasks = taskService.getPercentOfTasksByStatus(taskStatus);

        // assert
        assertEquals(expected, percentTasks);
        verify(taskJpaRepository,times(1)).countTasks();
        verify(taskJpaRepository,times(0)).countByStatus(taskStatus);
    }

    private List<Task> getSomeTasks(){
        return new ArrayList<>(List.of(
                new Task(1, "Task 1", Status.ACTIVE),
                new Task(2, "Task 2", Status.ACTIVE),
                new Task(3, "Task 3", Status.ACTIVE),
                new Task(4, "Task 4", Status.COMPLETED),
                new Task(5, "Task 5", Status.COMPLETED)
        ));
    }
}
