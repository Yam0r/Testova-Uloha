package user.testovauloha.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user.testovauloha.model.CastObce;
import user.testovauloha.model.Obec;
import user.testovauloha.repository.CastObceRepository;
import user.testovauloha.repository.ObecRepository;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class ImportData {
    private final CastObceRepository castObceRepository;
    private final ObecRepository obecRepository;

    public void importData(){
        String urlString = "https://www.smartform.cz/download/kopidlno.xml.zip";

        try(InputStream in = URI.create(urlString).toURL().openStream();
            ZipInputStream zis = new ZipInputStream(in)) {

            ZipEntry entry = zis.getNextEntry();
            if(entry != null && !entry.isDirectory()){
                XMLInputFactory factory = XMLInputFactory.newInstance();
                XMLStreamReader reader = factory.createXMLStreamReader(zis);
                Obec currentObec = null;
                CastObce currentCastObce = null;
                StringBuilder textBuilder = new StringBuilder();
                Obec savedObec = null;

                while (reader.hasNext()){
                    int event = reader.next();

                    switch (event) {
                        case XMLStreamConstants.START_ELEMENT -> {
                            String tagName = reader.getLocalName();
                            textBuilder.setLength(0);

                            if ("Obec".equals(tagName) && savedObec == null) {
                                currentObec = new Obec();
                            } else if ("CastObce".equals(tagName)) {
                                currentCastObce = new CastObce();
                            }
                        }
                        case XMLStreamConstants.CHARACTERS -> {
                            textBuilder.append(reader.getText());
                        }
                        case XMLStreamConstants.END_ELEMENT -> {
                            String tagName = reader.getLocalName();
                            String currentText = textBuilder.toString().trim();

                            if (currentObec != null) {
                                if ("Kod".equals(tagName) && currentObec.getId() == null) {
                                    currentObec.setId(Long.parseLong(currentText));
                                } else if ("Nazev".equals(tagName)) {
                                    if (currentObec.getName() == null) {
                                        currentObec.setName(currentText);
                                    }
                                } else if ("Obec".equals(tagName)) {
                                    savedObec = obecRepository.save(currentObec);
                                    currentObec = null;
                                }
                            }

                            if (currentCastObce != null) {
                                if ("Kod".equals(tagName) && currentCastObce.getId() == null) {
                                    currentCastObce.setId(Long.parseLong(currentText));
                                } else if ("Nazev".equals(tagName)) {
                                    if (currentCastObce.getName() == null) {
                                        currentCastObce.setName(currentText);
                                    }
                                } else if ("CastObce".equals(tagName)) {
                                    currentCastObce.setObec(savedObec);
                                    if (currentCastObce.getName() != null) {
                                        castObceRepository.save(currentCastObce);
                                    }
                                    currentCastObce = null;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
