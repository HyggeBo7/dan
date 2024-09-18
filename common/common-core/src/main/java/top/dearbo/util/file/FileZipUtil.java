package top.dearbo.util.file;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @fileName: FileZipUtil
 * @author: bo
 * @createDate: 2018-01-10 12:02.
 * @description:
 */
public class FileZipUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileZipUtil.class);

	/**
	 * 压缩文件-文件流
	 *
	 * @param sourceFilePath 压缩目录
	 * @return
	 */
	public static ByteArrayOutputStream zipFileWithTier(String sourceFilePath) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		zipFileWithTier(sourceFilePath, zip);
		if (zip != null) {
			try {
				zip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return outputStream;
	}

	/**
	 * 压缩文件-存储文件
	 *
	 * @param srcFiles 压缩目录
	 * @param zipPath  压缩文件存放位置
	 */
	public static void zipFileWithTier(String srcFiles, String zipPath) {
		try {
			FileOutputStream zipFile = new FileOutputStream(zipPath);
			BufferedOutputStream buffer = new BufferedOutputStream(zipFile);
			ZipOutputStream out = new ZipOutputStream(buffer);
			zipFiles(srcFiles, out, "");
			out.close();
		} catch (IOException e) {
			logger.error("zipFileWithTier zipPath：{} error", zipPath, e);
		}
	}

	/**
	 * 压缩文件-读取文件流
	 *
	 * @param srcFiles 压缩目录
	 * @param zip
	 */
	public static void zipFileWithTier(String srcFiles, ZipOutputStream zip) {
		try {
			zipFiles(srcFiles, zip, "");
			zip.closeEntry();
		} catch (IOException e) {
			logger.error("zipFileWithTier srcFiles：{} error", srcFiles, e);
		}
	}

	public static void zipFiles(String filePath, ZipOutputStream out, String prefix) throws IOException {
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (ArrayUtils.isNotEmpty(files)) {
				prefix += file.getName() + File.separator;
				for (File f : files) {
					zipFiles(f.getAbsolutePath(), out, prefix);
				}
			} else {
				ZipEntry zipEntry = new ZipEntry(prefix + file.getName() + "/");
				out.putNextEntry(zipEntry);
				out.closeEntry();
			}
		} else {
			FileInputStream in = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(prefix + file.getName());
			out.putNextEntry(zipEntry);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.closeEntry();
			in.close();
		}

	}

	public static void unzipFilesWithTier(byte[] bytes, String prefix) throws IOException {
		InputStream bais = new ByteArrayInputStream(bytes);
		ZipInputStream zin = new ZipInputStream(bais);
		ZipEntry ze;
		while ((ze = zin.getNextEntry()) != null) {
			if (ze.isDirectory()) {
				File file = new File(prefix + ze.getName());
				if (!file.exists()) {
					file.mkdirs();
				}
				continue;
			}
			File file = new File(prefix + ze.getName());
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			ByteArrayOutputStream toScan = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = zin.read(buf)) > 0) {
				toScan.write(buf, 0, len);
			}
			byte[] fileOut = toScan.toByteArray();
			toScan.close();
			writeByteFile(fileOut, new File(prefix + ze.getName()));
		}
		zin.close();
		bais.close();
	}

	public static byte[] readFileByte(String filename) throws IOException {
		if (filename == null || filename.isEmpty()) {
			throw new NullPointerException("File is not exist!");
		}
		File file = new File(filename);
		long len = file.length();
		byte[] bytes = new byte[(int) len];

		BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
		int r = bufferedInputStream.read(bytes);
		if (r != len) {
			throw new IOException("Read file failure!");
		}
		bufferedInputStream.close();

		return bytes;

	}

	public static boolean writeByteFile(byte[] bytes, File file) {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(bytes);
			return true;
		} catch (IOException e) {
			logger.error("read file error path：{}", file.getPath(), e);
		}
		return false;
	}

}
