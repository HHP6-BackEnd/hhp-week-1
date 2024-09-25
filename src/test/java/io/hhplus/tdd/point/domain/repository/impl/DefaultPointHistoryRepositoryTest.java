package io.hhplus.tdd.point.domain.repository.impl;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.domain.database.PointHistoryTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static io.hhplus.tdd.point.TransactionType.CHARGE;
import static io.hhplus.tdd.point.TransactionType.USE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class DefaultPointHistoryRepositoryTest {

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private DefaultPointHistoryRepository defaultPointHistoryRepository;

    List<PointHistory> pointHistoryList;

    @BeforeEach
    void setUp() {
        PointHistory pointHistory1 = new PointHistory(1L, 1L, 1000L, CHARGE, System.currentTimeMillis());
        PointHistory pointHistory2 = new PointHistory(2L, 1L, 500L, USE, System.currentTimeMillis());
        PointHistory pointHistory3 = new PointHistory(3L, 1L, 500L, CHARGE, System.currentTimeMillis());

        pointHistoryList.add(pointHistory1);
        pointHistoryList.add(pointHistory2);
        pointHistoryList.add(pointHistory3);
    }

    @Test
    @DisplayName("PointHistory 저장")
    void testInsert() {

        PointHistory pointHistory4 = new PointHistory(4L, 1L, 1000L, USE, System.currentTimeMillis());
        // given
        given(pointHistoryTable.insert(anyLong(), anyLong(), any(), System.currentTimeMillis())).willReturn(pointHistory4);

        // when
        PointHistory pointHistory = defaultPointHistoryRepository.insert(4L, 1L, USE, System.currentTimeMillis());

        // then
        then(pointHistoryTable).should(times(1)).insert(4L, 1L, USE, System.currentTimeMillis());

    }

    @Test
    @DisplayName("")
    void testSelectAllByUserId() {

        // given


        // when


        // then
    }
}