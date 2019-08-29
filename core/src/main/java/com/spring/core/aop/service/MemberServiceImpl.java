package com.spring.core.aop.service;

import com.spring.core.aop.domain.Member;
import com.spring.core.aop.domain.MemberRegRequest;
import com.spring.core.aop.domain.UpdateInfo;
import com.spring.core.aop.ifs.MemberService;

import java.util.HashMap;
import java.util.Map;

public class MemberServiceImpl implements MemberService {

    private Map<String, Member> members = new HashMap<>();

    @Override
    public boolean update(String memberId, UpdateInfo info) {
        if (members.containsKey(memberId)) {
            members.put(memberId, new Member(memberId, info.getPassword()));
            return true;
        }
        return false;
    }

    @Override
    public void regist(MemberRegRequest memberRegReq) {
        if (members.containsKey(memberRegReq.getId())) {
            System.out.printf("%s는 존재하는 id 입니다.", memberRegReq.getId());
            return;
        }
        members.put(memberRegReq.getId(), memberRegReq.toMember());
    }
}
