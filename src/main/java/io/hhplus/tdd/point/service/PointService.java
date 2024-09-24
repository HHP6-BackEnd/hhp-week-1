package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.UserPoint;

public interface PointService {
    UserPoint charge(Long id, long amount);
}
