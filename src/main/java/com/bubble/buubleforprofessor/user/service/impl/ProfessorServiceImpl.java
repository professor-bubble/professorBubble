package com.bubble.buubleforprofessor.user.service.impl;



import com.bubble.buubleforprofessor.user.dto.ApprovalRequestDto;
import com.bubble.buubleforprofessor.user.entity.Professor;
import com.bubble.buubleforprofessor.university.entity.University;
import com.bubble.buubleforprofessor.user.entity.User;
import com.bubble.buubleforprofessor.user.repository.ProfessorRepository;
import com.bubble.buubleforprofessor.user.service.ProfessorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//todo RequiredArgs 와 getter(lombok) 인식안되는듯
@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    //todo fetch type eager로 가져오면됨. queryDSL과 성능비교? ngrinder
    @Override
    public List<ApprovalRequestDto> getApproveRequests() {
        List<Professor> professors = professorRepository.findAllByIsApprovedFalse();

        return professors.stream()
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
                        .build())
                .toList();
    }
}
