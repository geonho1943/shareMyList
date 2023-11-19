package com.geonho1943.sharemylist.controller.testController;

import com.geonho1943.sharemylist.model.Potalgate;
import com.geonho1943.sharemylist.service.PotalgateSrevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class potalgateController {

    @Autowired
    private PotalgateSrevice potalgateSrevice;

    @GetMapping("/sample")
    public String sample() {
        System.out.println("sample 성공");
        return "sample/sample";
    }

    @GetMapping("/stopsample")
    public String sampleGetMapping(Model model) {

        try {
            List<Potalgate> pt = potalgateSrevice.getAllPotal();
            model.addAttribute("potalList", pt);
        }catch (Exception e){
            System.out.println(e);
        }
        return "sample/stopsample";
    }
}

