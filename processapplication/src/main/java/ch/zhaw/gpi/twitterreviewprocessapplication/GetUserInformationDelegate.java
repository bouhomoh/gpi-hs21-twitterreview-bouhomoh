package ch.zhaw.gpi.twitterreviewprocessapplication;

import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import camundajar.impl.com.google.gson.JsonObject;
import camundajar.impl.com.google.gson.JsonParser;

@Named("getUserInformationAdapter")
public class GetUserInformationDelegate implements JavaDelegate {

    @Bean(name = "activeDirectoryRestclient")
    public RestTemplate getActiveDirectoryRestClient(RestTemplateBuilder builder)
    {
        return builder.build();
    }

    @Autowired
    private RestTemplate activeDirectoryRestclient;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String userName = (String) execution.getVariable("anfrageStellenderBenutzer");
        
        String fullUserDescription = "";
        String eMail = "";
        String phoneNumber = "";
        String notificationChannel = "";

        try
        {
        ResponseEntity<UserRepresentation> userResponse = activeDirectoryRestclient.exchange("http://localhost:8070/api/users/{username}", HttpMethod.GET, null, UserRepresentation.class, userName);
            UserRepresentation user = userResponse.getBody();
            eMail = user.geteMail();
            phoneNumber = user.getPhoneNumber();
            notificationChannel = user.getNotificationChannel();
            
            String homeOrgUri = user.get_links().getHomeOrganization().getHref();
            ResponseEntity<String> homeOrgResponse = activeDirectoryRestclient.exchange(homeOrgUri, HttpMethod.GET, null, String.class);
            JsonObject homeOrgResponseAsJson = new JsonParser().parse(homeOrgResponse.getBody()).getAsJsonObject();
            String homeOrgLongName = homeOrgResponseAsJson.get("homeorg").getAsString();
            fullUserDescription = user.getFirstName() + " " + user.getOfficialName() + " (" + homeOrgLongName + ")";
        }
        catch(Exception e)
        {
            eMail = "alien@universe.os";
            fullUserDescription = "Alien (Universe)";    
        }

        execution.setVariable("userMail", eMail);
        execution.setVariable("userFullDescription", fullUserDescription);
        execution.setVariable("userPhoneNumber", phoneNumber);
        execution.setVariable("userNotificationChannel", notificationChannel);
    }
    
}
