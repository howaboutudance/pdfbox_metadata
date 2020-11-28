import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.*;

public class PDFMetadata extends AbstractPDFMetadata{
    private final Path doc_path;
    private PDDocument doc;
    public Boolean loaded = false;
    public Date modification_date;
    public Date file_modification_date;
    private PDDocumentInformation pdd;
    public Map<String, String> doc_metadata;

    public PDFMetadata (String fn){
        doc_path = Paths.get(fn);
        populate();
    }

    public PDFMetadata(Path file_path) {
        doc_path = file_path;
        populate();
    }

    private void populate(){
        try {
            doc = PDDocument.load(doc_path.toFile());
            loaded = true;
        } catch (IOException e){
            loaded = false;
        }

        pdd = doc.getDocumentInformation();
        modification_date = getModificationDate().getTime();
        doc_metadata = getMetadata();
    }

    public boolean exists() {
        return Files.exists(doc_path);
    }

    private Calendar getModificationDate() {
        return pdd.getModificationDate();
    }

    public HashMap<String, HashMap> getFileDates(){
        HashMap<String, Object>  doc_dates = new HashMap<>();
        doc_dates.put("modification_date", modification_date.getTime());
        doc_dates.put("creation_date", pdd.getCreationDate().getTime());

        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("modification_date", help_getCreationTime(doc_path));
        metadata.put("creation_date", help_getModificationTime(doc_path));

        HashMap<String, HashMap> res_dates = new HashMap<>();

        res_dates.put("doc", doc_dates);
        res_dates.put("file", metadata);
        return res_dates;
    }

    private Date help_getFileTime(Path file_path, String value){
        try {
            FileTime creation_date = (FileTime) Files.getAttribute(
                    file_path,
                    value);

            return new Date(creation_date.toMillis());
        } catch (IOException e){
            return null;
        }
    }

    public Date help_getCreationTime(Path file_path){
        return help_getFileTime(file_path, "CreationTime");
    }

    public Date help_getModificationTime(Path file_path){
        return help_getFileTime(file_path, "ModificationTime");
    }

    private Map<String, String> getMetadata(){
        Set<String> keys = pdd.getMetadataKeys();
        Map<String, String> res = keys
                .stream()
                .collect(
                        HashMap::new,
                        (map, i) -> map.put(i, pdd.getCustomMetadataValue(i)),
                        HashMap::putAll
                );
        return res;
    }
}
