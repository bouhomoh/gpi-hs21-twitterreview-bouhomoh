package ch.zhaw.gpi.twitterreviewprocessapplication;

import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import camundajar.impl.com.google.gson.JsonObject;

@Named("postUserAdapter")
public class PostUserDelegate implements JavaDelegate {

    @Autowired
    private RestTemplate activeDirectoryRestclient;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String userName = (String) execution.getVariable("userName");
        String firstName = (String) execution.getVariable("firstName");
        String homeOrg = (String) execution.getVariable("homeorg");
        
        JsonObject userAsJson = new JsonObject();
        userAsJson.addProperty("firstName", firstName);
        userAsJson.addProperty("userName", userName);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<String>(userAsJson.toString(), headers);

        try {
            activeDirectoryRestclient.exchange("http://localhost:8070/api/users", HttpMethod.POST, httpEntity, Void.class);

            headers.setContentType(MediaType.valueOf("text/uri-list"));
            httpEntity = new HttpEntity<String>("http://localhost:8070/api/orgUnits/gl"+homeOrg, headers);
            activeDirectoryRestclient.exchange("http://localhost:8070/api/users/"+userName+"/homeOrganization", HttpMethod.PUT, httpEntity, Void.class);
        } catch (Exception e) {
            throw e;
        }
    }
    
}
