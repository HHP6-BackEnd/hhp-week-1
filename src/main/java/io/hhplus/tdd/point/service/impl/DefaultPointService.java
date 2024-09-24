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
    public UserPoint charge(Long id, long amount) {
        if (0 >= amount) {
            throw new IllegalArgumentException("0원 이상 충전 되어야 합니다.");
        }

        long currentAmount = defaultUserPointRepository.selectById(id).point();
        long updateAmount = amount + currentAmount;

        UserPoint updateUserPoint = defaultUserPointRepository.insertOrUpdate(id, updateAmount);
        defaultPointHistoryRepository.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        return updateUserPoint;
    }
}
