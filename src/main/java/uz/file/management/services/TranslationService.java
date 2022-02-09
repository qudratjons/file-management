package uz.file.management.services;

import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    public String translate(String code) {
        return Translator.toLocale(code);
    }
}
