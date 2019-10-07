package com.spring.memberdb.member.domain;

import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

	Member findOne(Long id);

	Member findByUserId(String userId);

	Member findByEmail(String email);

	Member save(Member m);

}
