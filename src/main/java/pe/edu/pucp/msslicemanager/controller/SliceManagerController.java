package pe.edu.pucp.msslicemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.msslicemanager.dao.SliceDao;
import pe.edu.pucp.msslicemanager.entity.SliceManager;
import pe.edu.pucp.msslicemanager.entity.User;
import pe.edu.pucp.msslicemanager.exception.SliceActiveException;
import pe.edu.pucp.msslicemanager.exception.SliceNotFoundException;
import pe.edu.pucp.msslicemanager.repository.SliceManagerRepository;
import pe.edu.pucp.msslicemanager.repository.UserRepository;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/ms-slice-manager")
@CrossOrigin
public class SliceManagerController {

    @Autowired
    SliceManagerRepository sliceManagerRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createSlice(@Valid @RequestBody SliceDao sliceDao){
        Map<String, String> response = new HashMap<>();
        sliceDao.getSlice().setStatus(false);
        Integer newSliceId = sliceManagerRepository.save(sliceDao.getSlice()).getSliceId();
        response.put("status", HttpStatus.CREATED.getReasonPhrase());
        response.put("message", "Slice created successfully.");
        response.put("sliceId", String.valueOf(newSliceId));
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editSlice(@Valid @RequestBody SliceDao sliceDao){
        Map<String, String> response = new HashMap<>();

        Optional<SliceManager> sliceOpt = sliceManagerRepository.findById(sliceDao.getSlice().getSliceId());

        if(sliceOpt.isPresent()){
            SliceManager slice = sliceOpt.get();
            sliceDao.getSlice().setStatus(slice.getStatus());
        }

        Integer newSliceId = sliceManagerRepository.save(sliceDao.getSlice()).getSliceId();
        response.put("status", HttpStatus.OK.getReasonPhrase());
        response.put("message", "Slice edited successfully.");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/assign/{userId}")
    public ResponseEntity assignSliceToUser(@PathVariable("userId") String userId, @RequestParam(value = "sliceId", required = false) String sliceId){
        Map<String, String> response = new HashMap<>();

        Optional<User> userOptional = userRepository.findById(Integer.valueOf(userId));

        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(sliceId != null) {
                user.setSliceId(Integer.valueOf(sliceId));
            }else{
                user.setSliceId(null);
            }
            userRepository.save(user);
        }
        response.put("status", HttpStatus.OK.getReasonPhrase());
        response.put("message", "Slice N°" + sliceId + " assigned successfully to user "+ userId +".");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listAllSlice() {
        return new ResponseEntity(sliceManagerRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/list-by-id/{sliceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listSliceById(@PathVariable(value = "sliceId", required = false) String sliceId) {

        Optional<SliceManager> sliceOpt = sliceManagerRepository.findById(Integer.valueOf(sliceId));
        if(sliceOpt.isPresent()){
            return new ResponseEntity(sliceOpt.get(), HttpStatus.OK);
        }else{
            throw new SliceNotFoundException("SliceNotFoundException: slice not found.");
        }
    }
    @GetMapping(value = "/list-by-username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listSliceByUsername(@PathVariable(value = "username", required = false) String username) {
        return new ResponseEntity(sliceManagerRepository.findByUsername(username), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{sliceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteSliceById(@PathVariable("sliceId") String sliceId){
        Map<String, String> response = new HashMap<>();
        Optional<SliceManager> sliceManagerOptional = sliceManagerRepository.findById(Integer.valueOf(sliceId));
        if(sliceManagerOptional.isPresent()){
            SliceManager sliceManager = sliceManagerOptional.get();
            if(!sliceManager.getStatus()){
                List<User> users = userRepository.findBySliceId(Integer.valueOf(sliceId));

                for(User user: users){
                    user.setSliceId(null);
                    userRepository.save(user);
                }

                sliceManagerRepository.deleteById(Integer.valueOf(sliceId));
                response.put("status", HttpStatus.OK.getReasonPhrase());
                response.put("message", "Slice N°" + sliceId + " deleted successfully.");
                return new ResponseEntity(response, HttpStatus.OK);
            }else{
                throw new SliceActiveException("SliceActiveException: slice can not be deleted because is on state ACTIVE");
            }
        }else{
            throw new SliceNotFoundException("SliceNotFoundException: slice not found.");
        }
    }

}
