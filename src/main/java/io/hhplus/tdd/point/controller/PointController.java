package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.impl.DefaultPointService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    private final DefaultPointService defaultPointService;

    // 특정 유저의 포인트를 조회
    @GetMapping("{id}")
    public UserPoint point(
            @PathVariable("id") long id
    ) {
        return defaultPointService.getUserPoint(id);
    }

    // 특정 유저의 포인트 충전/이용 내역을 조회
    @GetMapping("{id}/histories")
    public List<PointHistory> history(
            @PathVariable("id") long id
    ) {
        return defaultPointService.getUserPointHistories(id);
    }

    // 특정 유저의 포인트를 충전하는 기능
    @PatchMapping("{id}/charge")
    public UserPoint charge(
            @PathVariable("id") long id,
            @RequestBody long amount
    ) {
        return defaultPointService.charge(id, amount);
    }

    // 특정 유저의 포인트를 사용하는 기능
    @PatchMapping("{id}/use")
    public UserPoint use(
            @PathVariable("id") long id,
            @RequestBody long amount
    ) {
        return defaultPointService.use(id, amount);
    }
}
