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
    @DisplayName("유저 1의 포인트 충전, 사용이 비동기적으로 요청 될 때, 순차적으로 처리 되는지 확인")
    void concurrencyOneUserAsyncIntegrationTest() {

        // given
        CompletableFuture.allOf(
                CompletableFuture.supplyAsync(() -> pointService.charge(1L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.charge(1L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(1L, 500L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(1L, 500L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.charge(1L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.charge(1L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(1L, 500L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(1L, 500L)).thenAccept(System.out::println)
        ).join();

        // when
        long resultFirstUserPoint = pointService.getUserPoint(1L).point();

        // then
        pointService.getUserPointHistories(1L).forEach(System.out::println);
        assertThat(resultFirstUserPoint).isEqualTo( 1000 + 1000 - 500 - 500 + 1000 + 1000 - 500 - 500);
    }

    @Test
    @DisplayName("유저 2와 유저3의 포인트 충전, 사용이 비동기적으로 요청 될 때, 순차적으로 처리 되는지 확인")
    void concurrencyTwoUserAsyncIntegrationTest() {

        // given
        CompletableFuture.allOf(
                CompletableFuture.supplyAsync(() -> pointService.charge(2L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.charge(3L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(2L, 500L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(3L, 500L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.charge(2L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.charge(3L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(2L, 500L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(3L, 500L)).thenAccept(System.out::println)
        ).join();

        // when
        long resultFirstUserPoint = pointService.getUserPoint(2L).point();
        long resultSecondUserPoint = pointService.getUserPoint(3L).point();

        // then
        pointService.getUserPointHistories(2L).forEach(System.out::println);
        pointService.getUserPointHistories(3L).forEach(System.out::println);
        assertThat(resultFirstUserPoint).isEqualTo( 1000 - 500 + 1000 - 500);
        assertThat(resultSecondUserPoint).isEqualTo( 1000 - 500 + 1000 - 500);
    }
}
