package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.UserPoint;

public interface PointService {
    UserPoint charge(long id, long amount);
    UserPoint use(long id, long amount);
}
