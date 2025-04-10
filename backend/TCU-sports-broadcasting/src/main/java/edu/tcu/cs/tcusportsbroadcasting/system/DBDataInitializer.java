package edu.tcu.cs.tcusportsbroadcasting.system;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.Game;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final CrewMemberRepository crewMemberRepository;

    private final GameRepository gameRepository;

    public DBDataInitializer(CrewMemberRepository crewMemberRepository, GameRepository gameRepository) {
        this.crewMemberRepository = crewMemberRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        CrewMember cm1 = new CrewMember();
        cm1.setFirstName("John");
        cm1.setLastName("Doe");
        cm1.setEmail("john.doe@example.com");
        cm1.setPhoneNumber("1234567890");
        cm1.setPassword("P@ssw0rd");
        cm1.setRole("ADMIN");
        cm1.setPosition(Arrays.asList("Director", "Producer"));

        CrewMember cm2 = new CrewMember();
        cm2.setFirstName("Jane");
        cm2.setLastName("Smith");
        cm2.setEmail("jane.smith@example.com");
        cm2.setPhoneNumber("9876543210");
        cm2.setPassword("Secret123");
        cm2.setRole("USER");
        cm2.setPosition(Arrays.asList("Camera", "Graphics"));

        crewMemberRepository.save(cm1);
        crewMemberRepository.save(cm2);

        CrewMember cm3 = new CrewMember();
        cm3.setFirstName("Clark");
        cm3.setLastName("Kent");
        cm3.setEmail("clark.kent2@dailyplanet.com");
        cm3.setPhoneNumber("1234567890");
        cm3.setPassword("superman");
        cm3.setRole("ADMIN");
        cm3.setPosition(List.of("Director", "Producer"));

        crewMemberRepository.save(cm3);

        Game g1 = new Game();
        g1.setScheduleId(1L);
        g1.setGameDate(LocalDate.of(2024, 9, 7));
        g1.setVenue("Carter");
        g1.setOpponent("LIU");
        g1.setFinalized(false);

        Game g2 = new Game();
        g2.setScheduleId(1L);
        g2.setGameDate(LocalDate.of(2024, 9, 14));
        g2.setVenue("Carter");
        g2.setOpponent("UCF");
        g2.setFinalized(false);

        gameRepository.save(g1);
        gameRepository.save(g2);


    }
}
