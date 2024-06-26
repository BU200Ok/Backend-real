package com.bu200.forum.controller;
import com.bu200.common.response.ResponseDTO;
import com.bu200.forum.dto.ForumDTO;
import com.bu200.common.response.Tool;
import com.bu200.forum.entity.Forum;
import com.bu200.forum.service.ForumService;
import com.bu200.mypage.service.DTO.MainPageDTO;
import com.bu200.mypage.service.FindMyPageMainService;
import com.bu200.security.dto.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;

@RequestMapping("/forum")
@Controller
public class ForumController {
    private final ForumService forumService;
    private final FindMyPageMainService findMyPageMainService;
    private final Tool tool;

    public ForumController(ForumService forumService, FindMyPageMainService findMyPageMainService, Tool tool) {
        this.forumService = forumService;
        this.findMyPageMainService = findMyPageMainService;
        this.tool = tool;
    }

    //공지사항(관리자)
    @GetMapping("/public")
    public ResponseEntity<ResponseDTO> getAdminForums(@AuthenticationPrincipal CustomUserDetails user) {
        try {
            List<ForumDTO> forums = forumService.findForumsByAccountRole();
            return tool.res(HttpStatus.OK, "관리자 게시글 목록.", forums);
        } catch (Exception e) {
            return tool.res(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    // 부서별 게시판
    @GetMapping("/department")
    public ResponseEntity<ResponseDTO> getDepartmentForums(@AuthenticationPrincipal CustomUserDetails user, @PageableDefault(page = 1) Pageable pageable) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            List<ForumDTO> forums = forumService.getAllForums();
            return tool.res(HttpStatus.OK, "Admin의 모든 포럼 데이터", forums);
        } else {
            String departmentName = forumService.getDepartmentName(user.getUsername());
            List<ForumDTO> forums = forumService.getForumsByDepartment(departmentName);
            return tool.res(HttpStatus.OK, "해당 부서의 포럼 데이터", forums);
        }
    }

    //개인정보 가져오기(로그인한 사용자)
    @GetMapping("/myinfo")
    public ResponseEntity<ResponseDTO> myInfoLoading(@AuthenticationPrincipal CustomUserDetails user) {
        System.out.println(user.getUsername()+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        MainPageDTO mainPageDTO = findMyPageMainService.FindAccountData(user.getUsername());
        System.out.println("myinfo입니다. \n\n\n\n\n\n\n\n\n\n"
        +mainPageDTO.toString());
        return tool.res(HttpStatus.OK, "mainpageDTO입니다.", mainPageDTO);
    }


    // 게시글 작성
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createForum(@RequestBody ForumDTO forumDTO) {
        if (forumDTO.getAccountCode() == null) {
            return tool.res(HttpStatus.BAD_REQUEST, "accountcode 없음.", null);
        }
        try {
            Forum savedForum = forumService.saveForum(forumDTO);
            return tool.res(HttpStatus.OK, "forum 내용입니다.", savedForum);
        } catch (IllegalArgumentException e) {
            return tool.res(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    // 사용자가 작성한 게시글 목록 확인
    @GetMapping("/my-posts")
    public ResponseEntity<ResponseDTO> getMyPostList(@AuthenticationPrincipal CustomUserDetails user) {
        try {
            List<ForumDTO> myPosts = forumService.findForumsByAccountCode(user.getUsername());
            return tool.res(HttpStatus.OK, "내가 작성한 게시글 목록입니다.", myPosts);
        } catch (Exception e) {
            return tool.res(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    // 상세 내용 보기
    @GetMapping("/{forumCode}")
    public ResponseEntity<ResponseDTO> getForumDetail(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long forumCode) {
        System.out.println(user.getCode());
        try {
            ForumDTO forum = forumService.getForumByForumCode(forumCode);
            return tool.res(HttpStatus.OK, "게시글 상세 내용입니다.", forum);
        } catch (Exception e) {
            return tool.res(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }


    //게시글 삭제
    @DeleteMapping("/delete/{forumCode}")
    public ResponseEntity<ResponseDTO> deleteForum(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long forumCode) {
        try {
            forumService.deleteForum(forumCode, user.getUsername());
            return tool.res(HttpStatus.OK, "게시글이 삭제되었습니다.", null);
        } catch (IllegalArgumentException e) {
            return tool.res(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    //게시글 수정
    @PutMapping("/edit/{forumCode}")
    public ResponseEntity<ResponseDTO> editForum(@AuthenticationPrincipal CustomUserDetails user, @RequestBody ForumDTO forumDTO, @PathVariable Long forumCode) {
        try {
            forumService.updateForum(forumCode, user.getUsername(), forumDTO);
            return tool.res(HttpStatus.OK, "게시글이 수정되었습니다.", null);
        } catch (IllegalArgumentException e) {
            return tool.res(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @GetMapping("/pages")
    public ResponseEntity<ResponseDTO> getForumsPage(@PageableDefault(size = 8) Pageable pageable) {
        Page<ForumDTO> forumsPage = forumService.getForumsPage(pageable);
        return tool.res(HttpStatus.OK, "포럼 페이지 데이터", forumsPage);
    }
}



