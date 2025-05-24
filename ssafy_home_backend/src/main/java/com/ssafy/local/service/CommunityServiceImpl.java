package com.ssafy.local.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.local.dto.CommentDto;
import com.ssafy.local.dto.PostDto;
import com.ssafy.local.repository.CommunityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {
	
	private final CommunityRepository repo;
	
	@Override
	public int createPost(PostDto postDto) {
		try {
			int result = repo.createBoard(postDto);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<PostDto> getAllPosts() {
		return repo.getAllPosts();
	}

	@Override
	public boolean updatePost(PostDto postDto) {
		return repo.updateBoard(postDto) >0;
	}

	@Override
	public boolean deletePost(int boardId) {
		return repo.deletePost(boardId)>0;
	}

	@Override
	public PostDto getPost(int boardId) {
		return repo.getPost(boardId);
	}

	@Override
	public boolean createComment(CommentDto commentDto) {
		return repo.createComment(commentDto)>0;
	}

	@Override
	public List<CommentDto> getAllCommentsByPostId(int boardId) {
		return repo.selectCommentsByBoardId(boardId);
	}

	@Override
	public int numberOfComments(int boardId) {
		return repo.countComments(boardId);
	}

	@Override
	public boolean deleteComment(int commentId) {
		return repo.deleteComment(commentId)>0;
	}


}
