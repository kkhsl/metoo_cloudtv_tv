package com.hkk.cloudtv.core.service;

import com.hkk.cloudtv.entity.LiveRoom;

import java.util.List;
import java.util.Map;

public interface ILiveRoomService {
    /**
     * 根据LiveRoom id 获取一个LiveRoom 对象
     * @param id
     * @return
     */
    public LiveRoom getObjById(Long id);

    /**
     * 查询所有的LiveRoom对象
     * @return
     */
    List<LiveRoom> findAllLiveRoom();

    /**
     * 保存一个LiveRoom 对象
     * @param instance
     * @return
     */
    public int save(LiveRoom instance);

    /**
     * 更新一个LiveRoom 对象
     * @param instance
     * @return
     */
    public int update(LiveRoom instance);

    /**
     *删除直播间
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     *分页查询
     * @param params
     * @return
     */
    public List<LiveRoom> findByPager(Map<String, Object> params);

    /**
     * 查询总数
     * @return
     */
    public int findAccountByTotal();

    int modify(String property);

    /**
     * 分页查询
     * @return
     */
   List<LiveRoom> queryLiveRooms(Integer currentPage, Integer pageSize);
}
