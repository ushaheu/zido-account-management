package com.logistics.zido.account.management.controller;

import com.logistics.zido.account.management.exception.TokenNotFoundException;
import com.logistics.zido.account.management.exception.ZidoAccountManagerException;
import com.logistics.zido.account.management.helpers.*;
import com.logistics.zido.account.management.helpers.monnify.response.ReserveAccountResponse;
import com.logistics.zido.account.management.service.ZidoAccountManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ZidoAccountManagementController {

    private ZidoAccountManagementService zidoAccountManagementService;

    @Autowired
    public ZidoAccountManagementController(ZidoAccountManagementService zidoAccountManagementService) {
        this.zidoAccountManagementService = zidoAccountManagementService;
    }

    @CrossOrigin
    @PostMapping("/v1/create/expense")
    @ResponseStatus(HttpStatus.CREATED)
    public void createExpense(@RequestBody ExpensesHelper expensesHelper) throws ZidoAccountManagerException {
        zidoAccountManagementService.createExpense(expensesHelper);
    }

    @CrossOrigin
    @GetMapping("/v1/list/expense")
    public List<ExpensesHelper> listExpense() throws ZidoAccountManagerException {
        return zidoAccountManagementService.listExpenses();
    }

    @CrossOrigin
    @PostMapping("/v1/create/vehicle/make")
    public void createVehicleMake(@RequestBody VehicleMakeRequest vehicleMakeRequest) throws ZidoAccountManagerException {
        zidoAccountManagementService.createVehicleMake(vehicleMakeRequest);
    }

    @CrossOrigin
    @GetMapping("/v1/list/vehicle/make")
    public List<VehicleMakeHelperResponse> listVehicleMake() throws ZidoAccountManagerException {
        return zidoAccountManagementService.listVehicleMakes();
    }

    @CrossOrigin
    @PostMapping("/v1/create/client/detail")
    public void createClientDetail(@RequestBody ClientDetailHelper clientDetailHelper) throws ZidoAccountManagerException {
        zidoAccountManagementService.createClientDetail(clientDetailHelper);
    }

    @CrossOrigin
    @GetMapping("/v1/list/client/detail")
    public List<ClientDetailHelper> listClientDetail() throws ZidoAccountManagerException {
        return zidoAccountManagementService.listClientDetails();
    }

    @CrossOrigin
    @PostMapping("/v1/create/vehicle/type")
    public void createVehicleType(@RequestBody VehicleTypeHelper vehicleTypeHelper) throws ZidoAccountManagerException {
        zidoAccountManagementService.createVehicleType(vehicleTypeHelper);
    }

    @CrossOrigin
    @GetMapping("/v1/list/vehicle/type")
    public List<VehicleTypeHelper> listVehicleType() throws ZidoAccountManagerException {
        return zidoAccountManagementService.listVehicleTypes();
    }

    @CrossOrigin
    @PostMapping("/v1/create/vehicle/detail")
    public void createVehicleDetail(@RequestBody VehicleDetailHelperRequest vehicleDetailHelperRequest) throws ZidoAccountManagerException {
        zidoAccountManagementService.createVehicleDetail(vehicleDetailHelperRequest);
    }

    @CrossOrigin
    @GetMapping("/v1/list/vehicle/detail")
    public List<VehicleDetailHelperResponse> listVehicleDetail() throws ZidoAccountManagerException {
        return zidoAccountManagementService.listVehicleDetails();
    }

    @CrossOrigin
    @PostMapping("/v1/create/transporter")
    public void createTransporter(@RequestBody TransporterHelperRequest transporterHelperRequest) throws ZidoAccountManagerException, TokenNotFoundException {
        zidoAccountManagementService.createTransporter(transporterHelperRequest);
    }

    @CrossOrigin
    @GetMapping("/v1/add/transporter/account")
    public TransporterHelperResponse addAccountToTransporter(@RequestParam("transporterId") Long transporterId) throws ZidoAccountManagerException, TokenNotFoundException {
        return zidoAccountManagementService.addAccountDetailsToTransporter(transporterId);
    }

    @CrossOrigin
    @GetMapping("/v1/list/vehicle/unassigned")
    public List<VehicleDetailHelperResponse> listUnAssignedVehicles() {
        return zidoAccountManagementService.listVehiclesByStatus(false);
    }

    @CrossOrigin
    @GetMapping("/v1/list/vehicle/assigned")
    public List<VehicleDetailHelperResponse> listAssignedVehicles() {
        return zidoAccountManagementService.listVehiclesByStatus(true);
    }

    @CrossOrigin
    @GetMapping("/v1/list/all/vehicles")
    public List<VehicleDetailHelperResponse> listAllVehicles() {
        return zidoAccountManagementService.listAllVehicles();
    }

    @CrossOrigin
    @PostMapping("/v1/create/driver/information")
    public void createDriverInformation(@RequestBody DriverInformationHelperRequest driverInformationHelperRequest) throws ZidoAccountManagerException, TokenNotFoundException {
        zidoAccountManagementService.createDriverInformation(driverInformationHelperRequest);
    }

    @CrossOrigin
    @GetMapping("/v1/add/driver/vehicle")
    public void addDriverInformationToVehicle(@RequestParam("driverInformationId") Long driverInformationId, @RequestParam("vehicleDetailId") Long vehicleDetailId) throws ZidoAccountManagerException {
        zidoAccountManagementService.addDriverInformationToVehicle(driverInformationId, vehicleDetailId);
    }

    @CrossOrigin
    @PostMapping("/v1/create/trip/detail")
    public void createTripDetail(@RequestBody TripDetailHelperRequest tripDetailHelperRequest) throws ZidoAccountManagerException, TokenNotFoundException {
        zidoAccountManagementService.createTripDetails(tripDetailHelperRequest);
    }

    @CrossOrigin
    @GetMapping("/v1/list/trip/details")
    public List<TripDetailHelperResponse> listAllTripDetails() {
        return zidoAccountManagementService.getAllTripDetails();
    }

    @CrossOrigin
    @PostMapping("/v1/create/trip/expense")
    public void createTripExpenses(TripExpenseDetailsHelperRequest tripExpenseDetailsHelperRequest) throws ZidoAccountManagerException {
        zidoAccountManagementService.createTripExpenses(tripExpenseDetailsHelperRequest);
    }

    @CrossOrigin
    @GetMapping("/v1/list/trip/expenses")
    public List<TripExpenseDetailsHelperResponse> getAllTripExpenses() {
        return zidoAccountManagementService.getAllTripExpenses();
    }

    @CrossOrigin
    @GetMapping("/v1/deallocate/transporter/account/{accountNumber}")
    public ReserveAccountResponse deallocateTransporterAccount(@PathVariable ("accountNumber") String accountNumber) throws ZidoAccountManagerException, TokenNotFoundException {
        return zidoAccountManagementService.deallocateAccount(accountNumber);
    }

    @CrossOrigin
    @GetMapping("/v1/list/unmapped/transporters")
    public List<TransporterDetailHelper> listTransportersNotMappedToAccount() {
        return zidoAccountManagementService.listTransportersNotMappedToAccount();
    }

    @CrossOrigin
    @GetMapping("/v1/list/all/transporters")
    public List<TransporterHelperResponse> listAllTransporters() {
        return zidoAccountManagementService.listAllTransporters();
    }

}
