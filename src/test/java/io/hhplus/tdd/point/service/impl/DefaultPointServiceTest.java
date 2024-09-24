package io.hhplus.tdd.point.service.impl;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.domain.repository.impl.DefaultPointHistoryRepository;
import io.hhplus.tdd.point.domain.repository.impl.DefaultUserPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class DefaultPointServiceTest {

    @Mock
    private DefaultUserPointRepository defaultUserPointRepository;

    @InjectMocks
    private DefaultPointService defaultPointService;

    UserPoint userPoint;

    @BeforeEach
    void setUp() {
        userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
    }

    @Test
    @DisplayName("요청한 회원의 요청한 만큼의 포인트가 충전")
    void testCharge() {

        // given
        long currentPoint = userPoint.point();
        long amount = 500L;
        long updatePoint = currentPoint + amount;

        // 1L 유저 조회시 1000L 마일리지 조회 되도록 주입
        given(defaultUserPointRepository.selectById(1L)).willReturn(userPoint);
        // 1L 유저 포인트 업데이트 시 updatePoint 로 저장 되도록 주입
        given(defaultUserPointRepository.insertOrUpdate(1L, updatePoint)).willReturn(new UserPoint(1L, updatePoint, System.currentTimeMillis()));

        // when
        // 1L 유저 포인트 500충전
        UserPoint resultUserPoint = defaultPointService.charge(1L, amount);

        // then
        // 1000 + 500 = 1500 포인트인지 확인
        assertThat(resultUserPoint.point()).isEqualTo(1500L);
        then(defaultUserPointRepository).should(times(1)).selectById(1L);
        then(defaultUserPointRepository).should(times(1)).insertOrUpdate(1L, updatePoint);

    }

    @Test
    @DisplayName("0원 이하의 포인트 충전 요청시 에러 발생")
    void testAmountUnderZeroCharge() {

        // given
        long amount = 0;

        // when
        // 1L 유저 포인트 500충전
        assertThatThrownBy(() -> defaultPointService.charge(1L, amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("0원 이상 충전 되어야 합니다.");

    }

}