package com.dkbanas.airflyer.Presentation;

import com.dkbanas.airflyer.Application.RequestDTO.FlightDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AirportResponseDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.FlightResponseDTO;
import com.dkbanas.airflyer.Infrastructure.FlightService;
import com.dkbanas.airflyer.Shared.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    public MockMvc mockMvc;
    @MockBean
    private FlightService flightService;
    @MockBean
    private JwtService jwtService;


    private FlightDTO flightDTO1;
    private FlightDTO flightDTO2;
    private FlightResponseDTO flightResponseDTO1;
    private FlightResponseDTO flightResponseDTO2;

    @BeforeEach
    void setUp() {
        flightDTO1 = new FlightDTO();
        flightDTO1.setFlightNumber("AA123");
        flightDTO1.setDepartureLocationId(1L);
        flightDTO1.setArrivalLocationId(2L);
        flightDTO1.setDepartureTime(LocalDateTime.now().plusHours(1));
        flightDTO1.setArrivalTime(LocalDateTime.now().plusHours(5));
        flightDTO1.setPriceEconomy(new BigDecimal("199.99"));
        flightDTO1.setAirline("American Airlines");

        flightDTO2 = new FlightDTO();
        flightDTO2.setFlightNumber("BB456");
        flightDTO2.setDepartureLocationId(3L);
        flightDTO2.setArrivalLocationId(4L);
        flightDTO2.setDepartureTime(LocalDateTime.now().plusHours(2));
        flightDTO2.setArrivalTime(LocalDateTime.now().plusHours(6));
        flightDTO2.setPriceEconomy(new BigDecimal("149.99"));
        flightDTO2.setAirline("British Airways");

        flightResponseDTO1 = new FlightResponseDTO();
        flightResponseDTO1.setId(1L);
        flightResponseDTO1.setFlightNumber("AA123");
        flightResponseDTO1.setDepartureTime(LocalDateTime.now().plusHours(1));
        flightResponseDTO1.setArrivalTime(LocalDateTime.now().plusHours(5));
        flightResponseDTO1.setPriceEconomy(new BigDecimal("199.99"));
        flightResponseDTO1.setTotalSeatsEconomy(150);
        flightResponseDTO1.setAvailableSeatsEconomy(50);
        flightResponseDTO1.setAvailableSeatsEconomyList(Arrays.asList("1A", "1B", "1C"));
        flightResponseDTO1.setAirline("American Airlines");

        flightResponseDTO2 = new FlightResponseDTO();
        flightResponseDTO2.setId(2L);
        flightResponseDTO2.setFlightNumber("BB456");
        flightResponseDTO2.setDepartureTime(LocalDateTime.now().plusHours(2));
        flightResponseDTO2.setArrivalTime(LocalDateTime.now().plusHours(6));
        flightResponseDTO2.setPriceEconomy(new BigDecimal("149.99"));
        flightResponseDTO2.setTotalSeatsEconomy(180);
        flightResponseDTO2.setAvailableSeatsEconomy(100);
        flightResponseDTO2.setAvailableSeatsEconomyList(Arrays.asList("2A", "2B", "2C"));
        flightResponseDTO2.setAirline("British Airways");
    }

    @Test
    @WithMockUser(username = "User", authorities = {"ROLE_ADMIN"})
    void getAllFlights() throws Exception {
        List<FlightResponseDTO> flights = Arrays.asList(new FlightResponseDTO(), new FlightResponseDTO());
        Mockito.when(flightService.getAllFlights()).thenReturn(flights);

        mockMvc.perform(MockMvcRequestBuilders.get("/flight"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    @WithMockUser(username = "User", authorities = {"ROLE_ADMIN"})
    void getFlightByFlightNumber() throws Exception {
        Mockito.when(flightService.getFlightByFlightNumber("AA123")).thenReturn(flightResponseDTO1);

        mockMvc.perform(MockMvcRequestBuilders.get("/flight/AA123"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.flightNumber").value("AA123"));
    }

    @Test
    @WithMockUser(username = "User", authorities = {"ROLE_ADMIN"})
    void createFlight() throws Exception {
        Mockito.when(flightService.createFlight(Mockito.any(FlightDTO.class))).thenReturn(flightResponseDTO1);

        mockMvc.perform(MockMvcRequestBuilders.post("/flight")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(flightDTO1)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "User", authorities = {"ROLE_ADMIN"})
    void deleteFlight() throws Exception {
        Mockito.doNothing().when(flightService).deleteFlight("AA123");

        mockMvc.perform(MockMvcRequestBuilders.delete("/flight/AA123").with(csrf()))
                .andExpect(status().isOk());
    }


}