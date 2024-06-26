package com.bu200.mypage.repository;

import com.bu200.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


//mypage의 project를 찾아오기 위한 리포지토리   1개의 프로젝트만 진행한다고 가정
public interface FindMyPageProjectRepository extends JpaRepository<Project, Long> {
    //우선순위가 가장 높은, projectOpenStatus가 true인 내 프로젝트를 오름차순으로 찾아와라
    List<Project> findByAccount_AccountIdAndProjectOpenStatusIsTrueOrderByProjectPriorityDesc(String accountId);

    @Query("select p from Project p " +
            "join p.accountProjects ap " +
            "where ap.account.accountId = :accountId " +
            "and p.projectName like %:keyword%")
    List<Project> findProject(String accountId, String keyword);
}
