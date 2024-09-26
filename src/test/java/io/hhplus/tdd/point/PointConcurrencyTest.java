package io.hhplus.tdd.point;

import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointConcurrencyTest {

    @Autowired
    PointService pointService;

    @Test
    @DisplayName("")
    void 비동기_test1() {

        // given
        CompletableFuture.allOf(
                CompletableFuture.supplyAsync(() -> pointService.charge(1L, 1000L)).thenAccept(result -> System.out.println("Result: " + result)),
                CompletableFuture.supplyAsync(() -> pointService.charge(1L, 1000L)).thenAccept(result -> System.out.println("Result: " + result)),
                CompletableFuture.supplyAsync(() -> pointService.use(1L, 500L)).thenAccept(result -> System.out.println("Result: " + result)),
                CompletableFuture.supplyAsync(() -> pointService.use(1L, 500L)).thenAccept(result -> System.out.println("Result: " + result)),
                CompletableFuture.runAsync(() -> pointService.charge(2L, 500L)).thenAccept(result -> System.out.println("Result: " + result)),
                CompletableFuture.runAsync(() -> pointService.charge(2L, 1500L)).thenAccept(result -> System.out.println("Result: " + result)),
                CompletableFuture.runAsync(() -> pointService.use(2L, 500L)).thenAccept(result -> System.out.println("Result: " + result)),
                CompletableFuture.runAsync(() -> pointService.use(2L, 1000L)).thenAccept(result -> System.out.println("Result: " + result))
        ).join();

        // when
        long resultFirstUserPoint = pointService.getUserPoint(1L).point();
        long resultSecondUserPoint = pointService.getUserPoint(2L).point();

        // then
        pointService.getUserPointHistories(1L).forEach(System.out::println);
        pointService.getUserPointHistories(2L).forEach(System.out::println);
        assertThat(resultFirstUserPoint).isEqualTo( 1000 + 1000 -500 - 500);
        assertThat(resultSecondUserPoint).isEqualTo( 2000 + 500 + 1500 - 500 - 1000);
    }
}
