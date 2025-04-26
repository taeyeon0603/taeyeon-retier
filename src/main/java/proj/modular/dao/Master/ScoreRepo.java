package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Score;

@Repository
public interface ScoreRepo extends JpaRepository<Score, Integer> {

}
