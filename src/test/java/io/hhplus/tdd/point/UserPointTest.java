package io.hhplus.tdd.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserPointTest {

    UserPoint userPoint;

    @BeforeEach
    void setUp() {
        userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
    }

    @Test
    @DisplayName("기존 1000 포인트에 500 충전 포인트 요청 계산 후 1500 포인트 반환")
    void testCalculateChargePoint() {

        // given
        long amount = 500L;

        // when
        long calculatedPoint = userPoint.calculateChargePoint(amount);

        // then
        assertThat(calculatedPoint).isEqualTo(1500);

    }

    @Test
    @DisplayName("기존 1000 포인트에 500 사용 포인트 요청 계산 후 500 포인트 반환")
    void testCalculateUsePoint() {

        // given
        long amount = 500L;

        // when
        long calculatedPoint = userPoint.calculateUsePoint(amount);

        // then
        assertThat(calculatedPoint).isEqualTo(500);

    }
}