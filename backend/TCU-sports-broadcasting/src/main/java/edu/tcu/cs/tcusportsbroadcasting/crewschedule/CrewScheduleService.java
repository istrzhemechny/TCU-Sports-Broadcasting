package edu.tcu.cs.tcusportsbroadcasting.crewschedule;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.CrewMemberNotFoundException;
import edu.tcu.cs.tcusportsbroadcasting.crewschedule.dto.CrewListResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.crewschedule.dto.CrewScheduleDto;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.Game;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.GameRepository;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.exception.GameNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CrewScheduleService {

    private final CrewScheduleRepository crewScheduleRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final GameRepository gameRepository;

    public CrewScheduleService(CrewScheduleRepository crewScheduleRepository,
                               CrewMemberRepository crewMemberRepository,
                               GameRepository gameRepository) {
        this.crewScheduleRepository = crewScheduleRepository;
        this.crewMemberRepository = crewMemberRepository;
        this.gameRepository = gameRepository;
    }

    public List<CrewScheduleDto> addCrewSchedule(Long gameId, List<CrewScheduleDto> dtoList) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));

        List<CrewScheduleDto> result = new ArrayList<>();

        for (CrewScheduleDto dto : dtoList) {
            CrewMember member = crewMemberRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new CrewMemberNotFoundException(dto.getUserId()));

            CrewSchedule cs = new CrewSchedule();
            cs.setCrewMember(member);
            cs.setGame(game);
            cs.setPosition(dto.getPosition());
            cs.setReportTime(dto.getReportTime());
            cs.setReportLocation(dto.getReportLocation());

            CrewSchedule saved = crewScheduleRepository.save(cs);

            CrewScheduleDto responseDto = new CrewScheduleDto();
            responseDto.setCrewedUserId(saved.getCrewedUserId());
            responseDto.setUserId(member.getId());
            responseDto.setGameId(game.getGameId());
            responseDto.setPosition(saved.getPosition());
            responseDto.setReportTime(saved.getReportTime());
            responseDto.setReportLocation(saved.getReportLocation());
            responseDto.setFullName(member.getFirstName() + " " + member.getLastName());

            result.add(responseDto);
        }

        return result;
    }

    public CrewListResponseDto findCrewListByGameId(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));

        List<CrewSchedule> scheduled = crewScheduleRepository.findByGame_GameId(gameId);

        List<CrewScheduleDto> crewedMembers = scheduled.stream().map(cs -> {
            CrewMember member = cs.getCrewMember();
            CrewScheduleDto dto = new CrewScheduleDto();
            dto.setCrewedUserId(cs.getCrewedUserId());
            dto.setUserId(member.getId());
            dto.setGameId(gameId);
            dto.setPosition(cs.getPosition());
            dto.setFullName(member.getFirstName() + " " + member.getLastName());
            dto.setReportTime(cs.getReportTime());
            dto.setReportLocation(cs.getReportLocation());
            return dto;
        }).toList();

        return new CrewListResponseDto(
                game.getGameId(),
                null, // placeholder for gameStart (not implemented in entity)
                game.getGameDate(),
                game.getVenue(),
                game.getOpponent(),
                crewedMembers
        );
    }
}
