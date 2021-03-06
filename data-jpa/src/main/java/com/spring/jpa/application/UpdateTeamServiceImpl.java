package com.spring.jpa.application;

import javax.transaction.Transactional;

import com.spring.jpa.domain.Team;
import com.spring.jpa.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class UpdateTeamServiceImpl implements UpdateTeamService {

	@Autowired
	private TeamRepository teamRepository;

	public void setTeamRepository(TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
	}

	@Transactional
	@Override
	public void udpateName(Long teamId, String newName) {
		Team team = teamRepository.findById(teamId);
		if (team == null)
			throw new TeamNotFoundException("No Team for ID[" + teamId + "]");
		System.out.println("변경 전: " + team.getName());
		int updated = teamRepository.updateName(teamId, newName);
		System.out.println("변경 개수: " + updated);
		System.out.println("변경 후 :" + team.getName());
	}

}
