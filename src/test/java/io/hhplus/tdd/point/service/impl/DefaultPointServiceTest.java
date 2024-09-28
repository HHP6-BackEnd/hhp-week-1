package io.hhplus.tdd.point.service.impl;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.domain.repository.impl.DefaultPointHistoryRepository;
import io.hhplus.tdd.point.domain.repository.impl.DefaultUserPointRepository;
import io.hhplus.tdd.point.PointValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class DefaultPointServiceTest {

    @Spy
    private PointValidator pointValidator;

    @Mock
    private DefaultUserPointRepository defaultUserPointRepository;

    @Mock
    private DefaultPointHistoryRepository defaultPointHistoryRepository;

    @InjectMocks
    private DefaultPointService defaultPointService;

    UserPoint userPoint;

    @BeforeEach
    void setUp() {
        userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
    }

    @Test
    @DisplayName("[성공] id가 1인 회원이 500 포인트 충전 요청시 충전 성공 후 1500포인트 반환")
    void testCharge() {

        // given

        // 유저포인트 조회 시 1000 으로 조회 되도록 주입
        given(defaultUserPointRepository.selectById(anyLong())).willReturn(userPoint);
        // 마지막 포인트 저장시 1500 포인트 반환 되도록 주입
        given(defaultUserPointRepository.insertOrUpdate(anyLong(), anyLong())).willReturn(new UserPoint(1L, 1500L, System.currentTimeMillis()));

        // when

        // 포인트 500충전
        UserPoint resultUserPoint = defaultPointService.charge(1L, 500L);

        // then

        // 기존 1000포인트 + 500포인트 = 1500 반환 되는지 확인
        assertThat(resultUserPoint.point()).isEqualTo(1500L);

        /*
        // 충전 amount 검증 호출 확인
        then(pointValidator).should(times(1)).verifyChargeAmount(anyLong());
        // 유저 포인트 조회 호출 확인
        then(defaultUserPointRepository).should(times(1)).selectById(1L);
        // 충전 point 검증 호출 확인
        then(pointValidator).should(times(1)).verifyChargePoint(anyLong());
        // 최종 업데이트 호출 확인
        then(defaultUserPointRepository).should(times(1)).insertOrUpdate(1L, 1500L);
        // 포인트 히스토리 저장 호출 확인
        then(defaultPointHistoryRepository).should(times(1)).insert(anyLong(), anyLong(), any(TransactionType.class), anyLong());
        */
    }

    @Test
    @DisplayName("[성공] id가 1인 회원이 500 포인트 사용 요청시 사용 성공 후 500포인트 반환")
    void testUse() {

        // given

        // 유저포인트 조회 시 1000 으로 조회 되도록 주입
        given(defaultUserPointRepository.selectById(anyLong())).willReturn(userPoint);
        // 마지막 포인트 저장시 500 포인트 반환 되도록 주입
        given(defaultUserPointRepository.insertOrUpdate(anyLong(), anyLong())).willReturn(new UserPoint(1L, 500L, System.currentTimeMillis()));

        // when
        // 포인트 500사용
        UserPoint resultUserPoint = defaultPointService.use(1L, 500L);

        // then
        assertThat(resultUserPoint.point()).isEqualTo(500L);

    }

    @Test
    @DisplayName("[실패] validator 에서 0원 이하 충전 에러 발생 시 더이상 진행 되지 않는지 테스트")
    void testReturnError() {

        // given

        // when
        // 포인트 500사용
        assertThatThrownBy(() -> defaultPointService.charge(1L, 0L))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("0 보다 큰 포인트를 충전해야 합니다.");

        // then

    }

}