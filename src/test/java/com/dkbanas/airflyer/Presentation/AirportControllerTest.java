package com.dkbanas.airflyer.Presentation;

import com.dkbanas.airflyer.Application.RequestDTO.AirportDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AirportResponseDTO;
import com.dkbanas.airflyer.Infrastructure.AirportService;
import com.dkbanas.airflyer.Shared.JwtService;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(AirportController.class)
class AirportControllerTest {
    @Autowired
    public MockMvc mockMvc;
    @MockBean
    private AirportService airportService;

    @MockBean
    private JwtService jwtService;


    AirportResponseDTO airport1 = new AirportResponseDTO();
    AirportResponseDTO airport2 = new AirportResponseDTO();
    AirportDTO airport_req = new AirportDTO();
    List<AirportResponseDTO> airports = new ArrayList<>();

    @BeforeEach
    void setUp(){

        airport_req.setName("John F. Kennedy International Airport");
        airport_req.setCode("JFK");
        airport_req.setCity("New York");
        airport_req.setCountry("USA");
        airport_req.setCityPhotoUrl("https://example.com/jfk.jpg");
        airport_req.setContinent("North America");

        airport1.setId(1L);
        airport1.setName("John F. Kennedy International Airport");
        airport1.setCode("JFK");
        airport1.setCity("New York");
        airport1.setCountry("USA");
        airport1.setCityPhotoUrl("https://example.com/jfk.jpg");
        airport1.setContinent("North America");

        airport2.setId(2L);
        airport2.setName("Los Angeles International Airport");
        airport2.setCode("LAX");
        airport2.setCity("Los Angeles");
        airport2.setCountry("USA");
        airport2.setCityPhotoUrl("https://example.com/lax.jpg");
        airport2.setContinent("North America");




        airports.add(airport1);
        airports.add(airport2);
    }


    @Test
    @WithMockUser(username = "User", authorities = {"ROLE_USER"})
    void testGetAllAirports() throws Exception {

        Mockito.when(airportService.getAllAirports()).thenReturn(airports);

        RequestBuilder request = MockMvcRequestBuilders.get("/airport");
        this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));;
    }



    @Test
    @WithMockUser(username = "User", authorities = {"ROLE_USER"})
    void testSearchAirports() throws Exception {
        Mockito.when(airportService.searchAirports("NewYork"))
                .thenReturn(List.of(airport1));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/airport/search")
                        .param("query", "NewYork"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "User", authorities = {"ROLE_USER"})
    void testGetAirportByCode_Success() throws Exception {
        Mockito.when(airportService.getAirportByCode("JFK")).thenReturn(airport1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/airport/JFK"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("JFK"));
    }

    @Test
    @WithMockUser(username = "User", authorities = {"ROLE_ADMIN"})
    void testCreateAirport_Success() throws Exception {

        Mockito.when(airportService.createAirport(Mockito.any(AirportDTO.class))).thenReturn(airport1);

        mockMvc.perform(MockMvcRequestBuilders.post("/airport")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(airport_req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("JFK"));
    }

    @Test
    @WithMockUser(username = "Admin", authorities = {"ROLE_ADMIN"})
    void testDeleteAirport_Success() throws Exception {
        Mockito.doNothing().when(airportService).deleteAirport("JFK");

        mockMvc.perform(MockMvcRequestBuilders.delete("/airport/JFK")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}