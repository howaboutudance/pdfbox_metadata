import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public abstract class AbstractPDFMetadata {
    abstract Date help_getCreationTime(Path file_path);
    abstract Date help_getModificationTime(Path file_path);
    private Date help_getCreationTime(String filename){
        return help_getCreationTime(Paths.get(filename));
    }
    private Date help_getMoedificationTime(String filename){
        return help_getModificationTime(Paths.get(filename));
    }
}
