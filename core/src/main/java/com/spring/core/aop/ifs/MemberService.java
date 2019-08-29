package com.spring.core.aop.ifs;

import com.spring.core.aop.domain.MemberRegRequest;
import com.spring.core.aop.domain.UpdateInfo;

public interface MemberService {
    boolean update(String memberId, UpdateInfo info);
    void regist(MemberRegRequest memberRegReq);
}
