package com.cloud.tv.core.mapper;

import com.cloud.tv.entity.Video;
import com.cloud.tv.req.VideoReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoMapper {

    /**
     * 根据Video Id 查找Video对象
     * @param id Video Id
     * @return
     */
    Video get(Long id);

    List<Video> find(Map params);

    int insert(Video instance);

    int update(Video instance);

    int delete(Long id);

    /**
     * 查询总数
     * @return
     */
    int getAccountByTotal();

    /**
     * pageHelper 分页插件；查询所有
     * @return
     */
    List<Video> selectAll();

    List<Video> findObjByReq(VideoReq req);

    List<Video> findObjByMap(Map map);

    List<Video> findObjBuLiveRoomId(Long id);


    Video videoUnit(Long id);

    /**
     * 根据直播间Id查询回放/and上传视频列表
     * @param id
     * @return
     */
    List<Video> selectByLiveRoomId(Long id);
}
