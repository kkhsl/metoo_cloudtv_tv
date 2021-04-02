package com.hkk.cloudtv.core.service;

import com.hkk.cloudtv.entity.RoomProgram;

import java.util.List;
import java.util.Map;

public interface IRoomProgramService {

    /**
     * 分页查询
     * @param params
     * @return
     */
    List<RoomProgram> getRoomProgram(Map<String, Object> params);

    /**
     * 查询总数
     * @return
     */
    int getAccountByTotal();

    Object save(RoomProgram instance);

    Object update(RoomProgram instance);

    int delete(Long id);

    Object programLiveRoom(RoomProgram instance);

 }
