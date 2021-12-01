package com.example.userapp.controller;
import java.io.InputStream;
import java.util.*;

import com.example.userapp.repository.UserRepository;
import com.example.userapp.model.User;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.ResultIterator;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    ResponseEntity<Object> all(@RequestParam(defaultValue = "0") String min, @RequestParam(defaultValue = "4000") String max,
                   @RequestParam(defaultValue = "0") String offset, @RequestParam(defaultValue = "1000000") String limit,
                               @RequestParam(required = false) String sort) {
        Map<String, List<User>> map = new HashMap<>();
        String error_message = "";
        try{
            float min_float = Float.parseFloat(min);
            float max_float = Float.parseFloat(max);
            int off_set_int =  Integer.parseInt(offset);
            int limit_int = Integer.parseInt(limit); //By default, some large number.
            int sort_int = 0;
            if(sort!= null && sort.equalsIgnoreCase("name")){
                sort_int = 1;
            }else if(sort!= null && sort.equalsIgnoreCase("salary")){
                sort_int = 2;
            }else if(sort!=null){
                error_message = "Illegal Sorting Parameter";
                throw new Exception(error_message);
            }
            if(sort_int != 0){
                List <User> users = repository.returnAllSorted(min_float, max_float,sort_int,limit_int, off_set_int);
                map.put("results",users);
            }else{
                List <User> users = repository.returnAll(min_float, max_float,limit_int, off_set_int);
                map.put("results",users);
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch(Exception e){
            Map<String, String> error_map = new HashMap<>();
            error_map.put("error",e.getMessage());
            return new ResponseEntity<>(error_map, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload")
    ResponseEntity<Object> uploadData(@RequestParam("file") MultipartFile file) throws Exception {
        Map<String, Integer> map = new HashMap<>();
        try {
            List<User> userList = new ArrayList<>();
            InputStream inputStream = file.getInputStream();
            CsvParserSettings setting = new CsvParserSettings();
            setting.setHeaderExtractionEnabled(true);
            CsvParser parser = new CsvParser(setting);
            List<Record> parseAllRecords = parser.parseAllRecords(inputStream);
            ResultIterator<Record, ParsingContext> iterator = parser.iterateRecords(inputStream).iterator();
            //Check that it has the correct number of columns.
            if(iterator.getContext().parsedHeaders().length != 2){
                throw new Exception("Invalid file");
            }
            for (Record parseAllRecord : parseAllRecords) {
                User u = new User();
                u.setName(parseAllRecord.getString("name"));
                float salary_csv = Float.parseFloat(parseAllRecord.getString("salary")); //Throws exception if not float type
                if(salary_csv >= 0.0){ // Check if greater than 0, if not skip.
                    u.setSalary(salary_csv);
                }else{
                    continue;
                }
                u.setSalary(salary_csv);
                userList.add(u);
            }
            repository.saveAll(userList);
            map.put("Success", 1);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch(Exception e){
            map.put("Success", 0);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR); //Not HTTP Ok
        }
    }
}
