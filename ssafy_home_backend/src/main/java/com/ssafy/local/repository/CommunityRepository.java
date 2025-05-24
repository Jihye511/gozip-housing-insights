package com.ssafy.local.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.local.dto.CommentDto;
import com.ssafy.local.dto.PostDto;

@Mapper
public interface CommunityRepository {
	int createBoard(PostDto postDto) throws Exception;
	List<PostDto> getAllPosts();
	PostDto getPost(int boardId);
	int deletePost(int boardId);
	int updateBoard(PostDto postDto);
	
	int createComment(CommentDto commentDto);
	List<CommentDto> selectCommentsByBoardId(int boardId);
	int countComments(int boardId);
	int deleteComment(int commentId);
}
