package org.prgrms.kdt.member.controller;

import org.prgrms.kdt.member.controller.dto.CreateMemberApiRequest;
import org.prgrms.kdt.member.controller.mapper.ControllerMemberMapper;
import org.prgrms.kdt.member.service.dto.MemberResponses;
import org.prgrms.kdt.member.service.MemberService;
import org.springframework.stereotype.Component;

@Component
public class MemberController {
    private final MemberService memberService;
    private final ControllerMemberMapper mapper;

    public MemberController(MemberService memberService, ControllerMemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    public void createMember(CreateMemberApiRequest request) {
        memberService.createMember(mapper.convertRequest(request));
    }

    public MemberResponses findAllMember() {
        return memberService.findAllMember();
    }

    public MemberResponses findAllBlackMember() {
        return memberService.findAllBlackMember();
    }
}