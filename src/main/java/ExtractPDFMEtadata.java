public class ExtractPDFMEtadata {
    public static void main(String[] args) {
        PDFMetadata doc = new PDFMetadata(args[0]);
        System.out.println(doc.doc_metadata);
    }
}
