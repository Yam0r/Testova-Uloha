package user.testovauloha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import user.testovauloha.model.Obec;
import user.testovauloha.repository.ObecRepository;
import user.testovauloha.service.ImportData;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImportController{
    private final ImportData importData;
    private final ObecRepository obecRepository;

    @GetMapping(value = "/getObce")
    public List<Obec> getListObce(){
        importData.importData();
        return obecRepository.findAll();
    }
}
