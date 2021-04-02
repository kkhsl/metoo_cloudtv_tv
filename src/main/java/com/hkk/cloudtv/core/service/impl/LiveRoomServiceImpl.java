package com.hkk.cloudtv.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hkk.cloudtv.core.mapper.LiveRoomMapper;
import com.hkk.cloudtv.core.service.ILiveRoomService;
import com.hkk.cloudtv.dto.PageInfo;
import com.hkk.cloudtv.entity.LiveRoom;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LiveRoomServiceImpl implements ILiveRoomService {

    @Autowired
    private LiveRoomMapper liveRoomMapper;

    @Override
    public LiveRoom getObjById(Long id) {
        return this.liveRoomMapper.getObjById(id);
    }

    @Override
    public List<LiveRoom> findAllLiveRoom() {
        return this.liveRoomMapper.findAllLiveRoom();
    }

    @Override
    public int save(LiveRoom instance) {
        return this.liveRoomMapper.save(instance);
    }

    @Override
    public int update(LiveRoom instance) {
        return this.liveRoomMapper.update(instance);
    }

    @Override
    public int delete(Long id) {
        return this.liveRoomMapper.delete(id);
    }

    @Override
    public List<LiveRoom> findByPager(Map<String, Object> params) {
        return this.liveRoomMapper.findByPager(params);
    }

    @Override
    public int findAccountByTotal() {
        return this.liveRoomMapper.getAccountByTotal();
    }

    @Override
    public int modify(String property) {
        return this.liveRoomMapper.change(property);
    }

    /**
     * pageHelper
     * @return
     */
    @Override
    public List<LiveRoom> queryLiveRooms(Integer currentPage, Integer pageSize) {
      /*  PageHelper.startPage(currentPage, pageSize);
        List<LiveRoom> liveRoomList = this.liveRoomMapper.selectAll();
        //将查询到的数据封装到PageInfo对象
        PageInfo<LiveRoom> pageInfo = new PageInfo(liveRoomList, pageSize);
        return liveRoomList;*/

        // 分页插件: 查询第1页，每页10行
        Page<LiveRoom> page = PageHelper.startPage(currentPage, pageSize);
        this.liveRoomMapper.selectAll();
        // 数据表的总行数
        page.getTotal();
        // 分页查询结果的总行数
        page.size();
        // 第一个User对象，参考list，序号0是第一个元素，依此类推
        page.get(0);

        return page;
    }
}
