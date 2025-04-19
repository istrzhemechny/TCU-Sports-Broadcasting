package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameScheduleDto;
import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameScheduleServiceTest {

    @Test
    void shouldAddGameScheduleSuccessfully() {
        GameScheduleRepository mockRepo = Mockito.mock(GameScheduleRepository.class);
        GameScheduleService service = new GameScheduleService(mockRepo);

        GameScheduleDto dto = new GameScheduleDto();
        dto.setSport("Basketball");
        dto.setSeason("2024-2025");

        GameSchedule saved = new GameSchedule("Basketball", "2024-2025");
        saved.setId(1L);

        when(mockRepo.save(any(GameSchedule.class))).thenReturn(saved);

        ApiResponse response = service.addGameSchedule(dto);

        assertTrue(response.isFlag());
        assertEquals(200, response.getCode());
        assertEquals("Add Success", response.getMessage());
        assertNotNull(response.getData());
    }
}
