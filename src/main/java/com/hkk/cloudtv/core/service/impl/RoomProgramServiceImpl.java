package com.hkk.cloudtv.core.service.impl;

import com.hkk.cloudtv.core.mapper.RoomProgramMapper;
import com.hkk.cloudtv.core.service.ILiveRoomService;
import com.hkk.cloudtv.core.service.IRoomProgramService;
import com.hkk.cloudtv.core.service.ISysConfigService;
import com.hkk.cloudtv.core.utils.CommUtils;
import com.hkk.cloudtv.entity.LiveRoom;
import com.hkk.cloudtv.entity.RoomProgram;
import com.hkk.cloudtv.entity.SysConfig;
import com.hkk.cloudtv.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RoomProgramServiceImpl implements IRoomProgramService {

    @Autowired
    private RoomProgramMapper roomProgramMapper;
    @Autowired
    private ILiveRoomService liveRoomService;
    @Autowired
    private ISysConfigService sysConfigService;

    @Override
    public List<RoomProgram> getRoomProgram(Map<String, Object> params) {
        return this.roomProgramMapper.findObjByPage(params);
    }

    @Override
    public int getAccountByTotal() {
        return this.roomProgramMapper.findAccountByTotal();
    }

    @Override
    public Object save(RoomProgram instance) {
        if(instance != null){
            if(StringUtils.isEmpty(instance.getTitle())){
                return new Result(400, "The title cannot be empty");
            }
            if(instance.getBeginTime() != null){
                if(instance.getEndTime() != null){
                    if(instance.getBeginTime().after(instance.getEndTime())){
                        return new Result(400, "The start time cannot be greater than the end time");
                    }
                }else{
                    return new Result(400, "Please select a end time");
                }
            }else{
                return new Result(400, "Please select a start time");
            }
           /* int flag = this.roomProgramMapper.creatRoomProgram(instance);
            if(flag == 1){
                return new Result(200, "Successfully");
            }else{
                return new Result(500, "Insert error");
            }*/
            try {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String df = sdf.format(date);
                String bindCode = df + CommUtils.randomString(6);// 推流码
                instance.setBindCode(bindCode);
                this.roomProgramMapper.insert(instance);
                return new Result(200, "Successfully");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(500, "Insert error");
            }
        }else{
            return new Result(400, "Params error");
        }
    }

    @Override
    public Object update(RoomProgram instance) {
           if(instance.getId() != null){
               if(instance.getBeginTime() != null){
                   if(instance.getEndTime() != null){
                       if(instance.getBeginTime().after(instance.getEndTime())){
                           return new Result(400, "The start time cannot be greater than the end time");
                       }
                   }else{
                       return new Result(400, "Please select a end time");
                   }
               }else{
                   return new Result(400, "Please select a start time");
               }
               try {
                   this.roomProgramMapper.update(instance);
                   return new Result(200, "Successfully");
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }else{
               return new Result(400, "Please select what you want to update");
           }
        return null;
    }

    @Override
    public int delete(Long id) {
        return this.roomProgramMapper.delete(id);
    }

    @Override
    public Object programLiveRoom(RoomProgram instance) {
        if(instance != null){
            List<LiveRoom> liveRoomList = this.liveRoomService.findAllLiveRoom();
            boolean flag = true;
            LiveRoom room = null;
            if(liveRoomList.size() < 1){
                room = new LiveRoom();
                room.setAddTime(new Date());
                room.setTitle("Default_Title");
                try {
                    this.liveRoomService.save(room);
                } catch (Exception e) {
                    e.printStackTrace();
                    flag = false;
                }
            }else{
                room = liveRoomList.get(0);
            }
            if(true){
                if(StringUtils.isEmpty(instance.getTitle())){
                    return new Result(400, "The title cannot be empty");
                }
                if(instance.getBeginTime() != null){
                    if(null != instance.getEndTime()){
                        if(instance.getBeginTime().after(instance.getEndTime())){
                            return new Result(400, "The start time cannot be greater than the end time");
                        }
                    }else{
                        return new Result(400, "Please select a end time");
                    }
                }else{
                    return new Result(400, "Please select a start time"); }
                try {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String df = sdf.format(date);
                    String bindCode = df + CommUtils.randomString(6);// 推流码
                    instance.setBindCode(bindCode);
                    instance.setRoomId(room.getId());
                    //rtmp://lk.soarmall.com:1935/hls
                    List<SysConfig> sysConfigList = this.sysConfigService.findSysConfigList();
                    String rtmp = "rtmp://" + sysConfigList.get(0).getIp() + ":1935" + "/hls/" + bindCode;
                    instance.setRtmp(rtmp);
                    this.roomProgramMapper.insert(instance);
                    return new Result(200, "Successfully");
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result(500, "Insert error");
                }
            }else{
                return new Result(500, "Create error");
            }
        }else{
            return new Result(500, "Create error");
        }
    }

}
