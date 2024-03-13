package com.rideeasy.service;

import com.rideeasy.exception.NotFoundException;
import com.rideeasy.exception.RideEasyException;
import com.rideeasy.model.Driver;
import com.rideeasy.repository.DriverRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {
    @Mock
    private DriverRepository driverRepository;
    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver requestDriver;
    private Driver responseDriver;
    private Driver updatedDriver;

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

        updatedDriver = new Driver();
        updatedDriver.setDriverId(10);
        updatedDriver.setName("Ram Kishan");
        updatedDriver.setUserName("Ramu123");
        updatedDriver.setPassword("1234");
        updatedDriver.setAddress("Mumbai");
        updatedDriver.setMobileNumber("7865453612");
        updatedDriver.setEmail("ramu@gmail.com");
        updatedDriver.setLicenceNumber("AB-1234567890123");
    }
    @Test
    @DisplayName("Driver can be persisted")
    public void testInsertDriver_whenValidDetailsAreProvided_thenReturnRegisteredDriver(){
        //Arrange
        when(driverRepository.findByUserName(anyString())).thenReturn(Optional.empty() );
        when(driverRepository.findByMobileNumber(anyString())).thenReturn(Optional.empty());
        when(driverRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(driverRepository.save(any(Driver.class))).thenReturn(responseDriver);
        //Act
        Driver registeredDriver = driverService.insertDriver(requestDriver);
        //Assert
        assertEquals(responseDriver.getUserName(), registeredDriver.getUserName(),()->"Username is incorrect");
        assertEquals(responseDriver.getEmail(), registeredDriver.getEmail(),()->"User's email is not matching");
        assertNotNull(registeredDriver,()->"Should have returned registered driver");
        assertNotNull(registeredDriver.getDriverId(),()->"driver id should not be null");
        assertEquals(responseDriver, registeredDriver,"reponse driver and registered driver should have been equal");
    }
    @Test
    @DisplayName("InsterDriver can give null pointer exception")
    public void testInsertDriver_wheNullInputGiven_thenThrowRideEasyException(){
        //Arrange
        String expectedMessage="null value";
        //Act and Assert
        RideEasyException rideEasyException= assertThrows(RideEasyException.class, ()->{
           driverService.insertDriver(null) ;
        });
        //Assert
        assertEquals(expectedMessage, rideEasyException.getMessage(),"Exception message is not same");
    }
    @Test
    @DisplayName("InsertDriver throws ride easy exception for existing user-name ")
    public void testInsertDriver_whenExistingUsernameProvided_thenThrowRideEasyException(){
        //Arrange
        String expectedMessage="This username already exists, please provide another username";
        when(driverRepository.findByUserName(anyString())).thenReturn(Optional.of(responseDriver));
        //Act and Assert
        RideEasyException rideEasyException= assertThrows(RideEasyException.class, ()->{
           driverService.insertDriver(requestDriver);
        });
        //Assert
        assertEquals(expectedMessage, rideEasyException.getMessage(),"Exception message not matching");
    }
    @Test
    @DisplayName("InsertDriver throws ride easy exception for existing mobile number ")
    public void testInsertDriver_whenExistingMobileNumberProvided_thenThrowRideEasyException(){
        //Arrange
        when(driverRepository.findByUserName(anyString())).thenReturn(Optional.empty());
        when(driverRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(responseDriver));
        //Act and Assert
        assertThrows(RideEasyException.class, ()->{
           driverService.insertDriver(requestDriver);
        });
    }
    @Test
    @DisplayName("InsertDriver throws ride easy exception for existing email ")
    public void testInsertDriver_whenExistingEmailProvided_thenThrowRideEasyException(){
        //Arrange
        String expectedMessage="This email already exists, please provide another email";
//        when(driverRepository.findByUserName(anyString())).thenReturn(Optional.empty());
//        when(driverRepository.findByMobileNumber(anyString())).thenReturn(Optional.empty());
        when(driverRepository.findByEmail(anyString())).thenReturn(Optional.of(responseDriver));
        //Act and Assert
        RideEasyException rideEasyException= assertThrows(RideEasyException.class, ()->{
            driverService.insertDriver(requestDriver);
        });
        //Assert
        assertEquals(expectedMessage, rideEasyException.getMessage(),"exception message should have been same");
    }

    // testing update driver method
    @Test
    @DisplayName("Driver can be updated")
    public void testUpdateDriver_whenUpdatedDetailsAreProvided_thenReturnUpdateDriver(){
        //Arrange
        when(driverRepository.save(any(Driver.class))).thenReturn(updatedDriver);
        when(driverRepository.findByUserName(anyString())).thenReturn(Optional.of(responseDriver));
        //Act
        Driver resultDriver= driverService.updateDriver(requestDriver);
        //Assert
        assertEquals(updatedDriver, resultDriver,()->"Driver is not updated");
    }
    @Test
    @DisplayName("updateDriver throws RideEasyException when given null value")
    public void testUpdateDriver_whenGivenNull_thenThrowsRideEasyException(){
        //Arrange
        //Act and Assert
        assertThrows(RideEasyException.class, ()->{
            driverService.updateDriver(null);
        });
    }
    @Test
    @DisplayName("updateDriver throws RideEasyException when when user-name does not exist")
    public void testUpdateDriver_whenGivenUnknownUsername_thenThrowsRideEasyException(){
        //Arrange
        String expextedMessage="This username does not exists";
        when(driverRepository.findByUserName(anyString())).thenReturn(Optional.empty());
        //Act and Assert
        RideEasyException rideEasyException= assertThrows(RideEasyException.class, ()->{
            driverService.updateDriver(requestDriver);
        });
        //Assert
        assertEquals(expextedMessage, rideEasyException.getMessage(),()->"exception message should have been same");
    }

    // testing for deleteDriver method;
    @Test
    public void testDeleteDriver_whenDriverIdIsGiven_thenDeleteTheDriver(){
        //Arrange
        when(driverRepository.findById(anyInt())).thenReturn(Optional.of(responseDriver));
        //Act
        Driver result= driverService.deleteDriver(10);
        //Assert
        assertNotNull(result);
        assertEquals(responseDriver, result);
        verify(driverRepository, times(1)).deleteById(10);
    }
    @Test
    @DisplayName("deleteDriver throws 'NotFoundException' for non-existing driver id")
    public void testDeleteDriver_whenNonExistingDriverIdIsGiven_thenThrowNotFoundException(){
        //Arrange
        when(driverRepository.findById(anyInt())).thenReturn(Optional.empty());
        //Act and Assert
        assertThrows(NotFoundException.class, ()->{
           driverService.deleteDriver(10);
        });
    }
    @Test
    @DisplayName("deleteDriver throws 'Exception' for null id")
    public void testDeleteDriver_whenNullIdIsGiven_thenThrowException(){
        //Arrange
        doThrow(RuntimeException.class).when(driverRepository).findById(null);
        //Act and Assert
        assertThrows(RuntimeException.class, ()->{
            driverService.deleteDriver(null);
        });
    }

    // unit tests for viewBestDrivers() method;
    @Test
    @DisplayName("viewBestDrivers method returns best drivers list")
    public void testViewBestDrivers_whenValidValueProvided_thenReturnBestDriver(){
        //Arrange
        List<Driver> driverList= new ArrayList<>();
//        driverList.add(requestDriver);
        driverList.add(responseDriver);
        driverList.add(updatedDriver);
        when(driverRepository.findByRatingGreaterThanEqual(4.5F)).thenReturn(driverList);
        //Act
        List<Driver> resultList= driverService.viewBestDrivers();
        //Assert
        assertEquals(driverList, resultList,"Best drivers list should have been given");
    }
    @Test
    @DisplayName("ViewBestDrivers throws NotFoundException")
    public void testViewBestDrivers_throwNotFoundException(){
        //Arrange
        List<Driver> emptyList= new ArrayList<>();
        String expectedMessage="No driver found";
        when(driverRepository.findByRatingGreaterThanEqual(4.5F)).thenReturn(emptyList);
        //Act and Assert
        NotFoundException notFoundException= assertThrows(NotFoundException.class,()->{
           driverService.viewBestDrivers();
        },"Should have thrown NotFoundException");
        //Assert
        assertEquals(expectedMessage, notFoundException.getMessage(),"Exception message should have been same");
    }

    // unit tests for viewDriver() method;
    @Test
    public void testViewDriver_whenDriverIdProvided_thenReturnDriverDetails(){
        //Arrange
        when(driverRepository.findById(10)).thenReturn(Optional.of(responseDriver));
        //Act
        Driver result= driverService.viewDriver(10);
        //Assert
        assertEquals(responseDriver, result, "Not getting the required driver");
        assertNotNull(result.getDriverId(),"Driver id should not have been null");
    }
    @Test
    @DisplayName("ViewDriver throws NotFoundException ")
    public void testViewDriver_whenDriverIdProvided_thenThrowNotFoundException(){
        //Arrange
        when(driverRepository.findById(10)).thenThrow(NotFoundException.class);
        //Act and Assert
        assertThrows(NotFoundException.class,()->{
            driverService.viewDriver(10);
        },"should have thrown NotFoundException");
    }
    @Test
    @DisplayName("ViewDriver throws RunTimeException for null value")
    public void testViewDriver_whenNullIdProvided_thenThrowRunTimeException(){
        //Arrange
        doThrow(RuntimeException.class).when(driverRepository).findById(null);
//        when(driverRepository.findById(10)).thenThrow(NotFoundException.class);
        //Act and Assert
        assertThrows(RuntimeException.class,()->{
            driverService.viewDriver(null);
        },"should have thrown RunTimeException");
    }

    // unit tests for viewDriverByUserName() method;
    @Test
    public void testViewDriverByUserName_whenDriverUsernameProvided_thenReturnsDriverDetails(){
        //Arrange
        when(driverRepository.findByUserName("Ramu123")).thenReturn(Optional.of(responseDriver));
        //Act
        Driver result= driverService.viewDriverByUserName("Ramu123");
        //Assert
        assertEquals(responseDriver, result, "Not getting the required driver");
        assertNotNull(result.getDriverId(),"Driver should not have been null");
    }
    @Test
    @DisplayName("ViewDriverByUserName throws NotFoundException ")
    public void testViewDriverByUserName_whenDriverUsernameProvided_thenThrowsNotFoundException(){
        //Arrange
        when(driverRepository.findByUserName("Ramu123")).thenThrow(NotFoundException.class);
        //Act and Assert
        assertThrows(NotFoundException.class,()->{
            driverService.viewDriverByUserName("Ramu123");
        },"should have thrown NotFoundException");
    }
    @Test
    @DisplayName("ViewDriverByUserName throws RunTimeException for null value")
    public void testViewDriverByUserName_whenNullUsernameProvided_thenThrowsRunTimeException(){
        //Arrange
        doThrow(RuntimeException.class).when(driverRepository).findByUserName("Ramu123");
        //Act and Assert
        assertThrows(RuntimeException.class,()->{
            driverService.viewDriverByUserName(null);
        },"should have thrown RunTimeException");
    }

}
