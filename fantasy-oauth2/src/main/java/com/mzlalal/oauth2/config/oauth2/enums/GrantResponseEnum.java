package com.mzlalal.oauth2.config.oauth2.enums;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.BaseEntity;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.AccessToken;
import com.mzlalal.base.entity.oauth2.ClientEntity;
import com.mzlalal.base.entity.oauth2.UserEntity;
import com.mzlalal.base.entity.oauth2.req.CreateTokenReq;
import com.mzlalal.base.entity.oauth2.vo.GrantCodeVo;
import com.mzlalal.base.entity.oauth2.vo.OauthVo;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.oauth2.config.oauth2.service.RedisAuthorizeCodeService;
import com.mzlalal.oauth2.config.oauth2.service.RedisTokenService;
import com.mzlalal.oauth2.service.ClientService;
import com.mzlalal.oauth2.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
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
        private final RedisAuthorizeCodeService redisAuthorizeCodeService = SpringUtil.getBean(RedisAuthorizeCodeService.class);
        /**
         * redis令牌服务
         */
        private final RedisTokenService redisTokenService = SpringUtil.getBean(RedisTokenService.class);
        /**
         * 用户服务
         */
        private final UserService userService = SpringUtil.getBean(UserService.class);
        /**
         * redis授权码服务
         */
        private final StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        /**
         * 密码工具
         */
        private final BCryptPasswordEncoder passwordEncoder = SpringUtil.getBean(BCryptPasswordEncoder.class);

        @Override
        public void verifyUsername(String username) {
            // 邮箱格式
            AssertUtil.isTrue(Validator.isEmail(username), GlobalResult.EMAIL_NOT_CORRECT);
        }

        @Override
        public String createAuthorizeCode(OauthVo oauthVo) {
            // 用户名
            String username = oauthVo.getUsername();
            // 邮箱格式
            AssertUtil.isTrue(Validator.isEmail(username), GlobalResult.EMAIL_NOT_CORRECT);
            // 用户名的邮箱验证码redis key
            String mailCodeRedisKey = GlobalConstant.mailCodeRedisKey(username);
            // 获取验证码
            String redisCode = redisTemplate.opsForValue().get(mailCodeRedisKey);
            // 删除只能使用一次
            redisTemplate.delete(mailCodeRedisKey);
            // 验证码是否正确
            AssertUtil.equals(redisCode, oauthVo.getPassword(), GlobalResult.VALIDATE_CODE_NOT_RIGHT);
            // 查询用户信息
            UserEntity userEntity = userService.findOneByMail(username).orElseThrow(GlobalResult.EMAIL_NOT_FOUNT::boom);
            // 存储用户信息并返回授权码
            return redisAuthorizeCodeService.store(oauthVo.getClientId(), userEntity);
        }

        @Override
        public Result<BaseEntity> createCallback(OauthVo oauthVo, ClientEntity client) {
            // 验证邮箱授权码登录,存储用户信息在authCode中
            String authorizeCode = this.createAuthorizeCode(oauthVo);
            // 回调URL格式
            String redirectUriFormat = "{}?code={}&responseType={}&state={}";
            // 格式化
            String redirectUri = StrUtil.format(redirectUriFormat, oauthVo.getRedirectUri()
                    , authorizeCode, GlobalConstant.MAIL, oauthVo.getState());
            // 返回结果
            return Result.ok(GrantCodeVo.builder()
                    .redirectUri(redirectUri)
                    .build());
        }

        @Override
        public Result<BaseEntity> createAccessToken(CreateTokenReq createTokenReq, ClientEntity client) {
            // 私匙不正确
            if (!passwordEncoder.matches(createTokenReq.getClientSecret(), client.getClientSecret())) {
                throw GlobalResult.OAUTH_FAIL.boom();
            }
            // 根据邮箱验证码获取用户信息
            UserEntity entity = redisAuthorizeCodeService.consume(createTokenReq.getAuthorizeCode());
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
        private final RedisAuthorizeCodeService redisAuthorizeCodeService = SpringUtil.getBean(RedisAuthorizeCodeService.class);
        /**
         * redis令牌服务
         */
        private final RedisTokenService redisTokenService = SpringUtil.getBean(RedisTokenService.class);
        /**
         * 密码工具
         */
        private final BCryptPasswordEncoder passwordEncoder = SpringUtil.getBean(BCryptPasswordEncoder.class);
        /**
         * 用户服务
         */
        private final UserService userService = SpringUtil.getBean(UserService.class);

        @Override
        public void verifyUsername(String username) {
            // 手机格式
            AssertUtil.isTrue(Validator.isMobile(username), GlobalResult.MOBILE_NOT_CORRECT);
        }

        @Override
        public String createAuthorizeCode(OauthVo oauthVo) {
            // 用户名
            String username = oauthVo.getUsername();
            // 手机格式
            AssertUtil.isTrue(Validator.isMobile(username), GlobalResult.MOBILE_NOT_CORRECT);
            // 查询用户信息
            UserEntity userEntity = userService.findOneByMobile(username)
                    .orElseThrow(GlobalResult.MOBILE_NOT_FOUNT::boom);
            // 密码不正确
            AssertUtil.isTrue(passwordEncoder.matches(oauthVo.getPassword(), userEntity.getPassword()),
                    GlobalResult.PASSWORD_NOT_RIGHT);
            // 存储用户信息和授权码
            return redisAuthorizeCodeService.store(oauthVo.getClientId(), userEntity);
        }

        @Override
        public Result<BaseEntity> createCallback(OauthVo oauthVo, ClientEntity client) {
            // 文本密码授权登录,存储用户信息在authCode中,直接取出来返回
            String authorizeCode = this.createAuthorizeCode(oauthVo);
            // 消费授权码,获取用户信息
            UserEntity userEntity = redisAuthorizeCodeService.consume(authorizeCode);
            // 私匙不正确
            if (!passwordEncoder.matches(oauthVo.getClientSecret(), client.getClientSecret())) {
                throw GlobalResult.OAUTH_FAIL.boom();
            }
            // 生成token
            AccessToken accessToken = redisTokenService.createAccessToken(userEntity, client);
            return Result.ok(accessToken);
        }

        @Override
        public Result<BaseEntity> createAccessToken(CreateTokenReq createTokenReq, ClientEntity clientEntity) {
            throw GlobalResult.OAUTH_FAIL.boom();
        }
    };

    /**
     * 验证用户名格式是否正确
     *
     * @param username 用户名
     */
    public abstract void verifyUsername(String username);

    /**
     * 验证用户名与密码
     * 密码可以是邮箱验证码,文本密码,短信验证码等
     *
     * @param oauthVo 授权信息
     * @return String
     */
    public abstract String createAuthorizeCode(OauthVo oauthVo);

    /**
     * 根据用户名加密码/验证码判断授权是否成功
     *
     * @param oauthVo 参数
     * @param client  客户端信息
     * @return Result<BaseEntity>
     */
    public abstract Result<BaseEntity> createCallback(OauthVo oauthVo, ClientEntity client);

    /**
     * 授权校验成功后生成用户TOKEN
     *
     * @param createTokenReq 参数
     * @param client         客户端信息
     * @return Result<BaseEntity>
     */
    public abstract Result<BaseEntity> createAccessToken(CreateTokenReq createTokenReq, ClientEntity client);

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
