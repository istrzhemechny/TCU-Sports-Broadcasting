package edu.tcu.cs.tcusportsbroadcasting.system;

import edu.tcu.cs.tcusportsbroadcasting.availability.Availability;
import edu.tcu.cs.tcusportsbroadcasting.availability.AvailabilityRepository;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import edu.tcu.cs.tcusportsbroadcasting.crewschedule.CrewSchedule;
import edu.tcu.cs.tcusportsbroadcasting.crewschedule.CrewScheduleRepository;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.Game;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final CrewMemberRepository crewMemberRepository;

    private final GameRepository gameRepository;

    private final AvailabilityRepository availabilityRepository;

    private final CrewScheduleRepository crewScheduleRepository;

    private final PasswordEncoder passwordEncoder;

    public DBDataInitializer(CrewMemberRepository crewMemberRepository, GameRepository gameRepository, AvailabilityRepository availabilityRepository, CrewScheduleRepository crewScheduleRepository, PasswordEncoder passwordEncoder) {
        this.crewMemberRepository = crewMemberRepository;
        this.gameRepository = gameRepository;
        this.availabilityRepository = availabilityRepository;
        this.crewScheduleRepository = crewScheduleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        CrewMember cm1 = new CrewMember();
        cm1.setFirstName("John");
        cm1.setLastName("Doe");
        cm1.setEmail("john.doe@example.com");
        cm1.setPhoneNumber("1234567890");
        cm1.setPassword(passwordEncoder.encode("password"));
        cm1.setRole("ADMIN");
        cm1.setPosition(Arrays.asList("Director", "Producer"));

        CrewMember cm2 = new CrewMember();
        cm2.setFirstName("Jane");
        cm2.setLastName("Smith");
        cm2.setEmail("jane.smith@example.com");
        cm2.setPhoneNumber("9876543210");
        cm2.setPassword(passwordEncoder.encode("secret123"));
        cm2.setRole("USER");
        cm2.setPosition(Arrays.asList("Camera", "Graphics"));

        crewMemberRepository.save(cm1);
        crewMemberRepository.save(cm2);

        CrewMember cm3 = new CrewMember();
        cm3.setFirstName("Clark");
        cm3.setLastName("Kent");
        cm3.setEmail("clark.kent2@dailyplanet.com");
        cm3.setPhoneNumber("1234567890");
        cm3.setPassword(passwordEncoder.encode("superman"));
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

        // Fetch existing crew member and game
        cm1 = crewMemberRepository.findAll().get(0);
        Game game1 = gameRepository.findAll().get(0);

// Create availability record
        Availability a1 = new Availability();
        a1.setCrewMember(cm1);
        a1.setGame(game1);
        a1.setAvailability(1); // available
        a1.setComment("Can be there early");

        availabilityRepository.save(a1);

        // Create some CrewSchedule records
        CrewSchedule cs1 = new CrewSchedule();
        cs1.setCrewMember(cm1);
        cs1.setGame(g1);
        cs1.setPosition("PRODUCER");
        cs1.setReportLocation("Control Room");
        cs1.setReportTime("5:30 PM");

        CrewSchedule cs2 = new CrewSchedule();
        cs2.setCrewMember(cm2);
        cs2.setGame(g1);
        cs2.setPosition("DIRECTOR");
        cs2.setReportLocation("Control Room");
        cs2.setReportTime("5:45 PM");

        this.crewScheduleRepository.saveAll(List.of(cs1, cs2));



    }
}
