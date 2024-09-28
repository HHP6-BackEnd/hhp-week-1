package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public interface PointService {
    UserPoint charge(long id, long amount);
    UserPoint use(long id, long amount);
    UserPoint getUserPoint(long id);
    List<PointHistory> getUserPointHistories(long userId);
}
