package com.logistics.zido.account.management.mapper;

import com.logistics.zido.account.management.entities.*;
import com.logistics.zido.account.management.helpers.*;

import java.math.BigDecimal;

public class MapperModel {

    public static VehicleDetailHelperResponse mapVehicleDetailToHelper(VehicleDetail vehicleDetail) {
        return new VehicleDetailHelperResponse().setChasisNumber(vehicleDetail.getChasisNumber())
                .setDriverAssignedStatus(vehicleDetail.isDriverAssignedStatus())
                .setEngineNumber(vehicleDetail.getEngineNumber())
                .setTonnage(vehicleDetail.getTonnage())
                .setTransporterHelperResponse(mapTransporterToHelper(vehicleDetail.getTransporter()))
                .setVehicleMakeHelperResponse(mapVehicleMakeToHelper(vehicleDetail.getVehicleMake()))
                .setVehicleNumber(vehicleDetail.getVehicleNumber())
                .setYearOfManufacture(vehicleDetail.getYearOfManufacture())
                .setYearOfPurchase(vehicleDetail.getYearOfPurchase());
    }

    public static TransporterHelperResponse mapTransporterToHelper(Transporter transporter) {
        return new TransporterHelperResponse().setTransporterAddress(transporter.getTransporterAddress())
                .setTransporterName(transporter.getTransporterName())
                .setTransporterPhoneNumber(transporter.getTransporterPhoneNumber())
                .setTransporterEmailAddress(transporter.getTransporterEmailAddress())
                .setAccountName(transporter.getAccountName())
                .setAccountNumber(transporter.getAccountNumber())
                .setAccountReference(transporter.getAccountReference())
                .setAccountStatus(transporter.isAccountStatus())
                .setBankCode(transporter.getBankCode())
                .setBankName(transporter.getBankName())
                .setCurrencyCode(transporter.getCurrencyCode())
                .setReservationReference(transporter.getReservationReference())
                .setTransporterStatus(transporter.isTransporterStatus());
    }

    public static VehicleMakeHelperResponse mapVehicleMakeToHelper(VehicleMake vehicleMake) {
        return new VehicleMakeHelperResponse().setVehicleMake(vehicleMake.getVehicleMake())
                .setVehicleManufacturer(vehicleMake.getVehicleManufacturer())
                .setVehicleTypeHelper(mapVehicleTypeToHelper(vehicleMake.getVehicleType()));
    }

    public static VehicleTypeHelper mapVehicleTypeToHelper(VehicleType vehicleType) {
        return new VehicleTypeHelper().setVehicleType(vehicleType.getVehicleType())
                .setVehicleDescription(vehicleType.getVehicleDescription());
    }

    public static ClientDetailHelper mapClientDetailToHelper(ClientDetail clientDetail) {
        return new ClientDetailHelper().setClientAddress(clientDetail.getClientAddress())
                .setPaymentTerms(clientDetail.getPaymentTerms())
                .setClientName(clientDetail.getClientName())
                .setContactPersonName(clientDetail.getContactPersonName())
                .setContactPhoneNumber(clientDetail.getContactPhoneNumber());
    }

    public static DriverInformationHelperResponse mapDriverInformationToHelper(DriverInformation driverInformation) {
        return new DriverInformationHelperResponse().setDateOfEmployment(driverInformation.getDateOfEmployment())
                .setDriverAddress(driverInformation.getDriverAddress())
                .setDriverLicenceNumber(driverInformation.getDriverLicenceNumber())
                .setDriverName(driverInformation.getDriverName())
                .setGuarantorAddress(driverInformation.getGuarantorAddress())
                .setGuarantorName(driverInformation.getGuarantorName())
                .setGuarantorPhoneNumber(driverInformation.getGuarantorPhoneNumber())
                .setNextOfKinAddress(driverInformation.getNextOfKinAddress())
                .setNextOfKinName(driverInformation.getNextOfKinName())
                .setNextOfKinPhoneNumber(driverInformation.getNextOfKinPhoneNumber())
                .setNextOfKinRelationship(driverInformation.getNextOfKinRelationship())
                .setVehicleDetailHelperResponse(mapVehicleDetailToHelper(driverInformation.getVehicleDetail()))
                .setVerificationStatus(driverInformation.isVerificationStatus());
    }

    public static TripDetailHelperResponse mapTripDetailToHelper(TripDetail tripDetail) {
        return new TripDetailHelperResponse().setDateOfRequest(tripDetail.getDateOfRequest())
                .setDestinationAddress(tripDetail.getDestinationAddress())
                .setDriverInformationHelperResponse(mapDriverInformationToHelper(tripDetail.getDriverInformation()))
                .setInvoiceAmount(tripDetail.getInvoiceAmount())
                .setPickUpLocationAddress(tripDetail.getPickUpLocationAddress())
                .setWayBillNumber(tripDetail.getWayBillNumber());
    }

    public static TripExpenseDetailsHelperResponse mapTripExpenseDetailsToHelper(TripExpenseDetails tripExpenseDetails) {
        return new TripExpenseDetailsHelperResponse()
                .setTripExpenseInformation(mapTripExpenseInformation(tripExpenseDetails.getExpense().getExpenseType(), tripExpenseDetails.getExpenseAmount()))
                .setTripDetailHelperResponse(mapTripDetailToHelper(tripExpenseDetails.getTripDetail()));
    }

    public static ExpensesHelper mapExpensesToHelper(Expenses expenses) {
        return new ExpensesHelper().setExpenseType(expenses.getExpenseType())
                .setExpenseDescription(expenses.getExpenseDescription());
    }

    public static TripExpenseInformation mapTripExpenseInformation(String expenseId, BigDecimal expenseAmount) {
        return new TripExpenseInformation().setExpenseAmount(expenseAmount)
                .setExpenseId(expenseId);
    }
}
