package com.shf.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shf.yygh.model.hosp.Schedule;
import com.shf.yygh.vo.hosp.ScheduleOrderVo;
import com.shf.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService extends IService<Schedule> {
    void save(Map<String, Object> paramMap);

    Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    /**
     * 查询排班规则数据
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    /**
     * 根据医院编号 、科室编号和工作日期，查询排班详细信息
     * @param hoscode
     * @param depcode
     * @param workDate
     * @return
     */
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    /**
     * 获取可预约排班数据
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    /**
     * 根据排班id获取排班数据
     * @param scheduleId
     * @return
     */
    Schedule getById(String scheduleId);

    /**
     * 根据排班id获取预约下单数据
     * @param scheduleId
     * @return
     */
    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    /**
     * 更新排班数据
     * @param schedule
     */
    void update(Schedule schedule);
}
