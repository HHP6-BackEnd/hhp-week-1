package io.hhplus.tdd.point.domain.repository.impl;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.domain.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DefaultUserPointRepositoryTest {

	@Mock
	private UserPointTable userPointTable;

	@InjectMocks
	private DefaultUserPointRepository defaultUserPointRepository;

	@Test
	@DisplayName("해당 ID의 UserPoint 조회")
	void selectByIdTest() {

		UserPoint userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());

	    // given
	    given(userPointTable.selectById(anyLong())).willReturn(userPoint);

	    // when
		UserPoint resultUserPoint = defaultUserPointRepository.selectById(1L);

	    // then
		assertThat(resultUserPoint.point()).isEqualTo(1000L);
		then(userPointTable).should(times(1)).selectById(1L);

	}

	@Test
	@DisplayName("해당 ID의 UserPoint 새로추가")
	void insertTest() {

	    // given
	    given(userPointTable.insertOrUpdate(anyLong(), anyLong())).willAnswer(item -> {
	        long id = item.getArgument(0);
	        long amount = item.getArgument(1);
	        return new UserPoint(id, amount, System.currentTimeMillis());
	    });

	    // when
	    UserPoint resultUserPoint = defaultUserPointRepository.insertOrUpdate(2L, 500L);

	    // then
	    assertThat(resultUserPoint.point()).isEqualTo(500L);
	    then(userPointTable).should(times(1)).insertOrUpdate(2L, 500L);
	}

}