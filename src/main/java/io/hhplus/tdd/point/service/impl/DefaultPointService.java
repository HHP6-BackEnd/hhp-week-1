package io.hhplus.tdd.point.service.impl;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.domain.repository.impl.DefaultPointHistoryRepository;
import io.hhplus.tdd.point.domain.repository.impl.DefaultUserPointRepository;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.PointValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class DefaultPointService implements PointService {

    private final DefaultUserPointRepository defaultUserPointRepository;
    private final DefaultPointHistoryRepository defaultPointHistoryRepository;
    private final PointValidator pointValidator;
    private final ConcurrentHashMap<Long, ReentrantLock> userMap = new ConcurrentHashMap<>();

    @Override
    public UserPoint charge(long id, long amount) {

        // 각 회원 id 에 따른 동시성 보장
        ReentrantLock lock = userMap.computeIfAbsent(id, userId -> new ReentrantLock());
        lock.lock();

        try {
            // amount 검증
            long validChargeAmount = pointValidator.verifyChargeAmount(amount);
            // 검증된 amount 를 통한 충전 이후 포인트 총합 계산
            long calculatedChargePoint =  defaultUserPointRepository.selectById(id).calculateChargePoint(validChargeAmount);
            // totalPoint 검증
            long validTotalPoint = pointValidator.verifyChargePoint(calculatedChargePoint);

            // 최종 검증된 포인트로 업데이트
            UserPoint returnUserPoint = defaultUserPointRepository.insertOrUpdate(id, validTotalPoint);

            // 포인트 히스토리 기록
            defaultPointHistoryRepository.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

            return returnUserPoint;

        } finally {
            lock.unlock();
        }
    }

    @Override
    public UserPoint use(long id, long amount) {

        // 각 회원 id 에 따른 동시성 보장
        ReentrantLock lock = userMap.computeIfAbsent(id, userId -> new ReentrantLock());
        lock.lock();

        try {

            long validUseAmount =  pointValidator.verifyUseAmount(amount);
            // 검증된 amount 를 통한 충전 이후 포인트 총합 계산
            long calculatedUsePoint =  defaultUserPointRepository.selectById(id).calculateUsePoint(validUseAmount);
            // totalPoint 검증
            long validTotalPoint = pointValidator.verifyUsePoint(calculatedUsePoint);

            // 최종 검증된 포인트로 업데이트
            UserPoint updateUserPoint = defaultUserPointRepository.insertOrUpdate(id, validTotalPoint);

            // 포인트 히스토리 기록
            defaultPointHistoryRepository.insert(id, amount, TransactionType.USE, System.currentTimeMillis());

            return updateUserPoint;

        } finally {
            lock.unlock();
        }
    }

    @Override
    public UserPoint getUserPoint(long id) {
        return defaultUserPointRepository.selectById(id);
    }

    @Override
    public List<PointHistory> getUserPointHistories(long userId) {
        return defaultPointHistoryRepository.selectAllByUserId(userId);
    }
}
