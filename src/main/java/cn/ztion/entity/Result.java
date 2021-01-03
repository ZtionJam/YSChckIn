package cn.ztion.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result {
    private String nickname;
    private Reward award;
    private Integer signDay;
    private String signResult;
}
