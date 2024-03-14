package com.rideeasy.service;

import com.rideeasy.exception.NotFoundException;
import com.rideeasy.exception.RideEasyException;
import com.rideeasy.model.Cab;
import com.rideeasy.repository.CabRepository;
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
public class CabServiceTest {
    @Mock
    private CabRepository cabRepository;
    @InjectMocks
    private CabServiceImpl cabService;

    private Cab requestCab;

    private Cab responseCab;
    private Cab updateCab;

    @BeforeEach
    public void init(){
        System.err.println("inside init method");
        requestCab = new Cab();
        requestCab.setCarType("Mini");
        requestCab.setPerKmRate(10.5F);

        responseCab= new Cab();
        responseCab.setCabId(12);
        responseCab.setCarType("Mini");
        responseCab.setPerKmRate(10.5F);

        updateCab= new Cab();
        updateCab.setCabId(12);
        updateCab.setCarType("Micro");
        updateCab.setPerKmRate(8.8F);

    }

    @Test
    @DisplayName("insertCab method can register a cab")
    public void testInsertCab_whenValidDetailsGiven_shouldReturnRegisteredCab() {
        //Arrange
        // Mocking repository method to return empty optional indicating cab doesn't exist
        when(cabRepository.findById(null)).thenReturn(Optional.empty());             // ===================== why findById method taking null value
        // Mocking repository method to return the persisted cab
        when(cabRepository.save(any(Cab.class))).thenReturn(responseCab);

        // Act
        Cab registeredCab = cabService.insertCab(requestCab);

        // Assert
        assertNotNull(registeredCab,"registeredCab should not be null");
        assertEquals(responseCab, registeredCab);
        // Verifying that findById and save methods are called
        verify(cabRepository, times(1)).findById(requestCab.getCabId());
        verify(cabRepository, times(1)).save(requestCab);
    }

    @Test
    public void testInsertCab_CabAlreadyExists() {
        //Arrange
        // Mocking repository method to return existing cab
        when(cabRepository.findById(null)).thenReturn(Optional.of(responseCab));
        //Act and Assert
        assertThrows(RideEasyException.class,()->{
            cabService.insertCab(requestCab);
        },"Should have been thrown RideEasyException");
        //Verifying
        verify(cabRepository, times(1)).findById(null);    // ===================== why findById method taking null value
    }
    @Test
    public void testUpdateCab_Success() {
        //Arrange
        // Mocking repository method to return the sample cab
        when(cabRepository.findById(anyInt())).thenReturn(Optional.of(responseCab));
        // Mocking repository save method to return the sample cab
        when(cabRepository.save(any(Cab.class))).thenReturn(updateCab);

        // Act
        Cab result = cabService.updateCab(updateCab);

        // Assert
        assertNotNull(result,"Result should not be null");
        assertEquals(updateCab, result, "Cab is not updated");
        // Verifying that findById method was called once with the correct argument
        verify(cabRepository, times(1)).findById(responseCab.getCabId());
        // Verifying that save method was called once with the correct argument
        verify(cabRepository, times(1)).save(updateCab);
    }
    @Test
    public void testUpdateCab_CabNotExists() {
        //Arrange
        // Mocking repository method to return empty optional, indicating cab doesn't exist
        when(cabRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RideEasyException.class, ()->{
            cabService.updateCab(updateCab);
        },"Cab should not have been exist");
        //Verify
        // Verifying that findById method was called once with the correct argument
        verify(cabRepository, times(1)).findById(responseCab.getCabId());
    }

    @Test
    public void testDeleteCab_Success() {
        //Arrange
        when(cabRepository.findById(anyInt())).thenReturn(Optional.of(responseCab));
//        doNothing().when(cabRepository).deleteById(anyInt());

        // Act
        Cab result = cabService.deleteCab(12);
        // Assert
        assertNotNull(result);
        assertEquals(responseCab, result);

        //Verify
        // Verifying that findById method was called once with the correct argument
        verify(cabRepository, times(1)).findById(12);
        // Verifying that deleteById method was called once with the correct argument
        verify(cabRepository, times(1)).deleteById(12);
    }

    @Test
    public void testDeleteCab_CabNotExists() {
        //Arrange
        // Mocking repository method to return empty optional, indicating cab doesn't exist
        when(cabRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RideEasyException.class,()->{
            cabService.deleteCab(12);
        });
        // Verify
        verify(cabRepository, times(1)).findById(12);
    }
    @Test
    public void testDeleteCab_NullPointerException() {
        //Arrange
        // Mocking repository method to return empty optional, indicating cab doesn't exist
        doThrow(NullPointerException.class).when(cabRepository).findById(null);
        // Act and Assert
        assertThrows(NullPointerException.class,()->{
            cabService.deleteCab(null);
        },"Should have been thrown Null Pointer Exception");
        // Verify
        verify(cabRepository, times(1)).findById(null);
    }

    // unit tests for viewCabsOfType() method;
    @Test
    public void testViewCabsOfType_Success() {
        // Arrange
        String carType = "Mini";
        List<Cab> sampleCabs = new ArrayList<>();
        sampleCabs.add(new Cab(1, "Mini", 18.8F,null));
        sampleCabs.add(new Cab(2, "Mini", 18.8F, null ));

        when(cabRepository.findByCarType(carType)).thenReturn(sampleCabs);

        // Act
        List<Cab> result = cabService.viewCabsOfType(carType);

        // Verifying the result
        assertNotNull(result,"Result should not be null");
        assertEquals(sampleCabs.size(), result.size(),"list size should have been same");
        assertEquals(sampleCabs, result,"Should have given the appropriate cab list");
        // Verifying that findByCarType method was called once with the correct argument
        verify(cabRepository, times(1)).findByCarType(carType);
    }

    @Test
    public void testViewCabsOfType_NoCabsFound() {
        // Arrange
        String carType = "Mini";
        // Mocking repository method to return an empty list, indicating no cabs found
        when(cabRepository.findByCarType(carType)).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(RideEasyException.class,()->{
            cabService.viewCabsOfType(carType);
        },"should have been thrown RideEasyException");
        //Verify
        verify(cabRepository, times(1)).findByCarType(carType);

    }
    @Test
    public void testCountCabsOfType_Success() {
        // Arrange
        String carType = "Mini";
        int sampleCount = 5;
        when(cabRepository.countByCarType(carType)).thenReturn(sampleCount);

        // Act
        Integer result = cabService.countCabsOfType(carType);

        // Assert
        assertNotNull(result,"result should not have been null");
        assertEquals((Integer) sampleCount, result);
        // Verifying that countByCarType method was called once with the correct argument
        verify(cabRepository, times(1)).countByCarType(carType);
    }




}
