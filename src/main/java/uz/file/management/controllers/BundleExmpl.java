package uz.file.management.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.file.management.exception.BundleException;

import static uz.file.management.utils.TranslatorCode.GREETINGS;

@RestController
@RequestMapping("/test")
public class BundleExmpl {

    @GetMapping
    public Object getMessage() throws Exception {
        throw new BundleException(GREETINGS);
    }
}
