package com.rideeasy.controller;

import com.rideeasy.model.Driver;
import com.rideeasy.service.DriverService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DriverController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
//@AutoConfigureMockMvc(addFilters = false)
public class DriverControllerTest {
    @MockBean
    DriverController driverController;
    @MockBean
    private DriverService driverService;
    @InjectMocks
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




}
