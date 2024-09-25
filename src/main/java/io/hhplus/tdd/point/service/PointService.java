package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;

public interface PointService {
    UserPoint charge(long id, long amount, TransactionType type);
    UserPoint use(long id, long amount, TransactionType type);
}
