package io.hhplus.tdd.point;

import org.springframework.stereotype.Component;

@Component
public class PointValidator {

    // 충전 Amount 검증
    public long verifyChargeAmount(long amount) {

        // 충전 amount 가 0이하일 경우 예외발생
        if (amount <= 0) {
            throw new IllegalArgumentException("0 보다 큰 포인트를 충전해야 합니다.");
        }

        // 한번의 충전되는 5000 포인트 이상 충전 불가
        if (amount > 5000) {
            throw new IllegalArgumentException("1회 최대 충전한도는 5000포인트 입니다.");
        }

        return amount;
    }

    // 사용 Amount 검증
    public long verifyUseAmount(long amount) {

        // 사용 amount 가 0이하일 경우 예외발생
        if (amount <= 0) {
            throw new IllegalArgumentException("0 보다 큰 포인트를 사용해야 합니다.");
        }

        // 사용정책 - 한번에 5000 포인트를 초과한 포인트 사용 불가
        if (amount > 5000) {
            throw new IllegalArgumentException("1회 최대 사용한도는 5000포인트 입니다.");
        }

        return amount;
    }

    // 충전 후 업데이트 될 Point 검증
    public long verifyChargePoint(long calculatedPoint) {

        // 충전정책 - 1만 포인트 이상 보유할 수 없게 제한
        if (calculatedPoint > 10000) {
            throw new IllegalArgumentException("최대 1만 포인트까지만 보유할 수 있습니다.");
        }

        return calculatedPoint;
    }

    // 사용 후 업데이트 될 Point 검증
    public long verifyUsePoint(long calculatedPoint) {

        // 계산된 포인트가 0보다 적을 때 예외발생
        if (0 > calculatedPoint) {
            throw new IllegalArgumentException("보유 포인트보다 많은 양을 사용할 수 없습니다.");
        }

        return calculatedPoint;
    }
}
