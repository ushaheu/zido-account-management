package com.logistics.zido.account.management.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.zido.account.management.entities.*;
import com.logistics.zido.account.management.entities.redis.TokenManager;
import com.logistics.zido.account.management.exception.TokenNotFoundException;
import com.logistics.zido.account.management.exception.ZidoAccountManagerException;
import com.logistics.zido.account.management.helpers.*;
import com.logistics.zido.account.management.helpers.monnify.ReserveAccountRequest;
import com.logistics.zido.account.management.helpers.monnify.authentication.AuthenticationResponse;
import com.logistics.zido.account.management.helpers.monnify.response.ReserveAccountResponse;
import com.logistics.zido.account.management.helpers.monnify.response.ResponseBody;
import com.logistics.zido.account.management.mapper.MapperModel;
import com.logistics.zido.account.management.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ZidoAccountManagementService {

    Logger logger = LoggerFactory.getLogger(ZidoAccountManagementService.class);

    @Value("${monnify.zido.contract.code}")
    private String contractCode;

    @Value("${monnify.zido.api.key}")
    private String monnifyUserName;

    @Value("${monnify.zido.secret.key}")
    private String monnifyPassword;

    @Value("${monnify.base.url}")
    private String monnifyBaseUrl;

    @Value("${currency.code}")
    private String currencyCode;

    @Value("${reserve.account.identifier}")
    private String reserveAccountIdentifier;

    private RestTemplate restTemplate;

    private ClientDetailRepository clientDetailRepository;
    private DriverInformationRepository driverInformationRepository;
    private ExpensesRepository expensesRepository;
    private TransporterRepository transporterRepository;
    private TripDetailRepository tripDetailRepository;
    private TripExpenseRepository tripExpenseRepository;
    private VehicleDetailRepository vehicleDetailRepository;
    private VehicleMakeRepository vehicleMakeRepository;
    private TokenManagerRepository tokenManagerRepository;
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    public ZidoAccountManagementService(RestTemplate restTemplate, ClientDetailRepository clientDetailRepository,
                                        DriverInformationRepository driverInformationRepository,
                                        ExpensesRepository expensesRepository, TransporterRepository transporterRepository,
                                        TripDetailRepository tripDetailRepository, TripExpenseRepository tripExpenseRepository,
                                        VehicleDetailRepository vehicleDetailRepository, VehicleMakeRepository vehicleMakeRepository,
                                        TokenManagerRepository tokenManagerRepository, VehicleTypeRepository vehicleTypeRepository) {
        this.restTemplate = restTemplate;
        this.clientDetailRepository = clientDetailRepository;
        this.driverInformationRepository = driverInformationRepository;
        this.expensesRepository = expensesRepository;
        this.transporterRepository = transporterRepository;
        this.tripDetailRepository = tripDetailRepository;
        this.tripExpenseRepository = tripExpenseRepository;
        this.vehicleDetailRepository = vehicleDetailRepository;
        this.vehicleMakeRepository = vehicleMakeRepository;
        this.tokenManagerRepository = tokenManagerRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @PostConstruct
    @Scheduled(fixedRate = 3600000)
    public void generateToken() throws ZidoAccountManagerException {
        String contextPath = MonnifyContextPath.AUTHENTICATION_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(monnifyUserName, monnifyPassword);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        Optional<AuthenticationResponse> optionalAuthenticationResponse = Optional.ofNullable(restTemplate.exchange(monnifyBaseUrl.concat(contextPath), HttpMethod.POST, httpEntity, AuthenticationResponse.class).getBody());
        if(optionalAuthenticationResponse.isPresent()) {
            AuthenticationResponse authenticationResponse = optionalAuthenticationResponse.get();
            if(authenticationResponse.requestSuccessful) {
                logger.info(authenticationResponse.getResponseBody().getAccessToken());
                tokenManagerRepository.save(new TokenManager("token", authenticationResponse.getResponseBody().getAccessToken()));
            } else {
                String errorMessage = authenticationResponse.getResponseMessage();
                throw new ZidoAccountManagerException(errorMessage);
            }
        }
    }

    private String getToken() throws TokenNotFoundException {
        String token = "";
        Optional<TokenManager> tokenManagerOptional = Optional.ofNullable(tokenManagerRepository.findById("token").orElseThrow(
                () -> new TokenNotFoundException("No Token Found in Database")));
        if(tokenManagerOptional.isPresent()) {
            token = tokenManagerOptional.get().getTokenId();
        }
        return token;
    }

    private ReserveAccountResponse reserveAccount(ReserveAccountRequest reserveAccountRequest) throws TokenNotFoundException, ZidoAccountManagerException {
        ReserveAccountResponse reserveAccountResponse;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            logger.info(new ObjectMapper().writeValueAsString(reserveAccountRequest));
        }catch (JsonProcessingException ex) {

        }
        String updatedAccountName = reserveAccountRequest.getAccountName().concat(reserveAccountIdentifier);
        logger.info(updatedAccountName);
        reserveAccountRequest.setAccountName(updatedAccountName);
        httpHeaders.set("Authorization", "Bearer ".concat(getToken()));
        HttpEntity<ReserveAccountRequest> httpEntity = new HttpEntity<>(reserveAccountRequest, httpHeaders);
        String contextPath = MonnifyContextPath.RESERVE_ACCOUNT_URL;
        logger.info(contextPath);
        logger.info(monnifyBaseUrl.concat(contextPath));
        Optional<ReserveAccountResponse> optionalReserveAccountResponse = Optional.ofNullable(restTemplate.exchange(monnifyBaseUrl.concat(contextPath), HttpMethod.POST, httpEntity, ReserveAccountResponse.class).getBody());
        logger.info(optionalReserveAccountResponse.toString());
        if(optionalReserveAccountResponse.isPresent()) {
            reserveAccountResponse = optionalReserveAccountResponse.get();
            if(!reserveAccountResponse.requestSuccessful) {
                throw new ZidoAccountManagerException(reserveAccountResponse.getResponseMessage());
            }
        } else {
            throw new ZidoAccountManagerException("Cannot Successfully Create A Reserve Account");
        }
        return reserveAccountResponse;
    }

    public void createExpense(ExpensesHelper expensesHelper) throws ZidoAccountManagerException {

        boolean expenseExist = expensesRepository.existsById(expensesHelper.getExpenseType().toUpperCase());
        if(!expenseExist) {
            Expenses expenses = new Expenses().setExpenseType(expensesHelper.getExpenseType().toUpperCase())
                    .setExpenseDescription(expensesHelper.getExpenseDescription());
            expensesRepository.save(expenses);
        } else {
            throw new ZidoAccountManagerException(MessageFormat.format("Expense with Id {0} already exist", expensesHelper.getExpenseType()));
        }
    }

    public List<ExpensesHelper> listExpenses() throws ZidoAccountManagerException {
        List<ExpensesHelper>  expensesHelperList = new ArrayList<>();
        List<Expenses> expensesList = expensesRepository.findAll();
        if(!expensesList.isEmpty()) {
            expensesList.forEach(expenses -> {
                ExpensesHelper expensesHelper = MapperModel.mapExpensesToHelper(expenses);
                expensesHelperList.add(expensesHelper);
            });
        } else {
            throw new ZidoAccountManagerException("No Expense Type Has Been Created");
        }
        return expensesHelperList;
    }

    public void createVehicleType(VehicleTypeHelper vehicleTypeHelper) throws ZidoAccountManagerException {

        boolean expenseExist = vehicleTypeRepository.existsById(vehicleTypeHelper.getVehicleType().toUpperCase());
        if(!expenseExist) {
            VehicleType vehicleType = new VehicleType().setVehicleType(vehicleTypeHelper.getVehicleType().toUpperCase())
                    .setVehicleDescription(vehicleTypeHelper.getVehicleDescription());
            vehicleTypeRepository.save(vehicleType);
        } else {
            throw new ZidoAccountManagerException(MessageFormat.format("Vehicle Type with Id {0} already Exist", vehicleTypeHelper.getVehicleType()));
        }
    }

    public List<VehicleTypeHelper> listVehicleTypes() throws ZidoAccountManagerException {
        List<VehicleTypeHelper>  vehicleTypeHelperList = new ArrayList<>();
        List<VehicleType> vehicleTypeList = vehicleTypeRepository.findAll();
        if(!vehicleTypeList.isEmpty()) {
            vehicleTypeList.forEach(vehicleType -> {
                VehicleTypeHelper vehicleTypeHelper = MapperModel.mapVehicleTypeToHelper(vehicleType);
                vehicleTypeHelperList.add(vehicleTypeHelper);
            });
        } else {
            throw new ZidoAccountManagerException("No Vehicle Type Has Been Created");
        }
        return vehicleTypeHelperList;
    }

    public void createVehicleMake(VehicleMakeRequest vehicleMakeRequest) throws ZidoAccountManagerException{

        boolean vehicleMakeExist = vehicleMakeRepository.existsById(vehicleMakeRequest.getVehicleMake().toUpperCase());
        if(!vehicleMakeExist) {
            Optional<VehicleType> optionalVehicleType = vehicleTypeRepository.findById(vehicleMakeRequest.getVehicleTypeId().toUpperCase());
            if(optionalVehicleType.isPresent()) {
                VehicleType vehicleType = optionalVehicleType.get();
                VehicleMake vehicleMake = new VehicleMake().setVehicleMake(vehicleMakeRequest.getVehicleMake().toUpperCase())
                        .setVehicleManufacturer(vehicleMakeRequest.getVehicleManufacturer())
                        .setVehicleType(vehicleType);
                vehicleMakeRepository.save(vehicleMake);
            } else {
                throw new ZidoAccountManagerException(MessageFormat.format("Vehicle Type with Id {0} Does Not Exist", vehicleMakeRequest.getVehicleMake()));
            }
        } else {
            throw new ZidoAccountManagerException(MessageFormat.format("Vehicle Make With Id {0} Already Exist", vehicleMakeRequest.getVehicleTypeId().toUpperCase()));
        }
    }

    public List<VehicleMakeHelperResponse> listVehicleMakes() throws ZidoAccountManagerException {
        List<VehicleMakeHelperResponse> vehicleMakeHelperResponseList = new ArrayList<>();
        List<VehicleMake> vehicleMakeList = vehicleMakeRepository.findAll();
        if(!vehicleMakeList.isEmpty()) {
            vehicleMakeList.forEach(vehicleMake -> {
                VehicleMakeHelperResponse vehicleMakeHelperResponse = MapperModel.mapVehicleMakeToHelper(vehicleMake);
                vehicleMakeHelperResponseList.add(vehicleMakeHelperResponse);
            });
        } else {
            throw new ZidoAccountManagerException("No Vehicle Make Has Been Created");
        }
        return vehicleMakeHelperResponseList;
    }

    public void createClientDetail(ClientDetailHelper clientDetailHelper) throws ZidoAccountManagerException{

        List<ClientDetail> clientDetailList = clientDetailRepository.findByContactPhoneNumber(clientDetailHelper.getContactPhoneNumber());
        if(clientDetailList.isEmpty()) {
            ClientDetail clientDetail = new ClientDetail().setClientAddress(clientDetailHelper.getClientAddress())
                    .setClientName(clientDetailHelper.getClientName())
                    .setContactPersonName(clientDetailHelper.getContactPersonName())
                    .setContactPhoneNumber(clientDetailHelper.getContactPhoneNumber());
            clientDetailRepository.save(clientDetail);
        } else {
            throw new ZidoAccountManagerException(MessageFormat.format("Client with Contact Phone Number {0} already exist", clientDetailHelper.getContactPhoneNumber()));
        }
    }

    public List<ClientDetailHelper> listClientDetails() throws ZidoAccountManagerException {
        List<ClientDetailHelper>  clientDetailHelperList = new ArrayList<>();
        List<ClientDetail> clientDetailList = clientDetailRepository.findAll();
        if(!clientDetailList.isEmpty()) {
            clientDetailList.forEach(clientDetail -> {
                ClientDetailHelper clientDetailHelper = MapperModel.mapClientDetailToHelper(clientDetail);
                clientDetailHelperList.add(clientDetailHelper);
            });
        } else {
            throw new ZidoAccountManagerException("No Client Has Been Created");
        }
        return clientDetailHelperList;
    }

    public void createVehicleDetail(VehicleDetailHelperRequest vehicleDetailHelperRequest) throws ZidoAccountManagerException{

        boolean vehicleDetailExist = vehicleDetailRepository.existsByVehicleNumber(vehicleDetailHelperRequest.getVehicleNumber());
        if(!vehicleDetailExist) {
            Optional<VehicleMake> optionalVehicleMake = Optional.ofNullable(vehicleMakeRepository.findById(vehicleDetailHelperRequest.getVehicleMakeId().toUpperCase()).orElseThrow(
                    () -> new ZidoAccountManagerException(MessageFormat.format("Vehicle Make with Id {0} Does Not Exist", vehicleDetailHelperRequest.getVehicleMakeId().toUpperCase()))
            ));
            if(optionalVehicleMake.isPresent()) {
                Optional<Transporter> optionalTransporter = Optional.ofNullable(transporterRepository.findById(vehicleDetailHelperRequest.getTransporterId()).orElseThrow(
                        () -> new ZidoAccountManagerException(MessageFormat.format("Transporter with Id {0} Does Not Exist", vehicleDetailHelperRequest.getTransporterId().toString()))
                ));
                if(optionalTransporter.isPresent()) {
                    Transporter transporter = optionalTransporter.get();
                    VehicleMake vehicleMake = optionalVehicleMake.get();
                    VehicleDetail vehicleDetail = new VehicleDetail().setVehicleMake(vehicleMake)
                            .setVehicleNumber(vehicleDetailHelperRequest.getVehicleNumber())
                            .setChasisNumber(vehicleDetailHelperRequest.getChasisNumber())
                            .setDriverAssignedStatus(false)
                            .setEngineNumber(vehicleDetailHelperRequest.getEngineNumber())
                            .setTonnage(vehicleDetailHelperRequest.getTonnage())
                            .setTransporter(transporter)
                            .setYearOfManufacture(vehicleDetailHelperRequest.getYearOfManufacture())
                            .setYearOfPurchase(vehicleDetailHelperRequest.getYearOfPurchase());
                    vehicleDetailRepository.save(vehicleDetail);
                }
            }
        } else {
            throw new ZidoAccountManagerException(MessageFormat.format("Vehicle Detail With Plate Number {0} Already Exist", vehicleDetailHelperRequest.getVehicleNumber()));
        }
    }

    public List<VehicleDetailHelperResponse> listVehicleDetails() throws ZidoAccountManagerException {
        List<VehicleDetailHelperResponse> vehicleDetailHelperResponseList = new ArrayList<>();
        List<VehicleDetail> vehicleDetailList = vehicleDetailRepository.findAll();
        if(!vehicleDetailList.isEmpty()) {
            vehicleDetailList.forEach(vehicleDetail -> {
                VehicleDetailHelperResponse vehicleDetailHelperResponse = MapperModel.mapVehicleDetailToHelper(vehicleDetail);
                vehicleDetailHelperResponseList.add(vehicleDetailHelperResponse);
            });
        } else {
            throw new ZidoAccountManagerException("No Vehicle Detail Has Been Created");
        }
        return vehicleDetailHelperResponseList;
    }

    public void createTransporter(TransporterHelperRequest transporterHelperRequest) throws TokenNotFoundException, ZidoAccountManagerException {
        boolean transporterExist = transporterRepository.existsByTransporterEmailAddress(transporterHelperRequest.getTransporterEmailAddress());
        if(!transporterExist) {
            Transporter transporter = new Transporter().setTransporterAddress(transporterHelperRequest.getTransporterAddress())
                    .setTransporterEmailAddress(transporterHelperRequest.getTransporterEmailAddress())
                    .setTransporterName(transporterHelperRequest.getTransporterName())
                    .setTransporterPhoneNumber(transporterHelperRequest.getTransporterPhoneNumber())
                    .setTransporterStatus(false)
                    .setAccountStatus(false);
            transporterRepository.save(transporter);
        } else {
            throw new ZidoAccountManagerException(MessageFormat.format("Transporter With Email {0} Already Exist", transporterHelperRequest.getTransporterEmailAddress()));
        }

    }

    public TransporterHelperResponse addAccountDetailsToTransporter(Long transporterId) throws ZidoAccountManagerException, TokenNotFoundException {
        TransporterHelperResponse transporterHelperResponse;
        Transporter transporter = transporterRepository.findById(transporterId).orElseThrow(
                    () -> new ZidoAccountManagerException(MessageFormat.format("Could Not Find Transporter With Id {0}", transporterId.toString())
                ));
            ReserveAccountRequest reserveAccountRequest = new ReserveAccountRequest().setAccountName(transporter.getTransporterName())
                    .setAccountReference(transporter.getTransporterPhoneNumber())
                    .setContractCode(contractCode)
                    .setCurrencyCode(currencyCode)
                    .setCustomerEmail(transporter.getTransporterEmailAddress())
                    .setIncomeSplitConfig(new ArrayList<>());
            ReserveAccountResponse reserveAccountResponse = this.reserveAccount(reserveAccountRequest);
            if (reserveAccountResponse.isRequestSuccessful()) {
                ResponseBody responseBody = reserveAccountResponse.getResponseBody();
                transporter.setBankCode(responseBody.getBankCode())
                        .setAccountStatus(true)
                        .setAccountName(responseBody.getAccountName())
                        .setAccountNumber(responseBody.getAccountNumber())
                        .setAccountReference(responseBody.getAccountReference())
                        .setBankName(responseBody.getBankName())
                        .setCurrencyCode(responseBody.getCurrencyCode())
                        .setReservationReference(responseBody.getReservationReference())
                        .setTransporterStatus(true);
                transporterRepository.save(transporter);
                transporterHelperResponse = MapperModel.mapTransporterToHelper(transporter);
            } else {
                throw new ZidoAccountManagerException(MessageFormat.format("Could Not Generate Account Details for Transporter With Email {0}", transporter.getTransporterEmailAddress()));
            }
        return transporterHelperResponse;
    }

    public List<VehicleDetailHelperResponse> listVehiclesByStatus(boolean driverAssignedStatus) {
        List<VehicleDetailHelperResponse> vehicleDetailHelperResponseList = new ArrayList<>();
        List<VehicleDetail> vehicleDetailList = vehicleDetailRepository.findByDriverAssignedStatus(driverAssignedStatus);
        if(!vehicleDetailList.isEmpty()) {
            vehicleDetailList.forEach(vehicleDetail -> {
                VehicleDetailHelperResponse vehicleDetailHelperResponse = MapperModel.mapVehicleDetailToHelper(vehicleDetail);
                vehicleDetailHelperResponseList.add(vehicleDetailHelperResponse);
            });
        }
        return vehicleDetailHelperResponseList;
    }

    public List<VehicleDetailHelperResponse> listAllVehicles() {
        List<VehicleDetailHelperResponse> vehicleDetailHelperResponseList = new ArrayList<>();
        List<VehicleDetail> vehicleDetailList = vehicleDetailRepository.findAll();
        if(!vehicleDetailList.isEmpty()) {
            vehicleDetailList.forEach(vehicleDetail -> {
                VehicleDetailHelperResponse vehicleDetailHelperResponse = MapperModel.mapVehicleDetailToHelper(vehicleDetail);
                vehicleDetailHelperResponseList.add(vehicleDetailHelperResponse);
            });
        }
        return vehicleDetailHelperResponseList;
    }

    public void createDriverInformation(DriverInformationHelperRequest driverInformationHelperRequest) throws ZidoAccountManagerException {
        boolean driverLicenseExist = driverInformationRepository.existsByDriverLicenceNumber(driverInformationHelperRequest.getDriverLicenceNumber());
        if(!driverLicenseExist) {
            DriverInformation driverInformation = new DriverInformation().setDriverAddress(driverInformationHelperRequest.getDriverAddress())
                    .setDriverLicenceNumber(driverInformationHelperRequest.getDriverLicenceNumber())
                    .setDriverName(driverInformationHelperRequest.getDriverName())
                    .setDateOfEmployment(driverInformationHelperRequest.getDateOfEmployment())
                    .setGuarantorAddress(driverInformationHelperRequest.getGuarantorAddress())
                    .setGuarantorName(driverInformationHelperRequest.getGuarantorName())
                    .setGuarantorPhoneNumber(driverInformationHelperRequest.getGuarantorPhoneNumber())
                    .setNextOfKinAddress(driverInformationHelperRequest.getNextOfKinAddress())
                    .setNextOfKinName(driverInformationHelperRequest.getNextOfKinName())
                    .setNextOfKinPhoneNumber(driverInformationHelperRequest.getNextOfKinPhoneNumber())
                    .setNextOfKinRelationship(driverInformationHelperRequest.getNextOfKinRelationship())
                    .setVerificationStatus(false)
                    .setVehicleDetail(null);
            driverInformationRepository.save(driverInformation);

        } else {
            throw new ZidoAccountManagerException(MessageFormat.format("Driver License {0} Record Already Exist", driverInformationHelperRequest.getDriverLicenceNumber()));
        }
    }

    public void addDriverInformationToVehicle(Long driverInformationId, Long vehicleDetailId) throws ZidoAccountManagerException {
        VehicleDetail vehicleDetail = vehicleDetailRepository.findById(vehicleDetailId).orElseThrow(
                () -> new ZidoAccountManagerException(MessageFormat.format("Vehicle Id {0} Does Not Exist", vehicleDetailId))
        );
        DriverInformation driverInformation = driverInformationRepository.findById(driverInformationId).orElseThrow(
                () -> new ZidoAccountManagerException(MessageFormat.format("Driver Information with Id {0} Does Not Exist", driverInformationId))
        );
        driverInformation.setVehicleDetail(vehicleDetail).setVerificationStatus(true);
        driverInformationRepository.save(driverInformation);
    }

    public void createTripDetails(TripDetailHelperRequest tripDetailHelperRequest) throws ZidoAccountManagerException {
        DriverInformation driverInformation = driverInformationRepository.findById(tripDetailHelperRequest.getDriverInformationId()).orElseThrow(
                () -> new ZidoAccountManagerException(MessageFormat.format("Driver Information With Id {0} Does Not Exist", tripDetailHelperRequest.getDriverInformationId()))
        );
        TripDetail tripDetail = new TripDetail().setDateOfRequest(tripDetailHelperRequest.getDateOfRequest())
                .setDestinationAddress(tripDetailHelperRequest.getDestinationAddress())
                .setDriverInformation(driverInformation)
                .setInvoiceAmount(tripDetailHelperRequest.getInvoiceAmount())
                .setPickUpLocationAddress(tripDetailHelperRequest.getPickUpLocationAddress())
                .setWayBillNumber(tripDetailHelperRequest.getWayBillNumber());
        tripDetailRepository.save(tripDetail);
    }

    public List<TripDetailHelperResponse> getAllTripDetails() {
        List<TripDetailHelperResponse> tripDetailHelperResponseList = new ArrayList<>();
        List<TripDetail> tripDetailList = tripDetailRepository.findAll();
        if(!tripDetailList.isEmpty()) {
            tripDetailList.forEach(tripDetail -> {
                TripDetailHelperResponse tripDetailHelperResponse = MapperModel.mapTripDetailToHelper(tripDetail);
                tripDetailHelperResponseList.add(tripDetailHelperResponse);
            });
        }
        return tripDetailHelperResponseList;
    }

    public void createTripExpenses(TripExpenseDetailsHelperRequest tripExpenseDetailsHelperRequest) throws ZidoAccountManagerException {
        TripDetail tripDetail = tripDetailRepository.findById(tripExpenseDetailsHelperRequest.getTripDetailId()).orElseThrow(
                () -> new ZidoAccountManagerException(MessageFormat.format("Trip Detail With Id {0} Does Not Exist", tripExpenseDetailsHelperRequest.getTripDetailId()))
        );
        List<TripExpenseInformation> tripExpenseInformationList = tripExpenseDetailsHelperRequest.getTripExpenseInformationList();
        if(!tripExpenseInformationList.isEmpty()) {
            tripExpenseInformationList.forEach(tripExpenseInformation -> {
                String expenseId = tripExpenseInformation.getExpenseId();
                Expenses expenses = null;
                try {
                    expenses = expensesRepository.findById(expenseId).orElseThrow(
                            () -> new ZidoAccountManagerException(MessageFormat.format("Expense Id {0} Does Not Exist", expenseId))
                    );
                } catch (ZidoAccountManagerException e) {
                    e.printStackTrace();
                }
                TripExpenseDetails tripExpenseDetails = new TripExpenseDetails().setExpense(expenses)
                        .setExpenseAmount(tripExpenseInformation.getExpenseAmount())
                        .setTripDetail(tripDetail);
                tripExpenseRepository.save(tripExpenseDetails);
            });
        }
    }

    public List<TripExpenseDetailsHelperResponse> getAllTripExpenses() {
        List<TripExpenseDetailsHelperResponse> tripExpenseDetailsHelperResponseList = new ArrayList<>();
        List<TripExpenseDetails> tripExpenseDetailsList = tripExpenseRepository.findAll();
        if(!tripExpenseDetailsList.isEmpty()) {
            tripExpenseDetailsList.forEach(tripExpenseDetails -> {
                TripExpenseInformation tripExpenseInformation = new TripExpenseInformation().setExpenseAmount(tripExpenseDetails.getExpenseAmount())
                        .setExpenseId(tripExpenseDetails.getExpense().getExpenseType());
                TripExpenseDetailsHelperResponse tripExpenseDetailsHelperResponse = new TripExpenseDetailsHelperResponse().setTripDetailHelperResponse(MapperModel.mapTripDetailToHelper(tripExpenseDetails.getTripDetail()))
                        .setTripExpenseInformation(tripExpenseInformation);
                tripExpenseDetailsHelperResponseList.add(tripExpenseDetailsHelperResponse);
            });
        }
        return tripExpenseDetailsHelperResponseList;
    }

    public ReserveAccountResponse deallocateAccount(String accountNumber) throws TokenNotFoundException, ZidoAccountManagerException {
        ReserveAccountResponse reserveAccountResponse;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        httpHeaders.set("Authorization", "Bearer ".concat(getToken()));
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        String contextPath = MonnifyContextPath.DEALLOCATE_ACCOUNT_URL;
        logger.info(contextPath);
        logger.info(monnifyBaseUrl.concat(contextPath));
        Optional<ReserveAccountResponse> optionalReserveAccountResponse = Optional.ofNullable(restTemplate.exchange(monnifyBaseUrl.concat(contextPath), HttpMethod.DELETE, httpEntity, ReserveAccountResponse.class, accountNumber).getBody());
        logger.info(optionalReserveAccountResponse.toString());
        if(optionalReserveAccountResponse.isPresent()) {
            reserveAccountResponse = optionalReserveAccountResponse.get();
            if(!reserveAccountResponse.requestSuccessful) {
                throw new ZidoAccountManagerException(reserveAccountResponse.getResponseMessage());
            }
        } else {
            throw new ZidoAccountManagerException(MessageFormat.format("Cannot Successfully DeAllocate Account with Account Number {0}", accountNumber));
        }
        return reserveAccountResponse;
    }

    public List<TransporterDetailHelper> listTransportersNotMappedToAccount() {
        List<TransporterDetailHelper> transporterDetailHelperList = new ArrayList<>();
        List<Transporter> transporterList = transporterRepository.findByTransporterStatusFalseAndAccountStatusFalse();
        if(!transporterList.isEmpty()) {
            transporterList.forEach(transporter -> {
                TransporterDetailHelper transporterDetailHelper = new TransporterDetailHelper().setTransporterAddress(transporter.getTransporterAddress())
                        .setTransporterEmailAddress(transporter.getTransporterEmailAddress())
                        .setTransporterName(transporter.getTransporterName())
                        .setTransporterPhoneNumber(transporter.getTransporterPhoneNumber())
                        .setId(transporter.getId());
                transporterDetailHelperList.add(transporterDetailHelper);
            });
        }
        return transporterDetailHelperList;
    }

    public List<TransporterHelperResponse> listAllTransporters() {
        List<TransporterHelperResponse> transporterHelperResponseList = new ArrayList<>();
        List<Transporter> transporterList = transporterRepository.findAll();
        if(!transporterList.isEmpty()) {
            transporterList.forEach(transporter -> {
                TransporterHelperResponse transporterHelperResponse = new TransporterHelperResponse().setTransporterAddress(transporter.getTransporterAddress())
                        .setTransporterEmailAddress(transporter.getTransporterEmailAddress())
                        .setTransporterName(transporter.getTransporterName())
                        .setTransporterPhoneNumber(transporter.getTransporterPhoneNumber())
                        .setTransporterStatus(transporter.isTransporterStatus())
                        .setAccountName(transporter.getAccountName())
                        .setAccountNumber(transporter.getAccountNumber())
                        .setAccountReference(transporter.getAccountReference())
                        .setAccountStatus(transporter.isAccountStatus())
                        .setBankCode(transporter.getBankCode())
                        .setBankName(transporter.getBankName())
                        .setCurrencyCode(transporter.getCurrencyCode())
                        .setReservationReference(transporter.getReservationReference());
                transporterHelperResponseList.add(transporterHelperResponse);

            });
        }
        return transporterHelperResponseList;
    }
}
