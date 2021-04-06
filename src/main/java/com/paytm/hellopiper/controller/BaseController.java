package com.paytm.hellopiper.controller;

import com.paytm.hellopiper.model.BaseVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BaseController {

    @GetMapping("/base-version")
    public BaseVersion baseVersion(){
        return new BaseVersion("1.0.1", "hello-piper");
    }

}
