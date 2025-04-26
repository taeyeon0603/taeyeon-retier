package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "score", schema = "retire")
@Setter
@Getter
public class Score {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "retire_id", length = 255)
    private String retireId;

    @Column(name = "date", length = 36)
    private String date;

    @Column(name = "weather", length = 255)
    private String weather;

    @Column(name = "last_score")
    private Short lastScore;

    @Column(name = "indoor_activity_count")
    private Short indoorActivityCount;

    @Column(name = "outdoor_activity_count")
    private Short outdoorActivityCount;

    @Column(name = "reward_count")
    private Short rewardCount;

    @Column(name = "vaccination_count")
    private Short vaccinationCount;

    @Column(name = "remark_count")
    private Short remarkCount;

    @Column(name = "visit_count")
    private Short visitCount;

    @Column(name = "news_count")
    private Short newsCount;

    @Column(name = "score")
    private Short score;


    // 其他字段的getter/setter省略...
}