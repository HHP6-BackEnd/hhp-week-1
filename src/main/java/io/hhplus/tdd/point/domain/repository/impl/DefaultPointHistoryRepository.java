package io.hhplus.tdd.point.domain.repository.impl;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.domain.database.PointHistoryTable;
import io.hhplus.tdd.point.domain.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DefaultPointHistoryRepository implements PointHistoryRepository {

	private final PointHistoryTable pointHistoryTable;

	@Override
	public PointHistory insert(long userId, long amount, TransactionType type, long updateMillis) {
		return pointHistoryTable.insert(userId, amount, type, updateMillis);
	}

	@Override
	public List<PointHistory> selectAllByUserId(long userId) {
		return pointHistoryTable.selectAllByUserId(userId);
	}
}
