package com.hkk.cloudtv.core.mapper;

import com.hkk.cloudtv.entity.LiveRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LiveRoomMapper {

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
    int save(LiveRoom instance);

    /**
     * 更新一个LiveRoom 对象
     * @param instance
     * @return
     */
    int update(LiveRoom instance);

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
    public int getAccountByTotal();

    int change(String isEnable);


    /**
     * pageHelper 分页插件；查询所有
     * @return
     */
    List<LiveRoom> selectAll();
}
