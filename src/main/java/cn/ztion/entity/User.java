package cn.ztion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    private Long qq;
    private String cookie;
    private List<GameRole> role;

    public User(Long qq, String cookie) {
        this.qq = qq;
        this.cookie = cookie;
    }
}
