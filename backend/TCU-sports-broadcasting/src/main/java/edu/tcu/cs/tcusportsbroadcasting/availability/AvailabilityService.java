package edu.tcu.cs.tcusportsbroadcasting.availability;

import edu.tcu.cs.tcusportsbroadcasting.availability.dto.AvailabilityDto;
import edu.tcu.cs.tcusportsbroadcasting.availability.dto.AvailabilityResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.availability.exception.*;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.Game;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final GameRepository gameRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository,
                               CrewMemberRepository crewMemberRepository,
                               GameRepository gameRepository) {
        this.availabilityRepository = availabilityRepository;
        this.crewMemberRepository = crewMemberRepository;
        this.gameRepository = gameRepository;
    }

    public AvailabilityResponseDto addAvailability(AvailabilityDto dto) {
        if (availabilityRepository.findByCrewMemberIdAndGame_GameId(dto.getUserId(), dto.getGameId()).isPresent()) {
            throw new AvailabilityAlreadyExistsException(dto.getUserId(), dto.getGameId());
        }

        CrewMember member = crewMemberRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AvailabilityUserNotFoundException(dto.getUserId()));

        Game game = gameRepository.findById(dto.getGameId())
                .orElseThrow(() -> new AvailabilityGameNotFoundException(dto.getGameId()));

        Availability availability = new Availability();
        availability.setCrewMember(member);
        availability.setGame(game);
        availability.setAvailability(dto.getAvailability());
        availability.setComment(dto.getComment());

        availabilityRepository.save(availability);

        return new AvailabilityResponseDto(
                member.getId(),
                game.getScheduleId(),
                game.getGameId(),
                availability.getAvailability(),
                availability.getComment()
        );
    }

    public List<AvailabilityResponseDto> findByUserId(Long userId) {
        CrewMember member = crewMemberRepository.findById(userId)
                .orElseThrow(() -> new AvailabilityUserNotFoundException(userId));

        return availabilityRepository.findByCrewMember_Id(userId)
                .stream()
                .map(a -> new AvailabilityResponseDto(
                        member.getId(),
                        a.getGame().getScheduleId(),
                        a.getGame().getGameId(),
                        a.getAvailability(),
                        a.getComment()
                ))
                .collect(Collectors.toList());
    }
}
