package com.github.benshi.example.model.mapper;

import com.github.benshi.example.model.User;
import java.lang.Long;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * MyBatis Mapper for UserMapper
 * Generated automatically by AutoGenMapperProcessor
 */
@Mapper
public interface UserMapper {
    /**
     * 查询所有User记录
     * @return 所有记录列表
     */
    List<User> selectAll();

    /**
     * 查询User记录
     * @return 所有条件查询列表
     */
    List<User> selectByFilter(User filters);

    /**
     * 查询User记录
     * @return 所有条件查询信息
     */
    User selectOneByFilter(User filters);

    /**
     * 根据ID查询User
     * @param id 主键ID
     * @return 对应的记录，如果不存在则返回null
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据ID删除User
     * @param id 主键ID
     * @return 受影响的行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新User记录
     * @param entity 要更新的实体对象
     * @return 受影响的行数
     */
    int update(User entity);

    /**
     * 插入新的User记录
     * @param entity 要插入的实体对象
     * @return 受影响的行数
     */
    int insert(User entity);

    /**
     * 统计User记录数
     * @param filters 查询条件
     * @return 记录数
     */
    long count(User filters);

    /**
     * 批量插入User记录
     * @param entities 要插入的实体对象
     * @return 受影响的行数
     */
    int batchInsert(List<User> entities);

    /**
     * 批量更新User记录
     * @param entities 要更新的实体对象
     * @return 受影响的行数
     */
    int batchUpdate(List<User> entities);
}
