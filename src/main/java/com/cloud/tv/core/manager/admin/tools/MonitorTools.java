package com.cloud.tv.core.manager.admin.tools;

import com.alibaba.fastjson.JSONObject;
import com.cloud.tv.core.service.ISysConfigService;
import com.cloud.tv.core.utils.CommUtils;
import com.cloud.tv.dto.MonitorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.json.JsonObject;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 异步调用监控方法
 */
@Component
public class MonitorTools {
    @Autowired
    private ISysConfigService configService;
//  private static final String url = "http://lkgl.link-ai.com/monitor/save";
//  private static final String url = "http://154.85.59.210:9090/monitor/save";
    private static final String url = "http://yllk.link-ai.com/lkapi/monitor/save";// 监控地址

    public MonitorTools() {
    }

    public String createAppId() {
        return CommUtils.randomString(6);
    }

    public void monitor(String timestamp, String sysConfigTitle, String roomProgramTitle, String liveRoomTitle, String appId, String userName, Long liveRoomId, Long roomProgramId, Long userId) {
        MonitorDto monitorDto = new MonitorDto();
        monitorDto.setBeginTime(timestamp);
        monitorDto.setTitle(sysConfigTitle);
        monitorDto.setRoomProgramTitle(roomProgramTitle);
        monitorDto.setLiveRoomTitle(liveRoomTitle);
        monitorDto.setAppId(appId);
        monitorDto.setUsername(userName);
        monitorDto.setLiveRoomId(liveRoomId);
        monitorDto.setRoomProgramId(roomProgramId);
        monitorDto.setUserId(userId);
        String signTemp = liveRoomId + roomProgramId + userId + appId + timestamp;
        String sign = this.getSHA256StrJava(signTemp);
        monitorDto.setSign(sign);
        this.sendMonitor(monitorDto);
    }

    @Async
    public void sendMonitor(MonitorDto dto) {
        if (dto != null) {
            RestTemplate client = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpMethod method = HttpMethod.POST;
            JSONObject postData = new JSONObject();
            headers.setContentType(MediaType.APPLICATION_JSON);

            try {
                JSONObject var6 = (JSONObject)client.postForEntity(url, JSONObject.toJSON(dto), JSONObject.class, new Object[0]).getBody();
            } catch (RestClientException var7) {
                var7.printStackTrace();
            }
        }

    }

    public String getSHA256StrJava(String str) {
        String encodeStr = "";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = this.byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

        return encodeStr;
    }

    public String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;

        for(int i = 0; i < bytes.length; ++i) {
            temp = Integer.toHexString(bytes[i] & 255);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }

            stringBuffer.append(temp);
        }

        return stringBuffer.toString();
    }

    public Long duration(Date time1, Date time2) {
        if (time1 != null && time1 != null) {
            long diff = time2.getTime() - time1.getTime();
            long days = diff / -1702967296L;
            System.out.println(days);
            long hours = (diff - days * 86400000L) / 3600000L;
            System.out.println(hours);
            long minutes = (diff - days * 86400000L - hours * 3600000L) / 60000L;
            System.out.println(minutes);
            long second = (diff - days * 86400000L - hours * 3600000L) / 60000L - minutes / 60L;
            return diff;
        } else {
            return 1L;
        }
    }
}

