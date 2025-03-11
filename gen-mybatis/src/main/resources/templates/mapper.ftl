<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperNamespace}">
    <resultMap id="BaseResultMap" type="${entityNamespace}">
        <#list baseResultMap as ci>
        <#if ci.pk>
        <id column="${ci.column}" jdbcType="${ci.jdbcType?upper_case}" property="${ci.property}"/>
        <#else>
        <result column="${ci.column}" jdbcType="${ci.jdbcType?upper_case}" property="${ci.property}"/>
        </#if>
        </#list>
    </resultMap>

    <sql id="Base_Column_List">
        ${agile}
    </sql>

    <select id="selectAll" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from `${table}`
    </select>

    <select id="selectByFilter" parameterType="${entityNamespace}" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from `${table}`
        <where>
            <#list baseResultMap as ci>
            <if test="${ci.property} != null">
               AND `${ci.column}` = ${r'#{'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}
            </if>
            </#list>
        </where>
    </select>

    <select id="selectOneByFilter" parameterType="${entityNamespace}" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from `${table}`
        <where>
            <#list baseResultMap as ci>
            <if test="${ci.property} != null">
               AND `${ci.column}` = ${r'#{'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}
            </if>
            </#list>
        </where>
        limit 1
    </select>

    <select id="selectById" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from `${table}`
        <#list baseResultMap as ci>
        <#if ci.pk>
        where `${ci.column}` = ${r'#{'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}
        </#if>
        </#list>
    </select>

    <delete id="deleteById" resultType="int">
        delete from `${table}`
        <#list baseResultMap as ci>
        <#if ci.pk>
        where `${ci.column}` = ${r'#{'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}
        </#if>
        </#list>
    </delete>

    <update id="update" parameterType="${entityNamespace}" resultType="int">
        update `${table}`
        <set>
            <#list baseResultMap as ci>
            <#if !ci.pk>
            <if test="${ci.property} != null">
                `${ci.column}` = ${r'#{'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}<#if ci_has_next>,</#if>
            </if>
            </#if>
            </#list>
        </set>
        <#list baseResultMap as ci>
        <#if ci.pk>
        where `${ci.column}` = ${r'#{'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}
        </#if>
        </#list>
    </update>

    <insert id="insert" parameterType="${entityNamespace}" resultType="int">
        insert into `${table}`
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#list baseResultMap as ci>
            <if test="${ci.property} != null">
                `${ci.column}`<#if ci_has_next>,</#if>
            </if>
            </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <#list baseResultMap as ci>
            <if test="${ci.property} != null">
                ${r'#{'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}<#if ci_has_next>,</#if>
            </if>
            </#list>
        </trim>
    </insert>

    <select id="count" parameterType="${entityNamespace}" resultType="long">
        select count(*)
        from `${table}`
        <where>
            <#list baseResultMap as ci>
            <#if ci.pk>
            AND `${ci.column}` = ${r'#{'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}
            </#if>
            </#list>
        </where>
        limit 1
    </select>

    <insert id="batchInsert" parameterType="${entityNamespace}" resultType="int">
        insert into `${table}`
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#list baseResultMap as ci>
            `${ci.column}`<#if ci_has_next>,</#if>
            </#list>
        </trim>
        values
        <foreach collection="list" item="item" separator=",">
            (<#list baseResultMap as ci>${r'#{item.'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}<#sep>,
            </#list>)
        </foreach>
    </insert>

    <update id="batchUpdate" parameterType="${entityNamespace}" resultType="int">
        insert into `${table}`
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#list baseResultMap as ci>
            `${ci.column}`<#if ci_has_next>,</#if>
            </#list>
        </trim>
        values
        <foreach collection="list" item="item" separator=",">
            (<#list baseResultMap as ci>${r'#{item.'}${ci.property}, jdbcType=${ci.jdbcType?upper_case}}<#sep>,
            </#list>)
        </foreach>
        ON DUPLICATE KEY UPDATE
        <#list baseResultMap as ci>
        <#if !ci.pk>
        `${ci.column}` = VALUES(`${ci.column}`)<#if ci_has_next>,</#if>
        </#if>
        </#list>
    </update>
</mapper>