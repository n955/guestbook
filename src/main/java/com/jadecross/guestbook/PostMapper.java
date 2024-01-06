package com.jadecross.guestbook;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.jadecross.guestbook.Post;

@Mapper
public interface PostMapper {
	List<Post> selectAll();

	void insert(Post post);
}
