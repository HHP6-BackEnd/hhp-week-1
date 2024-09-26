package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointValidatorTest {

    private final PointValidator pointValidator = new PointValidator();

    @Test
    @DisplayName("충전 amount가 1000 일때 검증 시 검증 성공하고 amount 1000 반환")
    void successTestVerifyChargeAmount() {

        // given
        long amount = 1000L;

        // when
        long validAmount = pointValidator.verifyChargeAmount(amount);

        // then
        assertThat(validAmount).isEqualTo(1000L);
    }

    @Test
    @DisplayName("충전 amount가 0이하 일때 검증 시 검증 실패하고 예외 반환")
    public void testVerifyChargeAmountUnderZero() {
        // given
        long amount = 0L;

        // when
        // 0포인트를 초과하여 충전 하여야 함
        assertThatThrownBy(() -> pointValidator.verifyChargeAmount(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("0 보다 큰 포인트를 충전해야 합니다.");
    }

    @Test
    @DisplayName("충전 amount가 2500 일때 검증 시 검증 실패하고 예외 반환")
    public void testVerifyChargeAmountOverMaxLimit() {
        // given
        long amount = 2500L;

        // when
        // 충전정책 - 1회 최대 2000포인트 까지 충전가능
        assertThatThrownBy(() -> pointValidator.verifyChargeAmount(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("1회 최대 충전한도는 2000포인트 입니다.");
    }

    @Test
    @DisplayName("사용 amount가 1000 일때 검증 시 검증 성공하고 amount 1000 반환")
    void successTestVerifyUseAmount() {

        // given
        long amount = 1000L;

        // when
        long validAmount = pointValidator.verifyUseAmount(amount);

        // then
        assertThat(validAmount).isEqualTo(1000L);
    }

    @Test
    @DisplayName("사용 amount가 0이하 일때 검증 시 검증 실패하고 예외 반환")
    public void testVerifyUseAmountUnderZero() {
        // given
        long amount = 0L;

        // when
        // 0포인트를 초과하여 사용 하여야 함
        assertThatThrownBy(() -> pointValidator.verifyUseAmount(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("0 보다 큰 포인트를 사용해야 합니다.");
    }

    @Test
    @DisplayName("사용 amount가 6000 일때 검증 시 검증 실패하고 예외 반환")
    public void testVerifyUseAmountOverMaxLimit() {
        // given
        long amount = 6000L;

        // when
        // 충전정책 - 1회 최대 2000원 까지 충전가능
        assertThatThrownBy(() -> pointValidator.verifyUseAmount(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("1회 최대 사용한도는 5000포인트 입니다.");
    }

    @Test
    @DisplayName("충전 이후 업데이트 될 포인트가 2000 일때 검증 시 검증 성공하고 포인트 2000L 반환")
    void successTestVerifyChargePoint() {

        // given
        long updateChargePoint = 2000L;

        // when
        long validChargePoint = pointValidator.verifyChargePoint(updateChargePoint);

        // then
        assertThat(validChargePoint).isEqualTo(2000L);
    }

    @Test
    @DisplayName("충전 이후 업데이트 될 포인트가 11000 일때 검증 시 검증 실패하고 예외 반환")
    public void testVerifyChargePointOverMaxLimit() {
        // given
        long updateChargePoint = 11000L;

        // when
        // 충전정책 - 1회 최대 2000원 까지 충전가능
        assertThatThrownBy(() -> pointValidator.verifyChargePoint(updateChargePoint))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("최대 1만 포인트까지만 보유할 수 있습니다.");
    }

    @Test
    @DisplayName("사용 이후 업데이트 될 포인트가 1000 일때 검증 시 검증 성공하고 포인트 2000L 반환")
    void successTestVerifyUsePoint() {

        // given
        long updateUsePoint = 1000L;

        // when
        long validUsePoint = pointValidator.verifyUsePoint(updateUsePoint);

        // then
        assertThat(validUsePoint).isEqualTo(1000L);
    }

    @Test
    @DisplayName("사용 이후 업데이트 될 포인트가 0 미만 일때 검증 시 검증 실패하고 예외 반환")
    public void testVerifyUsePointUnderZero() {
        // given
        long updateUsePoint = -100L;

        // when
        // 충전정책 - 1회 최대 2000원 까지 충전가능
        assertThatThrownBy(() -> pointValidator.verifyUsePoint(updateUsePoint))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("보유 포인트보다 많은 양을 사용할 수 없습니다.");
    }
}