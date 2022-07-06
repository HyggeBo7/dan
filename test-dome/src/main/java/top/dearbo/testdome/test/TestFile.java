package top.dearbo.testdome.test;

import top.dearbo.util.file.FileZipUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.ZipOutputStream;

/**
 * @fileName: TestFile
 * @author: bo
 * @createDate: 2018-12-27 9:00.
 * @description:
 */
public class TestFile {

    @Test
    public void zipFile() {
        String fileName = "icode";
        String sourceFilePath = "D:/dan_template/mess/" + fileName;
        String zipFilePath = "D:/dan_template/" + fileName + ".zip";
        File file = new File(zipFilePath);
        if (file.exists()) {
            file.delete();
        }
        FileZipUtil.zipFileWithTier(sourceFilePath, zipFilePath);
    }

    @Test
    public void zipFile2() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        String fileName = "icode";
        String sourceFilePath = "D:/dan_template/mess/" + fileName;

        FileZipUtil.zipFileWithTier(sourceFilePath, zip);

        IOUtils.closeQuietly(zip);
        System.out.println(outputStream.toByteArray());
    }

}
