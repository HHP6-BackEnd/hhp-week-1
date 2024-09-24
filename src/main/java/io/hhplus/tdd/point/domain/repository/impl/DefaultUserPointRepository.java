package io.hhplus.tdd.point.domain.repository.impl;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.domain.database.UserPointTable;
import io.hhplus.tdd.point.domain.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DefaultUserPointRepository implements UserPointRepository{

	private final UserPointTable userPointTable;

	@Override
	public UserPoint selectById(Long id) {
		return userPointTable.selectById(id);
	}

	@Override
	public UserPoint insertOrUpdate(long id, long amount) {
		return userPointTable.insertOrUpdate(id, amount);
	}
}
