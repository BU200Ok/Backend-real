package com.bu200.mypage.service.Dtos;

import com.bu200.login.entity.Team;
import lombok.*;
import org.modelmapper.internal.bytebuddy.build.ToStringPlugin;

import java.time.LocalDateTime;
import java.util.Date;

//account, project, annual
//후에 여러 공지사항들도 추가
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class MainPageDTO {
    //acount
    private Long accountCode;
    private String accountName;
    private String accountAddress;
    private String accountEmail;
    private Date accountJoinDate;
    private String accountPosition;
    private String accountRole;
    private Team team;
    private String accountId;
    private Date accountResignDate;

    //project
    private Long projectCode;
    private String projectName;
    private Date projectStart;
    private Date projectEnd;
    private Integer projectPriority;
    private String projectOperations;   //inprogress
    private String projectDescription;  //todo
                                        //done은 뭐가 들어가지...

    //마지막 근태 정보
    private Long attendanceCode;
    private LocalDateTime attendanceLeaveWork;     //아직 출근만 했을 시 null
    private LocalDateTime attendanceGoWork;
    private String attendanceStatus;                //출근 시 "출근" 퇴근 시 "퇴근"
}
