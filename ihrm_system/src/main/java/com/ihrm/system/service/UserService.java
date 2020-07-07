package com.ihrm.system.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 1.保存用户
     */
    public void save(User user) {
        //设置主键的值
        String id = idWorker.nextId()+"";
        user.setId(id);
        //调用dao保存用户
        user.setPassword("123456");
        user.setEnableState(1);
        userDao.save(user);
    }

    /**
     * 2.更新部门
     */
    public void update(User user) {
        //1.根据id查询用户
        User targer = userDao.findById(user.getId()).get();
        //2.设置用户属性
        targer.setUsername(user.getUsername());
        targer.setPassword(user.getPassword());
        targer.setDepartmentId(user.getDepartmentId());
        targer.setDepartmentName(user.getDepartmentName());
        //3.更新用户
        userDao.save(user);
    }

    /**
     * 3.根据id查询部门
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 4.查询全部部门列表
     */
    public Page<User> findAll(Map<String,Object> map, int page, int size) {
        Specification<User> spec = new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                if(!StringUtils.isEmpty(map.get("departmentId"))){
                    list.add(criteriaBuilder.equal(root.get("company").as(String.class),(String)map.get("departmentId")));
                }
                //根据请求的hasDept进行判断
                if (!StringUtils.isEmpty(map.get("hasDept"))){
                    if ("0".equals((String) map.get("hasDept")))   {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }else{
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<User> pageUser = userDao.findAll(spec, PageRequest.of(page-1, size));
        return pageUser;
    }

    /**
     * 5.根据id删除部门
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }
}
