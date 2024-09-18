package top.dearbo.util.file;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.dearbo.util.exception.AppException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 判断日期格式和范围-正则 格式:2019-01-03
	 */
	public static final String YEAR_REXP = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";


	private String serverUrl;

	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * 设置上传服务器路径
	 */
	public void setServerUrl(String serverUrl) {

		this.serverUrl = serverUrl;
	}

	/**
	 * 读取某个文件夹下的所有文件
	 *
	 * @param filePath     文件夹
	 * @param filePathFlag ["true:获取相对路径","false:获取所有文件名称","null:获取相对路径"]
	 */
	public static List<String> readFileAndPath(String filePath, Boolean filePathFlag) {
		return readFileAndPath(filePath, filePathFlag, null);
	}

	/**
	 * 读取某个文件夹下的所有文件名称,可以根据正则匹配(reg==null:获取所有)
	 */
	public static List<String> readFileAndPathReg(String filePath, String reg) {
		return readFileAndPath(filePath, false, reg);
	}

	/**
	 * 读取某个文件夹下的所有文件,或根据正则匹配
	 */
	public static List<String> readFileAndPath(String filePath, Boolean filePathFlag, String reg) {
		List<String> returnFileList = new ArrayList<>();
		//如果路径为null
		if (StringUtils.isBlank(filePath)) {
			return returnFileList;
		}
		File file = new File(filePath);
		//文件不存在直接返回
		if (!file.exists()) {
			return returnFileList;
		}
		try {
			//如果是文件
			if (!file.isDirectory()) {
				//设置值
				String value = getFilePathType(file, filePathFlag, reg);
				if (value != null) {
					returnFileList.add(value);
				}
			} else if (file.isDirectory()) {
				//文件夹
				String[] fileList = file.list();
				if (null != fileList && fileList.length > 0) {
					for (String path : fileList) {
						File readFile = new File(filePath + "/" + path);
						if (!readFile.isDirectory()) {
							String value = getFilePathType(readFile, filePathFlag, reg);
							if (value != null) {
								returnFileList.add(value);
							}
						} else if (readFile.isDirectory()) {
							//添加子目录获取到的名称
							returnFileList.addAll(readFileAndPath(filePath + "/" + path, filePathFlag, reg));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info("readfile()   Exception:", e);
		}
		return returnFileList;
	}

	private static String getFilePathType(File file, Boolean filePathFlag, String reg) {
		String returnValue = null;
		if (null == filePathFlag) {
			//相对路径
			returnValue = file.getPath();
		} else if (filePathFlag) {
			//绝对路径
			returnValue = file.getAbsolutePath();
		} else {
			//文件名称
			returnValue = file.getName();
		}
		if (StringUtils.isNotBlank(reg)) {
			return returnValue.matches(reg) ? returnValue : null;
		}
		return returnValue;
	}

	/**
	 * 获取文件后缀
	 *
	 * @param fileName   要截取的文件名称
	 * @param suffix     以什么截取
	 * @param suffixFlag 返回是否包含截取的字符
	 * @return
	 */
	public static String getFileSuffix(String fileName, char suffix, boolean suffixFlag) {
		if (StringUtils.isBlank(fileName)) {
			return "";
		}
		int indexOf = fileName.lastIndexOf(suffix);
		if (indexOf != -1) {
			if (suffixFlag) {
				return fileName.substring(indexOf);
			} else {
				return fileName.substring(indexOf + 1);
			}
		}
		return "";
	}

	/**
	 * 按最后一次出现的字符截取
	 *
	 * @param fileName 截取的字符
	 * @param suffix   以那个字符截取，默认不返回截取的字符
	 * @return
	 */
	public static String getFileSuffix(String fileName, char suffix) {
		return getFileSuffix(fileName, suffix, false);
	}

	/**
	 * 获取文件后缀
	 *
	 * @param fileName   文件名称
	 * @param suffixFlag 是否返回要截取的字符
	 * @return 默认以[.]截取
	 */
	public static String getFileSuffix(String fileName, boolean suffixFlag) {
		return getFileSuffix(fileName, '.', suffixFlag);
	}

	/**
	 * @return 返回要截取后的值[不包括截取的字符]
	 */
	public static String getFileSuffix(String fileName) {
		return getFileSuffix(fileName, '.', false);
	}

	/**
	 * 获取文件后缀名称
	 *
	 * @param name          名称
	 * @param split         分割符
	 * @param punctuateFlag 是否加分隔符
	 * @author: bo
	 * @created 2017年6月5日
	 */
	public static String getFileSuffixName(String name, String split, boolean punctuateFlag) {
		int i = name.lastIndexOf(split);
		if (punctuateFlag) {
			return i == -1 ? name : name.substring(0, name.lastIndexOf(split) + 1);
		} else {
			return i == -1 ? name : name.substring(0, name.lastIndexOf(split));
		}
	}

	/**
	 * 获取文件除后缀的名称
	 *
	 * @param name  名称
	 * @param split 分割符
	 * @author: bo
	 * @created 2017年6月5日
	 */
	public static String getFileSuffixFileName(String name, String split) {

		return getFileSuffixName(name, split, false);
	}

	public static String getFileSuffixFileName(String name) {

		return getFileSuffixName(name, ".", false);
	}

	/**
	 * 拼接文件url
	 *
	 * @param path     路径
	 * @param fileName 文件名称
	 */
	public static String getSpliceSuffix(String path, String fileName) {
		boolean rootUrlSuffix = path.endsWith("/") || path.endsWith("\\");
		boolean rootUrlPrefix = fileName.startsWith("/") || fileName.startsWith("\\");
		if (rootUrlSuffix || rootUrlPrefix) {
			return path + fileName;
		}
		return path + "/" + fileName;
	}

	/**
	 * 创建文件夹,失败抛异常.
	 *
	 * @param fileUrl    路径+名称
	 * @param suffixFlag 路径末尾是否加 /
	 */
	public static String createMkdirOne(String fileUrl, boolean suffixFlag) {

		File file = new File(fileUrl);

		if (!file.exists()) {
			//mkdirFlag = file.getParentFile().mkdirs(); 获取父级目录创建
			if (file.mkdirs()) {
				logger.info("文件夹【{}】创建成功...", file.getAbsolutePath());
				return suffixFlag ? file.getAbsolutePath() + "/" : file.getAbsolutePath();
			} else {
				AppException.throwEx("文件夹【" + file.getAbsolutePath() + "】创建失败...");
			}
		} else {
			logger.info("文件夹【{}】已存在...", file.getAbsolutePath());
		}
		if (suffixFlag) {
			return urlPrefixSuffix(fileUrl) ? fileUrl : fileUrl + "/";
		}
		return fileUrl;
	}

	/**
	 * 拼接文件路径
	 *
	 * @param rootUrl D:/
	 * @param values  aaa,bbb,ccc
	 *                D:/aaa/bbb/ccc
	 */
	public static String splicingFilePath(String rootUrl, String... values) {
		if (StringUtils.isBlank(rootUrl) || values.length < 1) {
			return rootUrl;
		}
		boolean rootUrlSuffix = rootUrl.endsWith("/") || rootUrl.endsWith("\\");
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			if (StringUtils.isBlank(value)) {
				continue;
			}
			if (i == 0) {
				boolean thisValueFlag = value.startsWith("/");
				if (thisValueFlag) {
					if (value.length() < 2) {
						continue;
					}
					value = value.substring(1);
				}
				stringBuffer.append(value);
			} else {
				String oldValue = stringBuffer.toString();
				boolean oldValueFlag = oldValue.endsWith("/") || oldValue.endsWith("\\");
				boolean thisValueFlag = value.startsWith("/");
				if (thisValueFlag) {
					if (value.length() < 2) {
						continue;
					}
					value = value.substring(1);
				}
				if (oldValueFlag) {
					stringBuffer.append(value);
				} else {
					stringBuffer.append("/").append(value);
				}
			}
		}

		return rootUrlSuffix ? rootUrl + stringBuffer : rootUrl + "/" + stringBuffer;
	}

	/**
	 * 创建多个文件夹,失败抛异常
	 *
	 * @param rootUrl    根目录 必须存在
	 * @param pathValues 要创建的文件夹
	 *                   rootUrl:"P:/"
	 *                   suffixFlag: true
	 *                   pathValues:"dan"
	 * @return 路径 ["suffixFlag=true--->P:/dan/","suffixFlag=false--->P:/dan"]
	 */
	public static String createMkdirMulti(String rootUrl, boolean suffixFlag, String... pathValues) {
		if (StringUtils.isBlank(rootUrl) || pathValues.length < 1) {
			return "";
		}
		if (pathValues.length > 1) {
			String thisPath = splicingFilePath(rootUrl, pathValues);
			return createMkdirOne(thisPath, suffixFlag);
		} else {
			String pathValue = pathValues[0];
			if (urlPrefixSuffix(rootUrl, pathValue)) {
				return createMkdirOne(rootUrl + pathValue, suffixFlag);
			} else {
				return createMkdirOne(rootUrl + "/" + pathValue, suffixFlag);
			}
		}
	}

	/**
	 * 判断url 后缀是否包含 /,path前缀是否包含 /
	 */
	private static boolean urlPrefixSuffix(String url, String path) {
		boolean urlSuffix = url.endsWith("/") || url.endsWith("\\");
		boolean pathPrefix = path.startsWith("/") || path.startsWith("\\");
		return urlSuffix || pathPrefix;
	}

	/**
	 * 判断url 后缀是否包含 /
	 */
	public static boolean urlPrefixSuffix(String url) {
		return url.endsWith("/") || url.endsWith("\\");
	}

	/**
	 * 文件重命名
	 *
	 * @param file         文件名称
	 * @param fileName     文件名称
	 * @param renameToFlag 文件重复重命名-加 1 2 3
	 * @return boolean
	 * @author Bo
	 */
	public static boolean setFileRenameTo(File file, String fileName, boolean renameToFlag, String splice) {
		if (file.exists() && file.isFile()) {
			String path = file.getParent();
			String newPath = getSpliceSuffix(path, fileName);
			if (renameToFlag) {
				//后缀
				String fileSuffix = getFileSuffix(fileName, true);

				//文件名称，除后缀
				String fileSuffixFileName = getFileSuffixFileName(fileName) + splice;
				int i = 1;
				while (true) {
					File newFile = new File(newPath);
					if (!newFile.exists()) {
						return file.renameTo(newFile);
					}
					newPath = getSpliceSuffix(path, fileSuffixFileName + (++i) + fileSuffix);
				}
			} else {
				return file.renameTo(new File(newPath));
			}
		}
		return false;
	}

	/**
	 * 判断当前路径是否存在该文件/路径(文件名称为null当成文件夹处理)
	 * 文件：返回新文件名称
	 * 文件夹：返回新文件夹路径(带/)
	 *
	 * @param path     路径
	 * @param fileName 文件名称
	 * @return path / fileName
	 */
	public static String getNotExistFilePath(String path, String fileName, String splice) {
		if (StringUtils.isBlank(path)) {
			logger.error("文件路径或者文件名称不能为空!");
			return path;
		}
		splice = splice == null ? "" : splice;
		//处理文件重复
		if (StringUtils.isNotBlank(fileName)) {
			//完整的文件url
			String fileUrl = getSpliceSuffix(path, fileName);
			File file = new File(fileUrl);
			if (file.exists() && file.isFile()) {
				int i = 1;
				//后缀
				String fileSuffix = getFileSuffix(fileName, true);
				//文件名称，除后缀
				String fileSuffixFileName = getFileSuffixFileName(fileName) + splice;
				String newPath = getSpliceSuffix(path, fileSuffixFileName + i + fileSuffix);
				while (true) {
					file = new File(newPath);
					if (!file.exists()) {
						return file.getName();
					}
					newPath = getSpliceSuffix(path, fileSuffixFileName + (++i) + fileSuffix);
				}
			}
			return fileName;
		} else {
			//处理文件夹
			File file = new File(path);
			String absolutePath = file.getAbsolutePath();
			if (file.exists() && file.isDirectory()) {
				int i = 1;
				//通过getAbsolutePath获取路径,如果传的path末尾带/,会自动去掉
				String newPath = absolutePath + splice;
				while (true) {
					file = new File(newPath + i);
					if (!file.exists()) {
						return file.getAbsolutePath() + "\\";
					}
					i++;
				}
			}
			return absolutePath + "\\";
		}
	}

	public static String getNotExistFilePath(String path, String fileName) {
		return getNotExistFilePath(path, fileName, "-");
	}

	/**
	 * 删除文件
	 *
	 * @param path
	 * @return
	 */
	public static boolean removeFile(String path) {
		if (StringUtils.isBlank(path)) {
			return false;
		}
		return removeFile(new File(path));
	}

	public static boolean removeFile(File file) {
		if (file != null && file.exists() && file.isFile()) {

			return file.delete();
		}
		return false;
	}

	/**
	 * 删除目录，返回删除的文件数
	 *
	 * @param rootPath 待删除的目录
	 * @return 已删除的文件个数
	 */
	public static int deleteFiles(String rootPath) {

		return deleteFiles(rootPath, 0);
	}

	/**
	 * 删除目录，返回删除的文件数
	 *
	 * @param rootPath 待删除的目录
	 * @param fileNum  已删除的文件个数
	 * @return 已删除的文件个数
	 */
	public static int deleteFiles(String rootPath, int fileNum) {
		File file = new File(rootPath);
		if (!file.exists()) {
			return -1;
		}
		if (file.isDirectory()) {
			File[] sonFiles = file.listFiles();
			if (sonFiles != null) {
				for (File sonFile : sonFiles) {
					if (sonFile.isDirectory()) {
						fileNum = deleteFiles(sonFile.getAbsolutePath(), fileNum);
					} else {
						sonFile.delete();
						fileNum++;
					}
				}
			}
		} else {
			file.delete();
		}
		return fileNum;
	}

	/**
	 * 读取文件内容为二进制数组
	 *
	 * @param filePath 文件路径
	 * @return
	 * @throws IOException
	 */
	public static byte[] read(String filePath) throws IOException {
		InputStream in = Files.newInputStream(Paths.get(filePath));
		byte[] data = inputStreamByteArray(in);
		in.close();
		return data;
	}

	/**
	 * 流转二进制数组
	 *
	 * @param in
	 * @throws IOException
	 */
	public static byte[] inputStreamByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while ((n = in.read(buffer)) != -1) {
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}

	/**
	 * 保存文件
	 *
	 * @param filePath 路径
	 * @param fileName 文件名称
	 * @param content  文件内容
	 */
	public static void save(String filePath, String fileName, byte[] content) throws IOException {
		File fileDir = new File(filePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		File file = new File(fileDir, fileName);
		OutputStream os = null;
		try {
			os = Files.newOutputStream(file.toPath());
			os.write(content, 0, content.length);
		} catch (IOException e) {
			throw e;
		} finally {
			if (null != os) {
				try {
					os.flush();
				} catch (IOException ignored) {

				}
				try {
					os.close();
				} catch (IOException ignored) {
				}
			}
		}

	}

	/**
	 * 保存内容到指定文件(覆盖)
	 *
	 * @param file    文件
	 * @param content 内容
	 * @return boolean
	 */
	public static boolean saveAsFileWriter(File file, String content) {
		return saveAsFileWriter(file, content, false);
	}

	/**
	 * 保存内容到指定文件
	 *
	 * @param file    文件
	 * @param content 内容
	 * @param append  true:追加,false:覆盖
	 * @return boolean
	 */
	public static boolean saveAsFileWriter(File file, String content, boolean append) {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		FileWriter fileWriter = null;
		try {
			// true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
			fileWriter = new FileWriter(file, append);
			fileWriter.write(content);
			return true;
		} catch (IOException ex) {
			logger.error("file name {},saveContent error", file.getName(), ex);
			return false;
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.flush();
				} catch (IOException ignored) {
				}
				try {
					fileWriter.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	/**
	 * 读取文件内容
	 *
	 * @param fileUrl 文件地址
	 * @return String
	 */
	public static String readFileContent(String fileUrl) {
		return readFileContent(new File(fileUrl));
	}

	/**
	 * 读取文件内容
	 *
	 * @param file 文件
	 * @return String
	 */
	public static String readFileContent(File file) {
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		BufferedReader reader = null;
		StringBuilder sbf = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempStr;
			while ((tempStr = reader.readLine()) != null) {
				sbf.append(tempStr);
			}
			return sbf.toString();
		} catch (IOException ignored) {
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ignored) {
				}
			}
		}
		return sbf.toString();
	}

	/**
	 * 上传文件
	 *
	 * @param file 文件
	 */
    /*public static boolean fileUpload(MultipartFile file, String path) {
        if (file != null && !file.isEmpty()) {
            File localFile = new File(path);
            try {
                file.transferTo(localFile);
                return true;
            } catch (IOException e) {
                System.out.println("上传文件失败!" + e.getMessage());
            }
        }
        return false;
    }*/

}
