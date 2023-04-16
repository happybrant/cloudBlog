package com.kongfu.backend.model.vo;

import com.kongfu.backend.util.BlogUtil;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-05-25 17:52:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class LoginToken {
    private static String lexicalXSDBase64Binary = "2D902FDB48454ECF632DAF76B5798A85";

    private static int expireMinutes = 12 * 60;

    private int id;

    private int role;

    private String loginName;

    private Date createTime;

    private Date expiration;

    private String ticket;

    private String jti;

    /**
     * 构造函数，创建LoginToken对象，同时生成Token
     *
     * @param id
     * @param loginName
     */
    public LoginToken(int id, String loginName, int role) {
        this.id = id;
        this.loginName = loginName;
        this.role = role;
        createTicket();
    }

    private void createTicket() {
        SignatureAlgorithm signature = SignatureAlgorithm.HS256;

        long millis = System.currentTimeMillis();
        this.createTime = new Date(millis);

        byte[] bytes = DatatypeConverter.parseBase64Binary(LoginToken.lexicalXSDBase64Binary);
        Key key = new SecretKeySpec(bytes, signature.getJcaName());

        JwtBuilder builder =
                Jwts.builder()
                        .setHeaderParam("typ", "JWT")
                        .claim("id", this.id)
                        .claim("role", this.role)
                        .setIssuer(this.loginName)
                        .setId(BlogUtil.generateUUID())
                        .signWith(signature, key);

        this.expiration = new Date(millis + LoginToken.expireMinutes * 60 * 1000);
        builder.setExpiration(this.expiration).setNotBefore(this.createTime);

        this.ticket = builder.compact();
    }

    public static LoginToken checkTicket(String ticket) {
        LoginToken result = null;

        if (!StringUtils.isEmpty(ticket)) {
            Claims claims;
            try {
                claims =
                        Jwts.parser()
                                .setSigningKey(
                                        DatatypeConverter.parseBase64Binary(LoginToken.lexicalXSDBase64Binary))
                                .parseClaimsJws(ticket)
                                .getBody();
            } catch (ExpiredJwtException e) {
                log.warn(e.getMessage());
                // 不管是否过期，都返回claims对象
                claims = e.getClaims();
            } catch (MalformedJwtException e) {
                // 格式错误直接返回
                log.error(e.getMessage());
                return null;
            }
            if (claims != null) {
                // 获取token唯一标识
                String jti = claims.getId();

                result = new LoginToken();
                result.setId((Integer) claims.get("id"));
                result.setRole((Integer) claims.get("role"));
                result.setLoginName(claims.getIssuer());
                result.setTicket(ticket);
                result.setExpiration(claims.getExpiration());
                result.setCreateTime(claims.getNotBefore());
                result.setJti(jti);
            }
        }

        return result;
    }

    public static Claims resolveTicket(String ticket) {
        Claims claims;
        try {
            claims =
                    Jwts.parser()
                            .setSigningKey(DatatypeConverter.parseBase64Binary(LoginToken.lexicalXSDBase64Binary))
                            .parseClaimsJws(ticket)
                            .getBody();
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            // 不管是否过期，都返回claims对象
            return e.getClaims();
        } catch (MalformedJwtException e) {
            // 格式错误直接返回
            log.error(e.getMessage());
            return null;
        }
        return claims;
    }
}
