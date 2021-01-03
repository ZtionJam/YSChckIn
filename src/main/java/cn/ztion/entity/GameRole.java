package cn.ztion.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class GameRole {
    private Long gameUid;
    private String nickname;
    private Integer level;
    private String game_biz;
    private String region;
    private String region_name;
    private String cookie;
    private Integer signDay;
}
