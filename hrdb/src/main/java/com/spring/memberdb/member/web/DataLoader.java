package com.spring.memberdb.member.web;

import com.spring.memberdb.member.domain.Member;
import com.spring.memberdb.member.domain.MemberRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class DataLoader {

	@Autowired
	private MemberRepository memberRepository;

	@Transactional
	public Member loadMember(Long memberId) {
		Member member = memberRepository.findOne(memberId);
		if (member == null)
			return null;
		Hibernate.initialize(member.getLocker());
		return member;
	}
}
