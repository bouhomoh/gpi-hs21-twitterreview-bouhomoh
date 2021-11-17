package ch.zhaw.gpi.admock;

import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
public class CustomRestController {
    
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="/customapi/users/{username}", method=RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable String username) {
        Optional<User> optionalUser = userRepository.findById(username);

        if(optionalUser.isPresent())
        {
            return new ResponseEntity<User>(optionalUser.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
}
