package com.ssamz.jblog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ssamz.jblog.domain.User;

@Mapper
public interface UserMapper {
	
	@Insert("INSERT INTO USER(ID, USERNAME, PASSWORD, EMAIL) VALUES((SELECT NVL(MAX(ID), 0) + 1 FROM USER), #{username}, #{password}, #{email})")
	public void insertUser(User user);

	@Select("SELECT * FROM USER ORDER BY USERNAME DESC")
	public List<User> getUserList();
}
