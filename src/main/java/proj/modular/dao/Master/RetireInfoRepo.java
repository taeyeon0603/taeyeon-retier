package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.RetireInfo;
import proj.modular.vo.index.*;

import java.util.List;

@Repository
public interface RetireInfoRepo extends JpaRepository<RetireInfo, String>, JpaSpecificationExecutor<RetireInfo> {
  List<RetireInfo> findAllByIsDelIsFalseAndTypeEquals(int type);
  @Query("select new proj.modular.vo.index.DistCalcVo(r.idCard, r.retireDate, r.workDate, r.joinPartyDate, r.deptId, r.manageDeptId, r.residenceType) from RetireInfo r where r.isDel = false and type = :type")
  List<DistCalcVo> getAgeCalcList(@Param("type") int type);
  @Query(value = "select count(*) as total, count(case when gender = '男' then 1 else null end) as male, count(case when gender = '女' then 1 else null end) as female from retire_info where is_del = 0 and type = 0", nativeQuery = true)
  GenderDistVo getGenderDist();
  @Query(value = "select count(case when political_identity = '党员' then 1 else null end) as party," +
      " count(case when political_identity = '外支部' then 1 else null end) as outerBranch," +
      " count(case when political_identity = '群众' then 1 else null end) as normal from retire_info where is_del = 0 and type = 0", nativeQuery = true)
  PartyCountVo getPartyCountVo();
  @Query(value = "select count(case when is_life_hard = 1 then 1 else null end) as lifeHard, count(case when is_lonely_old_man = 1 then 1 else null end) as lonelyOldMan, count(case when is_master_and_apprentice = '1' then 1 else null end) as masterAndApprentice from retire_info where is_del = 0 and type = 0", nativeQuery = true)
  SpecialCountVo getSpecialCountVo();
  @Query("select deptName as name, count(r) as value from RetireInfo r where isDel = 0 and type = 0 and deptName is not null and deptName <> '' and deptName not like '%派出所%' group by deptName order by deptName")
  List<LabelValueVo> getFunctionalBrigade();
  @Query("select deptName as name, count(r) as value from RetireInfo r where isDel = 0 and type = 0 and deptName like '%派出所%' group by deptName order by deptName")
  List<LabelValueVo> getPoliceStation();
//  @Query("select ri.policeStation as name, count(ri) as value from RetireInfo r left join ResidenceInfo ri on r.residenceRefId = ri.id where r.isDel = 0 and type = 0 and ri.policeStation is not null group by ri.policeStation order by ri.policeStation")
//  List<LabelValueVo> getResidencePoliceDist();

  @Query("select r.manageDept as name, count(r) as value from RetireInfo r where r.isDel = 0 and type = 0 and r.manageDept is not null group by r.manageDept order by r.manageDept")
  List<LabelValueVo> getResidencePoliceDist();
  @Query("select r.party as name, count(r) as value from RetireInfo r where isDel = 0 and type = 0 and party is not null and party <> '' group by party order by locate(substring(r.party, 2, 1), '一二三四五六七八九十')")
  List<LabelValueVo> getPartyBranch();
  @Query("select r.activeRate as name, count(r) as value from RetireInfo r where isDel = 0 and type = 0 and r.activeRate is not null group by r.activeRate order by r.activeRate")
  List<LabelValueVo> activeRateDist();

  RetireInfo findByIdCard(String idCard);
}
