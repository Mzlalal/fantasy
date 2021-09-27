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
 * auth   ->  response  ->   provide  -> token 或者 token code
 * 授权开始 ->  授权类型  -> 授权校验方式 -> 返回令牌或者授权码
 *
 * @author Mzlalal88
 * @date 2021/7/26 11:04
 */
public enum GrantResponseEnum {
    /**
     * 密码授权登录
     */
    PASSWORD() {
        @Override
        public Result<BaseEntity> processGrant(OauthVo oauthVo) {
            // 密码直接授权
            String authCode = GrantProvideEnum.PASSWORD
                    .validate(oauthVo.getUsername(), oauthVo.getGrantValue());
            // 消费授权码,获取用户信息
            UserEntity userEntity = SpringUtil.getBean(RedisAuthCodeService.class).consume(authCode);
            // 查询客户端
            ClientEntity client = SpringUtil.getBean(ClientService.class).getOneByClientId(oauthVo.getClientId());
            // 私匙不正确
            if (!SpringUtil.getBean(BCryptPasswordEncoder.class).matches(oauthVo.getClientSecret(), client.getClientSecret())) {
                throw GlobalResult.OAUTH_FAIL.boom();
            }
            // 生成token
            AccessToken accessToken = SpringUtil.getBean(RedisTokenService.class).createAccessToken(userEntity, client);
            return Result.ok(accessToken);
        }

        @Override
        public Result<BaseEntity> processToken(OauthVo oauthVo, ClientEntity clientEntity) {
            throw GlobalResult.OAUTH_FAIL.boom();
        }
    },
    /**
     * 授权码登录
     */
    MAIL() {
        @Override
        public Result<BaseEntity> processGrant(OauthVo oauthVo) {
            // 验证邮箱授权码
            String authCode = GrantProvideEnum.MAIL.validate(oauthVo.getUsername(), oauthVo.getGrantValue());
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
            // 验证二次授权码
            UserEntity entity = SpringUtil.getBean(RedisAuthCodeService.class)
                    .consume(oauthVo.getGrantValue());
            // 生成token
            AccessToken accessToken = SpringUtil.getBean(RedisTokenService.class)
                    .createAccessToken(entity, client);
            return Result.ok(accessToken);
        }
    };

    /**
     * 处理授权
     *
     * @param oauthVo 参数
     * @return Result
     */
    public abstract Result<BaseEntity> processGrant(OauthVo oauthVo);

    /**
     * 处理结果
     *
     * @param oauthVo 参数
     * @param client  客户端
     * @return Result
     */
    public abstract Result<BaseEntity> processToken(OauthVo oauthVo, ClientEntity client);

    /**
     * 根据授权方式type获取
     *
     * @param type 授权方式
     * @return GrantEnum
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
