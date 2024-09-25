package io.hhplus.tdd.point.service.impl;

import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.domain.repository.impl.DefaultPointHistoryRepository;
import io.hhplus.tdd.point.domain.repository.impl.DefaultUserPointRepository;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.service.PointValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultPointService implements PointService {

    private final DefaultUserPointRepository defaultUserPointRepository;
    private final DefaultPointHistoryRepository defaultPointHistoryRepository;
    private final PointValidator pointValidator;

    @Override
    public UserPoint charge(long id, long amount, TransactionType type) {

        // amount 검증
        long validChargeAmount =  pointValidator.verifyChargeAmount(amount);
        // 검증된 amount 를 통한 충전 이후 포인트 총합 계산
        long calculatedChargePoint =  defaultUserPointRepository.selectById(id).calculateChargePoint(validChargeAmount);
        // totalPoint 검증
        long validTotalPoint = pointValidator.verifyChargePoint(calculatedChargePoint);
        // 최종 검증된 포인트로 업데이트
        UserPoint returnUserPoint = defaultUserPointRepository.insertOrUpdate(id, validTotalPoint);

        // 포인트 히스토리 기록
        defaultPointHistoryRepository.insert(id, amount, type, System.currentTimeMillis());

        return returnUserPoint;
    }

    @Override
    public UserPoint use(long id, long amount, TransactionType type) {

        long validUseAmount =  pointValidator.verifyUseAmount(amount);
        // 검증된 amount 를 통한 충전 이후 포인트 총합 계산
        long calculatedUsePoint =  defaultUserPointRepository.selectById(id).calculateUsePoint(validUseAmount);
        // totalPoint 검증
        long validTotalPoint = pointValidator.verifyUsePoint(calculatedUsePoint);
        // 최종 검증된 포인트로 업데이트
        UserPoint updateUserPoint = defaultUserPointRepository.insertOrUpdate(id, validTotalPoint);

        // 포인트 히스토리 기록
        defaultPointHistoryRepository.insert(id, amount, type, System.currentTimeMillis());

        return updateUserPoint;
    }
}
