package com.vaccinesoap.main.endpoint;

import com.vaccinesoap.gs_ws.AddVaccineRequest;
import com.vaccinesoap.gs_ws.AddVaccineResponse;
import com.vaccinesoap.gs_ws.DeleteVaccineRequest;
import com.vaccinesoap.gs_ws.DeleteVaccineResponse;
import com.vaccinesoap.gs_ws.GetAllVaccineResponse;
import com.vaccinesoap.gs_ws.GetVaccineByIdRequest;
import com.vaccinesoap.gs_ws.GetVaccineByIdResponse;
import com.vaccinesoap.gs_ws.SearchRequest;
import com.vaccinesoap.gs_ws.SearchResponse;
import com.vaccinesoap.gs_ws.ServiceStatus;
import com.vaccinesoap.gs_ws.UpdateVaccineRequest;
import com.vaccinesoap.gs_ws.UpdateVaccineResponse;
import com.vaccinesoap.gs_ws.VaccineInfo;
import com.vaccinesoap.main.model.VaccineEntity;
import com.vaccinesoap.main.search.SearchHibernate;
import com.vaccinesoap.main.service.VaccineService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class VaccineEndPoint {
    public static final String NAMESPACE_URI = "http://localhost:8080/soapws/vaccines.wsdl";

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private SearchHibernate searchHibernateRepository;

    public final static Logger logger = LoggerFactory
            .getLogger(VaccineEndPoint.class);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllVaccineRequest")
    @ResponsePayload
    public GetAllVaccineResponse getAllVaccines() {
        GetAllVaccineResponse response = new GetAllVaccineResponse();
        List<VaccineInfo> vaccineInfoList = new ArrayList<>();
        List<VaccineEntity> vaccineEntities = vaccineService.getAllVaccines();
        for (int i = 0; i < vaccineEntities.size(); i++) {
            VaccineInfo ob = new VaccineInfo();
            BeanUtils.copyProperties(vaccineEntities.get(i), ob);
            vaccineInfoList.add(ob);
        }
        response.getVaccineInfo().addAll(vaccineInfoList);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getVaccineByIdRequest")
    @ResponsePayload
    public GetVaccineByIdResponse getVaccine(@RequestPayload GetVaccineByIdRequest request) {
        GetVaccineByIdResponse response = new GetVaccineByIdResponse();
        VaccineInfo vaccineInfo = new VaccineInfo();
        BeanUtils.copyProperties(vaccineService.getVaccineById(request.getId()), vaccineInfo);
        response.setVaccineInfo(vaccineInfo);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addVaccineRequest")
    @ResponsePayload
    public AddVaccineResponse addVaccine(@RequestPayload AddVaccineRequest request) {
        AddVaccineResponse response = new AddVaccineResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        VaccineEntity vaccineEntity = new VaccineEntity();
        vaccineEntity.setName(request.getName());
        vaccineEntity.setEmail(request.getEmail());
        vaccineEntity.setPhone(request.getPhone());
        vaccineEntity.setModel(request.getModel());
        vaccineEntity.setSerialNumber(request.getSerialNumber());
        vaccineEntity.setPurchasePrice(request.getPurchasePrice());
        boolean flag = vaccineService.addVaccine(vaccineEntity);
        if ( !flag ) {
            serviceStatus.setStatusCode("CONFLICT");
            serviceStatus.setMessage("Content Already Available");
            response.setServiceStatus(serviceStatus);
        } else {
            VaccineInfo vaccineInfo = new VaccineInfo();
            BeanUtils.copyProperties(vaccineEntity, vaccineInfo);
            response.setVaccineInfo(vaccineInfo);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Added Successfully");
            response.setServiceStatus(serviceStatus);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateVaccineRequest")
    @ResponsePayload
    public UpdateVaccineResponse updateVaccine(@RequestPayload UpdateVaccineRequest request) {
        VaccineEntity vaccineEntity = new VaccineEntity();
        BeanUtils.copyProperties(request.getVaccineInfo(), vaccineEntity);
        vaccineService.updateVaccine(vaccineEntity);
        ServiceStatus serviceStatus = new ServiceStatus();
        serviceStatus.setStatusCode("SUCCESS");
        serviceStatus.setMessage("Content Updated Successfully");
        UpdateVaccineResponse response = new UpdateVaccineResponse();
        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteVaccineRequest")
    @ResponsePayload
    public DeleteVaccineResponse deleteVaccine(@RequestPayload DeleteVaccineRequest request) {
        VaccineEntity vaccineEntity = vaccineService.getVaccineById(request.getId());
        ServiceStatus serviceStatus = new ServiceStatus();
        if ( vaccineEntity == null ) {
            serviceStatus.setStatusCode("FAIL");
            serviceStatus.setMessage("Content Not Available");
        } else {
            vaccineService.deleteVaccine(vaccineEntity);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Deleted Successfully");
        }
        DeleteVaccineResponse response = new DeleteVaccineResponse();
        response.setServiceStatus(serviceStatus);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "searchVaccineRequest")
    @ResponsePayload
    public SearchResponse searchResponse(SearchRequest request , Model model){
            //List<VaccineEntity> searchResults = null;
            SearchResponse searchResponse = new SearchResponse();
            VaccineInfo vaccineInfo = new VaccineInfo();

            try {
                //searchResults = searchRepository.search(request.getText());
                BeanUtils.copyProperties(searchHibernateRepository.search(request.getText()),vaccineInfo);
            }
            catch (Exception ex) {
            }
            searchResponse.setVaccineInfo(vaccineInfo);
            model.addAttribute("searchResults", searchResponse);
            return searchResponse;
        }
    }

