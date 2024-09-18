package top.dearbo.util.file;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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

	private static final Logger log = LoggerFactory.getLogger(FileZipUtil.class);

	/**
	 * 递归压缩方法
	 * @param sourceFile 源文件
	 * @param zos        zip输出流
	 * @param name       压缩后的名称
	 * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
	 *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws Exception
	 */
	public static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) {
		byte[] buf = new byte[2048];
		try {
			if (sourceFile.isFile()) {
				// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
				zos.putNextEntry(new ZipEntry(name));
				// copy文件到zip输出流中
				int len;
				FileInputStream in = new FileInputStream(sourceFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			} else {
				File[] listFiles = sourceFile.listFiles();
				if (listFiles != null && listFiles.length > 0) {
					for (File file : listFiles) {
						// 判断是否需要保留原来的文件结构
						if (KeepDirStructure) {
							// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
							// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
							String dirName = StringUtils.isBlank(name) ? file.getName() : name + "/" + file.getName();
							compress(file, zos, dirName, KeepDirStructure);
						} else {
							compress(file, zos, file.getName(), KeepDirStructure);
						}
					}
				} else {
					// 需要保留原来的文件结构时,需要对空文件夹进行处理
					if (KeepDirStructure && StringUtils.isNotBlank(name)) {
						// 空文件夹的处理
						zos.putNextEntry(new ZipEntry(name + "/"));
						// 没有文件，不需要文件的copy
						zos.closeEntry();
					}
				}
			}
		} catch (Exception e) {
			log.error("zip file failed", e);
		}
	}

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
			log.error("zipFileWithTier zipPath：{} error", zipPath, e);
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
			log.error("zipFileWithTier srcFiles：{} error", srcFiles, e);
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
			log.error("read file error path：{}", file.getPath(), e);
		}
		return false;
	}

}
