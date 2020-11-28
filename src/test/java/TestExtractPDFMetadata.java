/*
 * Copyright (c) 2020. Michael Penhallegon <mike@hematite.tech>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

public class TestExtractPDFMetadata{
    String test_filename = "src/test/resources/ERIC_ED094011.pdf";
    @Test
    public void testPDFexists() {
        PDFMetadata tester = new PDFMetadata(test_filename);

        assertTrue(tester.exists());
        assertTrue(tester.loaded);
    }

    @Test
    public void testPDFModDateDifferent() {
        PDFMetadata tester = new PDFMetadata(test_filename);
        assertNotNull(tester.modification_date);

        try {
            FileTime doc_filetime = Files.getLastModifiedTime(
                    Paths.get(test_filename));
            Date doc_last_date = new Date(doc_filetime.toMillis());
            assertNotEquals(doc_last_date, tester.modification_date,
                    format("Modification date the same %s & %s", doc_filetime,
                            tester.modification_date));
        } catch (IOException e){
            fail();
        }
    }

    @Test
    public void  testPDFFullMetadata() {
        PDFMetadata tester = new PDFMetadata(test_filename);
        assertTrue(tester.doc_metadata.size() > 0, tester.doc_metadata.toString());
        assertNotNull(tester.doc_metadata.get("Producer"), tester.doc_metadata.toString());
        assertNotNull(tester.doc_metadata.get("ModDate"), tester.doc_metadata.toString());
    }
}
