package com.mzlalal.oauth2.service.oauth2;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.dto.ClientEntity;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.entity.oauth2.req.CreateTokenReq;
import com.mzlalal.base.entity.oauth2.req.OauthReq;
import com.mzlalal.base.entity.oauth2.vo.AccessToken;
import com.mzlalal.base.entity.oauth2.vo.RedirectUriVo;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.oauth2.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 授权方式枚举类
 * 登录后使用此枚举创建用户TOKEN
 * auth   ->  response  ->   provide  -> createToken 或者 createToken code
 * 授权开始 ->  授权方式  -> 授权校验方式 -> 返回令牌或者授权码
 *
 * @author Mzlalal
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
         * 用户服务
         */
        private final UserService userService = SpringUtil.getBean(UserService.class);
        /**
         * redis授权码服务
         */
        private final StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);

        @Override
        public void verifyUsername(String username) {
            // 邮箱格式
            AssertUtil.isTrue(Validator.isEmail(username), GlobalResult.EMAIL_NOT_CORRECT);
        }

        @Override
        public String createAuthorizeCode(OauthReq req) {
            // 邮箱
            String mail = req.getUsername();
            // 验证邮箱格式
            this.verifyUsername(mail);
            // 用户名的邮箱验证码redis key
            String mailCodeRedisKey = GlobalConstant.clientMailCode(req.getClientKey(), mail);
            // 获取验证码
            String redisCode = redisTemplate.opsForValue().get(mailCodeRedisKey);
            // 删除只能使用一次
            redisTemplate.delete(mailCodeRedisKey);
            // 验证码是否正确
            AssertUtil.equals(redisCode, req.getPassword(), GlobalResult.VALIDATE_CODE_NOT_RIGHT);
            // 查询用户信息
            UserEntity userEntity = userService.queryOneByMail(mail)
                    .orElseThrow(GlobalResult.EMAIL_NOT_FOUNT::boom);
            // 存储用户信息并返回授权码
            return redisAuthorizeCodeService.store(req.getClientKey(), userEntity);
        }
    },
    /**
     * 密码授权登录
     */
    PASSWORD() {
        /**
         * redis授权码服务
         */
        private final RedisAuthorizeCodeService redisAuthorizeCodeService = SpringUtil.getBean(RedisAuthorizeCodeService.class);

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
        public String createAuthorizeCode(OauthReq req) {
            // 手机号
            String mobile = req.getUsername();
            // 手机格式
            this.verifyUsername(mobile);
            // 查询用户信息
            UserEntity userEntity = userService.queryOneByMobile(mobile)
                    .orElseThrow(GlobalResult.MOBILE_NOT_FOUNT::boom);
            // 密码不正确
            AssertUtil.isTrue(passwordEncoder.matches(req.getPassword(), userEntity.getPassword()),
                    GlobalResult.PASSWORD_NOT_RIGHT);
            // 存储用户信息和授权码
            return redisAuthorizeCodeService.store(req.getClientKey(), userEntity);
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
     * @param req 授权信息
     * @return String
     */
    public abstract String createAuthorizeCode(OauthReq req);

    /**
     * 根据用户名加密码/验证码判断授权是否成功
     *
     * @param req 参数
     * @return Result<BaseEntity>
     */
    public Result<RedirectUriVo> createCallback(OauthReq req) {
        // 验证邮箱授权码登录,存储用户信息在authCode中
        String authorizeCode = this.createAuthorizeCode(req);
        // 回调URL格式
        String redirectUriFormat = "{}?code={}&responseType={}&state={}";
        // 格式化
        String redirectUri = StrUtil.format(redirectUriFormat, req.getRedirectUri()
                , authorizeCode, req.getResponseType(), req.getState());
        // 返回结果
        return Result.ok(RedirectUriVo.builder()
                .redirectUri(redirectUri)
                .indexUri(req.getIndexUri())
                .build());
    }

    /**
     * 授权校验成功后生成用户TOKEN
     *
     * @param createTokenReq 参数
     * @param client         客户端信息
     * @return Result<BaseEntity>
     */
    public Result<AccessToken> createAccessToken(CreateTokenReq createTokenReq, ClientEntity client) {
        // 私匙不正确
        if (!SpringUtil.getBean(BCryptPasswordEncoder.class).matches(createTokenReq.getClientSecret(), client.getClientSecret())) {
            throw GlobalResult.OAUTH_FAIL.boom();
        }
        // 根据邮箱验证码获取用户信息
        UserEntity entity = SpringUtil.getBean(RedisAuthorizeCodeService.class).consume(createTokenReq.getAuthorizeCode());
        // 生成token
        AccessToken accessToken = SpringUtil.getBean(RedisTokenService.class).createAccessToken(entity, client);
        return Result.ok(accessToken);
    }

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
