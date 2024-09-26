package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.impl.DefaultPointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PointController.class)
@AutoConfigureMockMvc
class PointControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private DefaultPointService defaultPointService;

    UserPoint userPoint;

    @BeforeEach
    void setUp() {
        userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
    }

    // /point/{id}
    @Test
    @DisplayName("특정 유저의 포인트를 조회")
    void testGetUserPoint() throws Exception {

        // given

        long id = 1L;
        given(defaultPointService.getUserPoint(id)).willReturn(userPoint);

        // when

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/point/{id}", id));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andDo(print())
                .andReturn();
    }

    // /point/{id}/histories
    @Test
    @DisplayName("특정 유저의 포인트 충전/이용 내역 조회")
    void testGetUserHistories() throws Exception {

        // given

        long id = 1L;
        List<PointHistory> pointHistoryList = Collections.emptyList();
        given(defaultPointService.getUserPointHistories(id)).willReturn(pointHistoryList);

        // when

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/point/{id}/histories", id));

        // then

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("특정 유저의 포인트를 충전하는 기능")
    public void testCharge() throws Exception{

        // given

        long id = 1L;
        long amount = 100L;
        given(defaultPointService.charge(id, amount)).willReturn(userPoint);

        // when

        ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.patch("/point/{id}/charge", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(amount)));

        // then

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("특정 유저의 포인트를 사용하는 기능")
    public void testUse () throws Exception{

        // given

        long id = 1L;
        long amount = 100L;
        given(defaultPointService.use(id, amount)).willReturn(userPoint);

        // when

        ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.patch("/point/{id}/use", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(amount)));

        // then

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andDo(print())
                .andReturn();
    }
}