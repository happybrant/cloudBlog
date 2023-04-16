package com.kongfu.backend.controller;

import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.vo.StatisticData;
import com.kongfu.backend.service.StatisticService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author 付聪
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    @Resource
    StatisticService statisticService;

    /**
     * 获取首页信息
     *
     * @return
     */
    @RequestMapping("getIndexData")
    @ResponseBody
    public ResponseResult<StatisticData> getIndexData() {

        StatisticData statisticData = statisticService.getStatisticData();
        return new ResponseResult<>(ResponseResultCode.Success, "查询成功", statisticData);
    }
}
