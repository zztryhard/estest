package es.test.base.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author 旺旺小学酥
 * @Time 2017/12/6
 */
public abstract class User extends BaseEntity<User> {

    @JsonProperty("name")
    public abstract String getNickName();

    public abstract void setNickName(final String nickName);

}
