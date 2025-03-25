package com.bubble.buubleforprofessor.user.service.impl;



import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestCreateDto;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestDto;
import com.bubble.buubleforprofessor.user.entity.Professor;
import com.bubble.buubleforprofessor.university.entity.University;
import com.bubble.buubleforprofessor.user.entity.Role;
import com.bubble.buubleforprofessor.user.entity.User;
import com.bubble.buubleforprofessor.user.repository.ProfessorRepository;
import com.bubble.buubleforprofessor.user.repository.RoleRepository;
import com.bubble.buubleforprofessor.user.repository.UserRepository;
import com.bubble.buubleforprofessor.user.service.ProfessorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    //todo fetch type eager로 가져오면됨. queryDSL과 성능비교? ngrinder
//    교수 승인 요청 리스트 반환
    @Override
    public Page<ApprovalRequestDto> getApproveRequests(Pageable pageable) {
        return professorRepository.findAllByIsApprovedFalse(pageable)
                .map(professor -> ApprovalRequestDto.builder()
                        .userId(Optional.ofNullable(professor.getUser()).map(User::getId).orElse(null))
                        .universityName(Optional.ofNullable(professor.getUser())
                                .map(User::getUniversity)
                                .map(University::getName)
                                .orElse(null))
                        .department(professor.getDepartment())
                        .professorNum(professor.getProfessorNum())
                        .professorName(Optional.ofNullable(professor.getUser()).map(User::getName).orElse(null))
                        .email(Optional.ofNullable(professor.getUser()).map(User::getEmail).orElse(null))
                        .build());
    }

//승인 수락하면 true로 바꿔주며 승인처리
    @Override
    public void setApprovalStatus(UUID userId) {
        Professor professor = professorRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        Optional<Role> role = roleRepository.findByName("ROLE_PROFESSOR");
        if (role.isEmpty()) {
            throw new CustomException(ErrorCode.NON_EXISTENT_ROLE);
        }

        user.modifyRole(role.get());

        userRepository.save(user);

        professor.approve();
        professorRepository.save(professor);
    }
//  승인거절하면 교수데이터 삭제
    @Override
    public void deleteApprovalById(UUID userId) {

        if(!professorRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.NON_EXISTENT_USER);
        }

        professorRepository.deleteById(userId);
    }
//  교수 삭제 후 일반유저로 변경
    @Override
    public void deleteById(UUID userId) {

        if(!professorRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.NON_EXISTENT_USER);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        Optional<Role> role= roleRepository.findByName("ROLE_USER");

        if(role.isEmpty()) {
            throw new CustomException(ErrorCode.NON_EXISTENT_ROLE);
        }
        user.modifyRole(role.get());

        professorRepository.deleteById(userId);
        userRepository.save(user);
    }
//  교수승인 요청하면 일단 교수데이터 저장.
    @Override
    public void createProfessor(UUID userId, ApprovalRequestCreateDto approvalRequestCreateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        if(professorRepository.existsById(userId)) {
           throw new CustomException(ErrorCode.EXISTENT_PROFESSOR);
        }

        Professor professor = Professor.builder()
                .user(user)
                .description(approvalRequestCreateDto.getDescription())
                .isApproved(false)
                .professorNum(approvalRequestCreateDto.getProfessorNum())
                .department(approvalRequestCreateDto.getDepartment())
                .build();
        professorRepository.save(professor);
    }
}
