package com.spring.memberdb.member.application;

import com.spring.memberdb.member.domain.Member;
import com.spring.memberdb.member.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class ChangePasswordServiceImpl implements ChangePasswordService {

	@Autowired
	private MemberRepository memberRepository;

	@Transactional
	@Override
	public void changePassword(ChangePasswordRequest req) {
		Member member = memberRepository.findOne(req.getMemberId());
		if (member == null)
			throw new MemberNotFoundException();

		member.changePassword(req.getCurrentPassword(), req.getNewPassword());
	}

}
