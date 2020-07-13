package com.ihrm.system.service;
import com.baidu.aip.util.Base64Util;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.FaceLoginResult;
import com.ihrm.domain.system.response.QRCode;
import com.ihrm.system.utils.BaiduAiUtil;
import com.ihrm.system.utils.QRCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Service
public class FaceLoginService {

    @Value("${qr.url}")
    private String url;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private QRCodeUtil qrCodeUtil;

    @Autowired
    private BaiduAiUtil baiduAiUtil;

    @Autowired
    private UserService userService;


    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

	//创建二维码
    public QRCode getQRCode() throws Exception {
        String code = idWorker.nextId()+"";
        String content = url+"?code="+code;
        String file = qrCodeUtil.crateQRCode(content);
        FaceLoginResult result = new FaceLoginResult("-1");
        redisTemplate.boundValueOps(getCacheKey(code)).set(result,10, TimeUnit.MINUTES);
        return new QRCode(code,file);
    }

	//根据唯一标识，查询用户是否登录成功
    public FaceLoginResult checkQRCode(String code) {
        String key = getCacheKey(code);
        Object result = redisTemplate.opsForValue().get(key);
        return (FaceLoginResult)result;
    }

	//扫描二维码之后，使用拍摄照片进行登录
    public String loginByFace(String code, MultipartFile attachment) throws Exception {
        String id = baiduAiUtil.faceSearch(Base64Util.encode(attachment.getBytes()));
        FaceLoginResult result = new FaceLoginResult("0");
        if(id != null){
            User user = userService.findById(id);
            if(user != null){
                Subject subject = SecurityUtils.getSubject();
                subject.login(new UsernamePasswordToken(user.getMobile(),user.getPassword()));
                String token = (String) subject.getSession().getId();
                result = new FaceLoginResult("1",token,id);
            }
        }
        redisTemplate.boundValueOps(getCacheKey(code)).set(result,10,TimeUnit.MINUTES);
        return id;
    }

	//构造缓存key
    private String getCacheKey(String code) {
        return "qrcode_" + code;
    }
}
