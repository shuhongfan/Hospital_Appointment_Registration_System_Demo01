package com.shf.yygh.hosp.repository;

import com.shf.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    /**
     * 判断是否存在数据
     * @param hoscode
     * @return
     */
    Hospital getHospitalByHoscode(String hoscode);

    /**
     * 根据医院名称获取医院列表
     */
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
