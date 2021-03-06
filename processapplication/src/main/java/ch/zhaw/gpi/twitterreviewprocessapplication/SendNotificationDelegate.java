package ch.zhaw.gpi.twitterreviewprocessapplication;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import com.sendgrid.helpers.mail.objects.Email;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

@Named("sendNotificationAdapter")
public class SendNotificationDelegate implements JavaDelegate {

    @Autowired
    private SendGridClient sendGridClient;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String email = (String) execution.getVariable("userMail");
        //String phoneNumber = (String) execution.getVariable("userPhoneNumber");
        String notificationChannel = (String) execution.getVariable("userNotificationChannel");
        String checkResult = (String) execution.getVariable("checkResult");
        String checkResultComment = (String) execution.getVariable("checkResultComment");
        String tweetContent = (String) execution.getVariable("tweetContent");

        Boolean accepted = checkResult.equals("accepted");
        String content = "Die folgende Tweet-Anfrage wurde " + (accepted ? "angenommen" : "abgelehnt") + ": '"
                + tweetContent + "'" + (accepted ? "" : " Die Begründung für die Ablehnung ist: " + checkResultComment);

        switch (notificationChannel) {
            case "email":
                Map<String, Object> templateData = new HashMap<>();
                templateData.put("content", content);
                sendGridClient.sendHtmlMail(new Email(email), new Email("bouhomoh@students.zhaw.ch"),
                        "Tweet-Anfrage '" + tweetContent.substring(0, 15) + "' "
                                + (accepted ? "angenommen" : "abgelehnt"),
                        "d-271e4b8830554b849f07f1c56bd2aa05", templateData);
                break;
            case "sms":
                //twilioClient.sendSms(phoneNumber, content);
                break;
            case "voice":
                //twilioClient.notifyViaCall(phoneNumber, content);
                break;
            default:
                throw new Exception("UnsupportedNotificationChannel");
        }

    }

}
