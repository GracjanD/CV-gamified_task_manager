package com.gracjan_d.taskmanager;


import com.gracjan_d.taskmanager.entity.Difficulty;
import com.gracjan_d.taskmanager.entity.Priority;
import com.gracjan_d.taskmanager.entity.RemainingTime;
import com.gracjan_d.taskmanager.entity.Task;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.Random.class)
public class TaskUnitTests {

    private Task task;

    @BeforeEach
    public void setInitData(){
        task = new Task();
        task.setId(1);
        task.setTitle("Gym");
        task.setDescription("Do a training");
        task.setPriority(Priority.MEDIUM);
        task.setDifficulty(Difficulty.EASY);
        task.setPrize(5);
        task.setDeadline(LocalDate.of(2025, 3, 15));
    }

    @Test
    public void countRemainingTime_ValidStartDate(){

        RemainingTime remainingTime = task.countRemainingTime(LocalDateTime.of(2025, 3, 11, 3, 12));
        assertAll(
                () -> assertEquals(3, remainingTime.getDays(), "Should be 3 days"),
                () -> assertEquals(20, remainingTime.getHours(), "Should be 20 hours"),
                () -> assertEquals(48, remainingTime.getMinutes(), "Should be 48 minutes")
        );

        RemainingTime remainingTime2 = task.countRemainingTime(LocalDateTime.of(2025, 3, 11, 0, 0));
        assertAll(
                () -> assertEquals(4, remainingTime2.getDays(), "Should be 4 days"),
                () -> assertEquals(0, remainingTime2.getHours(), "Should be 0 hours"),
                () -> assertEquals(0, remainingTime2.getMinutes(), "Should be 0 minutes")
        );
    }

    @Test
    public void countRemainingTime_FutureStartDate_ShouldReturnZero(){

        RemainingTime remainingTime = task.countRemainingTime(LocalDateTime.of(2025,3,15,6,0));
        assertAll(
                () -> assertEquals(0, remainingTime.getDays(), "Should be 0 days"),
                () -> assertEquals(0, remainingTime.getHours(), "Should be 0 hours"),
                () -> assertEquals(0, remainingTime.getMinutes(), "Should be 0 minutes")
        );
    }

    @Test
    public void isAfterDeadline(){
        assertTrue(task.isAfterDeadline());
    }
}
