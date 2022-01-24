package com.mzlalal.oauth2.config.oauth2.enums;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.AccessToken;
import com.mzlalal.base.entity.oauth2.ClientEntity;
import com.mzlalal.base.entity.oauth2.UserEntity;
import com.mzlalal.base.entity.oauth2.vo.GrantCodeVo;
import com.mzlalal.base.entity.oauth2.vo.OauthVo;
import com.mzlalal.oauth2.config.oauth2.service.RedisAuthCodeService;
import com.mzlalal.oauth2.config.oauth2.service.RedisTokenService;
import com.mzlalal.oauth2.service.ClientService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 授权方式枚举类
 * 登录后使用此枚举创建用户TOKEN
 * auth   ->  response  ->   provide  -> createToken 或者 createToken code
 * 授权开始 ->  授权方式  -> 授权校验方式 -> 返回令牌或者授权码
 *
 * @author Mzlalal88
 * @date 2021/7/26 11:04
 */
public enum GrantResponseEnum {
    /**
     * 邮箱授权登录
     */
    MAIL() {
        /**
         * redis授权码服务
         */
        private final RedisAuthCodeService redisAuthCodeService = SpringUtil.getBean(RedisAuthCodeService.class);
        /**
         * redis令牌服务
         */
        private final RedisTokenService redisTokenService = SpringUtil.getBean(RedisTokenService.class);

        @Override
        public Result<BaseEntity> processGrant(OauthVo oauthVo) {
            // 验证邮箱授权码,存储用户信息在authCode中
            String authCode = GrantProvideEnum.MAIL.processLogin(oauthVo);
            // 回调URL格式
            String redirectUriFormat = "{}?code={}&responseType={}&state={}";
            // 格式化
            String redirectUri = StrUtil.format(redirectUriFormat, oauthVo.getRedirectUri()
                    , authCode, GlobalConstant.MAIL, oauthVo.getState());
            // 返回结果
            return Result.ok(GrantCodeVo.builder()
                    .redirectUri(redirectUri)
                    .build());
        }

        @Override
        public Result<BaseEntity> processToken(OauthVo oauthVo, ClientEntity client) {
            // 根据邮箱验证码获取用户信息
            UserEntity entity = redisAuthCodeService.consume(oauthVo.getPassword());
            // 生成token
            AccessToken accessToken = redisTokenService.createAccessToken(entity, client);
            return Result.ok(accessToken);
        }
    },
    /**
     * 密码授权登录
     */
    PASSWORD() {
        /**
         * 客户端服务
         */
        private final ClientService clientService = SpringUtil.getBean(ClientService.class);
        /**
         * redis授权码服务
         */
        private final RedisAuthCodeService redisAuthCodeService = SpringUtil.getBean(RedisAuthCodeService.class);
        /**
         * redis令牌服务
         */
        private final RedisTokenService redisTokenService = SpringUtil.getBean(RedisTokenService.class);
        /**
         * 密码工具
         */
        private final BCryptPasswordEncoder passwordEncoder = SpringUtil.getBean(BCryptPasswordEncoder.class);

        @Override
        public Result<BaseEntity> processGrant(OauthVo oauthVo) {
            // 文本密码授权,存储用户信息在authCode中,直接取出来返回
            String authCode = GrantProvideEnum.PASSWORD.processLogin(oauthVo);
            // 消费授权码,获取用户信息
            UserEntity userEntity = redisAuthCodeService.consume(authCode);
            // 查询客户端
            ClientEntity client = clientService.getOneByClientId(oauthVo.getClientId());
            // 私匙不正确
            if (!passwordEncoder.matches(oauthVo.getClientSecret(), client.getClientSecret())) {
                throw GlobalResult.OAUTH_FAIL.boom();
            }
            // 生成token
            AccessToken accessToken = redisTokenService.createAccessToken(userEntity, client);
            return Result.ok(accessToken);
        }

        @Override
        public Result<BaseEntity> processToken(OauthVo oauthVo, ClientEntity clientEntity) {
            throw GlobalResult.OAUTH_FAIL.boom();
        }
    };

    /**
     * 根据用户名加密码/验证码判断授权是否成功
     *
     * @param oauthVo 参数
     * @return Result<BaseEntity>
     */
    public abstract Result<BaseEntity> processGrant(OauthVo oauthVo);

    /**
     * 授权校验成功后生成用户TOKEN
     *
     * @param oauthVo 参数
     * @param client  客户端
     * @return Result<BaseEntity>
     */
    public abstract Result<BaseEntity> processToken(OauthVo oauthVo, ClientEntity client);

    /**
     * 根据授权方式type获取
     *
     * @param type 授权方式
     * @return GrantResponseEnum
     */
    public static GrantResponseEnum getEnum(String type) {
        for (GrantResponseEnum value : GrantResponseEnum.values()) {
            if (StrUtil.equalsIgnoreCase(type, value.name())) {
                return value;
            }
        }
        throw GlobalResult.OAUTH_RESPONSE_TYPE_NOT_CORRECT.boom();
    }
}
