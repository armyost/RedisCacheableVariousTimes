package com.example.demo;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemoLookAsideController {

    private final DemoLookAsideService demoLookAsideService;

    public DemoLookAsideController(DemoLookAsideService demoLookAsideService) {
        this.demoLookAsideService = demoLookAsideService;
    }

    private static final Logger logger = LoggerFactory.getLogger(DemoLookAsideController.class);;

    @GetMapping(value = "/ping")
    public String getPing() {
        String response_val = "pong";
        return response_val;
    }

    /*
     * Look-Aside Caching pattern Test
     */

    // Cacheable Controller
    @GetMapping(value = "/cacheablePathWith1H/{postValue}")
    public String cacheablePathWith1H(@PathVariable String postValue) throws Exception {
        String responseValue = "EMPTY_VALUE";

        HashMap<String, String> sampleMap = new HashMap<String, String>();
        sampleMap.put("SampleKey", postValue);

        // realApplication <- Path will be cached
        Map responseMap = demoLookAsideService.getRestData1H("http://localhost:8080", "/realApplication/1H/listSample.do", sampleMap,
                HttpMethod.POST);
        logger.info("##### Result Value is {} #####", responseMap);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(responseMap);
    }

    @GetMapping(value = "/cacheablePathWith1D/{postValue}")
    public String cacheablePathWith1D(@PathVariable String postValue) throws Exception {

        HashMap<String, String> sampleMap = new HashMap<String, String>();
        sampleMap.put("SampleKey", postValue);

        // realApplication <- Path will be cached
        Map responseMap = demoLookAsideService.getRestData1D("http://localhost:8080", "/realApplication/1D/listSample.do", sampleMap,
                HttpMethod.POST);
        logger.info("##### Result Value is {} #####", responseMap);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(responseMap);
    }

    // Uncacheable Controller
    @GetMapping(value = "/uncacheablePath/{postValue}")
    public String uncacheablePath(@PathVariable String postValue) throws Exception {

        HashMap<String, String> sampleMap = new HashMap<String, String>();
        sampleMap.put("SampleKey", postValue);

        // demoApplication <- Path will be uncached
        Map responseMap = demoLookAsideService.getRestData1D("http://localhost:8080", "/demoApplication/listSample.do",
                sampleMap,
                HttpMethod.POST);

        logger.info("##### Result Value is {} #####", responseMap);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(responseMap);
    }

    /*
     * SAMPLE -------------------------------------------------------------------
     * 
     * @GetMapping(value = "/{postId}")
     * public DemoEntity findGet(@PathVariable String postId){
     * return demoEntity;
     * }
     * 
     * @GetMapping(value = "")
     * public DemoEntity findGet(@RequestParam int postId){
     * return demoEntity;
     * }
     * 
     * @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
     * public ResponseEntity<String> addPost(@RequestBody Map<String, String>
     * requestBody){
     * return new ResponseEntity<>("result successful", HttpStatus.OK);
     * }
     * 
     * @PutMapping(value = "/{postId}", consumes = MediaType.APPLICATION_JSON_VALUE)
     * public ResponseEntity<String> modifyPut(@PathVariable String
     * postId, @RequestBody Map<String, String> requestBody){
     * return new ResponseEntity<>("result successful", HttpStatus.OK);
     * }
     * 
     * @DeleteMapping("/{postId}")
     * public ResponseEntity<String> removeDelete(@PathVariable String postId){
     * return new ResponseEntity<>("result successful", HttpStatus.OK);
     * }
     * -----------------------------------------------------------------------------
     * --
     */
}