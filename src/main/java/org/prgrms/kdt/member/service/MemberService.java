package org.prgrms.kdt.member.service;

import org.prgrms.kdt.member.domain.Member;
import org.prgrms.kdt.member.dao.MemberRepository;
import org.prgrms.kdt.member.domain.MemberStatus;
import org.prgrms.kdt.member.service.dto.CreateMemberRequest;
import org.prgrms.kdt.member.service.dto.MemberResponse;
import org.prgrms.kdt.member.service.dto.MemberResponses;
import org.prgrms.kdt.member.service.mapper.ServiceMemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ServiceMemberMapper mapper;

    public MemberService(MemberRepository memberRepository, ServiceMemberMapper mapper) {
        this.memberRepository = memberRepository;
        this.mapper = mapper;
    }

    @Transactional
    public MemberResponse createMember(CreateMemberRequest request) {
        Member member = mapper.convertMember(request);
        return new MemberResponse(memberRepository.insert(member));
    }

    public MemberResponses findAllBlackMember() {
        return MemberResponses.of(memberRepository.findByStatus(MemberStatus.BLACK));
    }

    public MemberResponses findAllMember() {
        return MemberResponses.of(memberRepository.findAll());
    }
}
