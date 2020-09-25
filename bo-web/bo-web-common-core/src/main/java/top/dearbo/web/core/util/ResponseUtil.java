package top.dearbo.web.core.util;

import org.apache.commons.lang3.StringUtils;
import top.dearbo.util.constant.ResultGeneric;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ResponseUtil
 * @createDate: 2020-06-30 13:34.
 * @description: Response操作
 */
public class ResponseUtil {

    /**
     * 下载文档
     *
     * @param response      HttpServletResponse
     * @param resultGeneric code,msg,ByteArrayOutputStream
     * @param fileName      文件名称(包含后缀)
     * @throws IOException
     */
    public static void downDocumentToStream(HttpServletResponse response, ResultGeneric<ByteArrayOutputStream> resultGeneric, String fileName) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        //自定义请求头
        response.setHeader("Access-Control-Expose-Headers", "ResultCode,ResultMsg,ResultDocumentName");
        ByteArrayOutputStream resultOutputStream = resultGeneric.getData();
        if (resultGeneric.isSuccess() && resultOutputStream != null && resultOutputStream.size() > 0) {
            response.setHeader("ResultMsg", StringUtils.isBlank(resultGeneric.resultMessage()) ? "" : URLEncoder.encode(resultGeneric.resultMessage(), "UTF-8"));
            response.setHeader("ResultCode", String.valueOf(resultGeneric.getCode()));
            response.setHeader("ResultDocumentName", URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(resultOutputStream.size()));
            response.setHeader("Content-Disposition", "attachment; filename=" + new String((fileName).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.getOutputStream().write(resultOutputStream.toByteArray());
            response.flushBuffer();
        } else {
            try {
                String msg = StringUtils.isNotBlank(resultGeneric.resultMessage()) ? resultGeneric.resultMessage() : "下载异常!";
                response.setHeader("ResultMsg", URLEncoder.encode(msg, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setHeader("ResultCode", String.valueOf(-1));
            response.setHeader("Content-Length", String.valueOf(0));
        }
    }

    /**
     * 返回一个提示信息的空流
     *
     * @param response HttpServletResponse
     * @param msg      提示
     * @throws UnsupportedEncodingException
     */
    public static void msgToEmptyStream(HttpServletResponse response, String msg) throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Access-Control-Expose-Headers", "ResultCode,ResultMsg,ResultDocumentName");
        response.setHeader("ResultMsg", StringUtils.isBlank(msg) ? "" : URLEncoder.encode(msg, "UTF-8"));
        response.setHeader("ResultCode", "-1");
        response.setHeader("Content-Length", "0");
    }
}
