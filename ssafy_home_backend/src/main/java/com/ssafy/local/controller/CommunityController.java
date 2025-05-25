package com.ssafy.local.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.ssafy.local.dto.CommentDto;
import com.ssafy.local.dto.PostDto;
import com.ssafy.local.service.CommunityService;
import com.ssafy.local.service.HouseDealService;
import com.ssafy.local.service.HouseInfoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {
	private final CommunityService communityService;
	
	@PostMapping
	public int postBoard(@RequestBody PostDto postDto, Authentication authentication) {
	    String username = authentication.getName(); 
	    postDto.setUser_id(username);
	    return communityService.createPost(postDto);
	}
	
	// 2. 게시글 전체 조회
    @GetMapping
    public List<PostDto> getAllPosts() {
        return communityService.getAllPosts();
    }

    // 3. 게시글 단건 조회
    @GetMapping("/{boardId}")
    public PostDto getPost(@PathVariable int boardId) {
        return communityService.getPost(boardId);
    }

    // 4. 게시글 수정
    @PutMapping("/{boardId}")
    public boolean updatePost(@PathVariable int boardId, @RequestBody PostDto postDto, Authentication authentication) {
        String username = authentication.getName();
        postDto.setBoard_id(boardId);     // URL에서 온 boardId 설정
        postDto.setUser_id(username);     // 로그인 사용자 ID 설정
        return communityService.updatePost(postDto);
    }

    // 5. 게시글 삭제
    @DeleteMapping("/{boardId}")
    public boolean deletePost(@PathVariable int boardId, Authentication authentication) {
        // 💡 필요한 경우 사용자 인증 후 작성자와 일치 여부 확인 가능
        return communityService.deletePost(boardId);
    }
    
 // 1. 댓글 작성
    @PostMapping("/comment")
    public boolean createComment(@RequestBody CommentDto commentDto, Authentication authentication) {
        String username = authentication.getName();
        commentDto.setUser_id(username); // 로그인 유저 ID 설정
        return communityService.createComment(commentDto);
    }

    // 2. 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public boolean deleteComment(@PathVariable int commentId, Authentication authentication) {
        // 💡 여기서 인증된 유저와 작성자가 같은지 비교하는 로직 추가 가능
        return communityService.deleteComment(commentId);
    }

    // 3. 특정 게시글의 댓글 조회
    @GetMapping("/comment/{boardId}")
    public List<CommentDto> getCommentsByBoardId(@PathVariable int boardId) {
        return communityService.getAllCommentsByPostId(boardId);
    }

}