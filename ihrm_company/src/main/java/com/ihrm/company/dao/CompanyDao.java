package com.ihrm.company.dao;

import com.ihrm.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//需要继承jpa的两个接口
//JpaRepository和JpaSpecificationExecutor
public interface CompanyDao extends JpaRepository<Company,String> , JpaSpecificationExecutor<Company> {
}
