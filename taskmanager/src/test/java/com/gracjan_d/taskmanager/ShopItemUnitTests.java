package com.gracjan_d.taskmanager;

import com.gracjan_d.taskmanager.entity.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.Random.class)
public class ShopItemUnitTests {
    private ShopItem shopItem;

    @BeforeEach
    public void setInitData(){
        shopItem = new ShopItem();
        shopItem.setId(1);
        shopItem.setTitle("Test Item");
        shopItem.setPrice(5);
        shopItem.setRefreshDate(LocalDate.of(2025, 3, 15));
        shopItem.setRefreshDays(7);
    }

    @Test
    public void countRemainingTime_ValidStartDate(){

        RemainingTime remainingTime = shopItem.countRemainingTime(LocalDateTime.of(2025, 3, 11, 3, 12));
        assertAll(
                () -> assertEquals(3, remainingTime.getDays(), "Should be 3 days"),
                () -> assertEquals(20, remainingTime.getHours(), "Should be 20 hours"),
                () -> assertEquals(48, remainingTime.getMinutes(), "Should be 48 minutes")
        );

        RemainingTime remainingTime2 = shopItem.countRemainingTime(LocalDateTime.of(2025, 3, 11, 0, 0));
        assertAll(
                () -> assertEquals(4, remainingTime2.getDays(), "Should be 4 days"),
                () -> assertEquals(0, remainingTime2.getHours(), "Should be 0 hours"),
                () -> assertEquals(0, remainingTime2.getMinutes(), "Should be 0 minutes")
        );
    }

    @Test
    public void countRemainingTime_FutureStartDate_ShouldReturnZero(){

        RemainingTime remainingTime = shopItem.countRemainingTime(LocalDateTime.of(2025,3,15,6,0));
        assertAll(
                () -> assertEquals(0, remainingTime.getDays(), "Should be 0 days"),
                () -> assertEquals(0, remainingTime.getHours(), "Should be 0 hours"),
                () -> assertEquals(0, remainingTime.getMinutes(), "Should be 0 minutes")
        );
    }

    @ParameterizedTest
    @CsvSource({"1","7","30"})
    public void calculateRefreshDate(int refreshDays){
        ShopItem testShopItem = new ShopItem();
        testShopItem.setRefreshDays(refreshDays);

        assertNull(testShopItem.getRefreshDate());

        testShopItem.calculateRefreshDate();
        assertEquals(LocalDate.now().plusDays(refreshDays), testShopItem.getRefreshDate());
    }
}
