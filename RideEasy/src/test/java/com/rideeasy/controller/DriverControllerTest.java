package com.rideeasy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideeasy.model.Driver;
import com.rideeasy.service.DriverService;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = DriverController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
public class DriverControllerTest {
    @MockBean
    DriverController driverController;
    @MockBean
    private DriverService driverService;
    @Autowired
    MockMvc mockMvc;

    private Driver requestDriver;
    private Driver responseDriver;

    @BeforeEach
    public void init(){
        System.err.println("inside init method");
        requestDriver = new Driver();
        requestDriver.setName("Ramu");
        requestDriver.setUserName("Ramu123");
        requestDriver.setPassword("abcd");
        requestDriver.setAddress("Bengaluru");
        requestDriver.setMobileNumber("9867453612");
        requestDriver.setEmail("ramu@gmail.com");
        requestDriver.setLicenceNumber("AB-1234567890123");

        responseDriver = new Driver();
        responseDriver.setDriverId(10);
        responseDriver.setName("Ramu");
        responseDriver.setUserName("Ramu123");
        responseDriver.setPassword("abcd");
        responseDriver.setAddress("Bengaluru");
        responseDriver.setMobileNumber("9867453612");
        responseDriver.setEmail("ramu@gmail.com");
        responseDriver.setLicenceNumber("AB-1234567890123");
    }

    @Test
    @WithMockUser
    public void testTestHandler() throws Exception {
        //Arrange
        String expectedResult ="Welcome to Driver Handler spring security ";
        RequestBuilder requestBuilder= MockMvcRequestBuilders.get("/drivers/hello");
        //Act
        MvcResult mvcResult= mockMvc.perform(requestBuilder).andReturn();
        String result= mvcResult.getResponse().getContentAsString();
        //Assert
        System.out.println("result: "+result);
        assertEquals(expectedResult, result);
    }

    @Test
//    @WithMockUser
    public void testHandler() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/drivers/hello"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Welcome to Driver Handler spring security "));
    }

    @Test
    public void testAddDriverHandler_whenValidDetailsProvided_shouldReturnRegisteredDriver() throws Exception {
        //Arrange
        RequestBuilder requestBuilder= MockMvcRequestBuilders.post("/drivers/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(requestDriver));
        //Act
        MvcResult mvcResult= mockMvc.perform(requestBuilder).andReturn();
        String resultAsString= mvcResult.getResponse().getContentAsString();
        System.out.println("resultAsString: "+resultAsString);
        Driver registeredDriver= new ObjectMapper().readValue(resultAsString, Driver.class);
        //Assert
        assertNotNull(registeredDriver,"Registered driver should not heve been null");
        assertEquals(responseDriver, registeredDriver,"Driver not registered");

    }



}
