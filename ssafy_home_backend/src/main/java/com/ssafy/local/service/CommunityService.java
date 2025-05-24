package com.ssafy.local.service;

import java.util.List;

import com.ssafy.local.dto.CommentDto;
import com.ssafy.local.dto.PostDto;

public interface CommunityService {
	//게시
	int createPost(PostDto postDto);
	List<PostDto> getAllPosts();
	boolean updatePost(PostDto postDto);
	boolean deletePost(int boardId);
	PostDto getPost(int boardId);
	//댓글
	boolean createComment(CommentDto commentDto);
	List<CommentDto> getAllCommentsByPostId(int boardId);
	int numberOfComments(int boardId); //댓글
	boolean deleteComment(int commentId);
	

}
