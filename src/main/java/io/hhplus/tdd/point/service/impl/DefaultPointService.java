package io.hhplus.tdd.point.service.impl;

import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.domain.repository.impl.DefaultPointHistoryRepository;
import io.hhplus.tdd.point.domain.repository.impl.DefaultUserPointRepository;
import io.hhplus.tdd.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultPointService implements PointService {

    private final DefaultUserPointRepository defaultUserPointRepository;
    private final DefaultPointHistoryRepository defaultPointHistoryRepository;

    @Override
    public UserPoint charge(long id, long amount) {
        if (0 >= amount) {
            throw new IllegalArgumentException("포인트는 0원 이상 충전 되어야 합니다.");
        }

        long updateAmount =  defaultUserPointRepository.selectById(id).chargePoint(amount);

        UserPoint updateUserPoint = defaultUserPointRepository.insertOrUpdate(id, updateAmount);
        defaultPointHistoryRepository.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        return updateUserPoint;
    }

    @Override
    public UserPoint use(long id, long amount) {

        if (amount > 5000) {
            throw new IllegalArgumentException("포인트는 5000원 이상 사용 불가합니다.");
        }

        long updateAmount = defaultUserPointRepository.selectById(id).usePoint(amount);

        if (0 > updateAmount) {
            throw new IllegalArgumentException("포인트는 잔고이상 사용할 수 없습니다.");
        }

        UserPoint updateUserPoint = defaultUserPointRepository.insertOrUpdate(id, updateAmount);
        defaultPointHistoryRepository.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        return updateUserPoint;
    }
}
