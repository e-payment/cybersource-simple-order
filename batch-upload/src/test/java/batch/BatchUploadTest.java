package batch;

import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchUploadTest {
  
    @Test
    public void test() throws Exception {

        BatchUpload.main(new String[] {"batch-upload.properties"});

    }
}
