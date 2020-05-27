package com.dan.common.util.mail;

import com.dan.utils.exception.AppException;
import com.dan.utils.lang.ObjectUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @fileName: EmailUtilBuilder
 * @author: Dan
 * @createDate: 2018-05-10 15:11.
 * @updateDate: 2019-11-15 09:15.
 * @description: 建造者模式-邮件
 */
public class EmailUtilBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtilBuilder.class);

    /**
     * 参数构建对象
     */
    private Builder builder;
    /**
     * 邮件对象
     */
    private MimeMessage mimeMsg;
    private Session session;
    /**
     * 附件添加的组件
     */
    private Multipart mp;

    /**
     * 存放附件文件
     */
    private List<FileDataSource> files;

    /**
     * 根据内部建造者的参数来初始化对象
     */
    private EmailUtilBuilder(Builder builder) {
        if (builder == null || StringUtils.isBlank(builder.host) || StringUtils.isBlank(builder.sendUserName) || StringUtils.isBlank(builder.sendUserPass) || StringUtils.isBlank(builder.to)) {
            AppException.throwEx("Mailer sender information [Host,SendUserName,SendUserPass,To] Is Null...");
        }
        this.builder = builder;
        this.init();
    }

    private void init() {
        Properties props = System.getProperties();
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", ObjectUtil.booleanIsTrue(builder.sslEnable) ? "true" : "false");
            props.put("mail.smtp.ssl.required", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
            //使用的协议（JavaMail规范要求）
            props.put("mail.transport.protocol", StringUtils.isNotBlank(builder.protocol) ? builder.protocol : "smtp");
            props.put("mail.smtp.host", builder.host);
            //开启ssl时
            if (ObjectUtil.booleanIsTrue(builder.sslEnable)) {
                props.setProperty("mail.smtp.socketFactory.fallback", builder.sslFactoryFallback);
                props.setProperty("mail.smtp.socketFactory.class", builder.sslFactoryClass);
            }
            if (builder.port != null && builder.port > -1) {
                ///SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
                ///需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
                ///QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
                props.setProperty("mail.smtp.port", builder.port.toString());
                if (ObjectUtil.booleanIsTrue(builder.sslEnable)) {
                    props.setProperty("mail.smtp.socketFactory.port", builder.port.toString());
                }
            }
            //需要身份验证
            props.put("mail.smtp.auth", builder.authFlag);
            props.put("mail.smtp.timeout", String.valueOf(builder.timeOut));
            props.put("mail.smtp.connectiontimeout", String.valueOf(builder.connectionTimeOut));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        // 根据配置创建会话对象, 用于和邮件服务器交互
        if (builder.authFlag) {
            MyAuthenticator myAuthenticator = new MyAuthenticator(builder.sendUserName, builder.sendUserPass);
            session = Session.getDefaultInstance(props, myAuthenticator);
        } else {
            session = Session.getDefaultInstance(props);
        }
        // 置true可以在控制台（console)上看到发送邮件的过程
        session.setDebug(ObjectUtil.booleanIsTrue(builder.deBugFlag));
        // 用session对象来创建并初始化邮件对象
        mimeMsg = new MimeMessage(session);
        // 生成附件组件的实例
        mp = new MimeMultipart();
        files = new LinkedList<>();

        setFrom(builder.sendUserName);
        setTo(builder.to);
        setCC(builder.cc);
        setBody(builder.mailBody);
        setSubject(builder.mailSubject, builder.charset);
        setSendDate(builder.sendDate);
        if (ObjectUtil.isNotEmptyObj(builder.attachments)) {
            for (String attachment : builder.attachments) {
                addFileAffix(attachment);
            }
        }
    }

    public static class Builder {

        /**
         * 邮箱服务器
         */
        private String host;

        /**
         * 发件人
         */
        private String sendUserName;

        /**
         * 发件人凭证
         */
        private String sendUserPass;

        /**
         * 收件人，多个收件人以半角逗号分隔
         */
        private String to;

        /**
         * 正文，可以用html格式的哟
         */
        private String mailBody;

        //============>可选<============

        /**
         * 抄送，多个抄送以半角逗号分隔
         */
        private String cc;

        /**
         * 邮件主题
         */
        private String mailSubject;
        /**
         * 使用的协议-默认smtp
         */
        private String protocol;
        /**
         * 是否需要身份认证-默认需要
         */
        private boolean authFlag = true;
        /**
         * 设置true可以在控制台（console)上看到发送邮件的过程-默认不显示
         */
        private Boolean deBugFlag;
        /**
         * 是否输出异常日志
         */
        private boolean logErrorEnable = false;
        /**
         * 邮件发送时间-默认当前
         */
        private Date sendDate;
        /**
         * 附件url
         */
        private List<String> attachments;

        /**
         * 端口,邮箱端口默认-自动
         */
        private Integer port = null;
        /**
         * 是否开启,ssl.enable-默认关闭
         */
        private boolean sslEnable = false;

        //ssl 开启后
        private String sslFactoryClass = "javax.net.ssl.SSLSocketFactory";
        private String sslFactoryFallback = "false";

        /**
         * smtp邮件服务器读取超时-默认5秒
         */
        private long timeOut = 5000;

        /**
         * smtp邮件服务器连接超时-默认5秒
         */
        private long connectionTimeOut = 5000;

        /**
         * 编码
         */
        private String charset = "UTF-8";

        public Builder(String host, String sendUserName, String sendUserPass) {
            this(host, sendUserName, sendUserPass, null);
        }

        public Builder(String host, String sendUserName, String sendUserPass, String to) {
            this(host, sendUserName, sendUserPass, to, null);
        }

        public Builder(String host, String sendUserName, String sendUserPass, String to, String mailBody) {
            this(host, sendUserName, sendUserPass, to, mailBody, null);
        }

        public Builder(String host, String sendUserName, String sendUserPass, String to, String mailBody, String mailSubject) {
            this(host, sendUserName, sendUserPass, to, mailBody, null, mailSubject);
        }

        public Builder(String host, String sendUserName, String sendUserPass, String to, String mailBody, String cc, String mailSubject) {
            this.host = host;
            this.sendUserName = sendUserName;
            this.sendUserPass = sendUserPass;
            this.to = to;
            this.mailBody = mailBody;
            this.cc = cc;
            this.mailSubject = mailSubject;
        }

        /**
         * 通过内部类创建者的builder方法,调用外部类的构造器.
         * 创建对象.
         * builder类似构造器,能够对参数加约束条件.
         */
        public EmailUtilBuilder builder() {
            // 可以在此对所有的参数做检查.凡是不满足条件的可以在此处就报错.这就是约束条件的check
            return new EmailUtilBuilder(this);
        }

        public Builder setSslFactoryClass(String sslFactoryClass) {
            this.sslFactoryClass = sslFactoryClass;
            return this;
        }

        public Builder setSslFactoryFallback(String sslFactoryFallback) {
            this.sslFactoryFallback = sslFactoryFallback;
            return this;
        }

        public Builder setSslEnable(boolean sslEnable) {
            this.sslEnable = sslEnable;
            return this;
        }

        public Builder setTimeOut(long timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder setConnectionTimeOut(long connectionTimeOut) {
            this.connectionTimeOut = connectionTimeOut;
            return this;
        }

        public Builder setPort(Integer port) {
            this.port = port;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setSendUserName(String sendUserName) {
            this.sendUserName = sendUserName;
            return this;
        }

        public Builder setSendUserPass(String sendUserPass) {
            this.sendUserPass = sendUserPass;
            return this;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getCc() {
            return cc;
        }

        public Builder setCc(String cc) {
            this.cc = cc;
            return this;
        }

        public Builder setTo(String to) {
            this.to = to;
            return this;
        }

        public Builder setMailBody(String mailBody) {
            this.mailBody = mailBody;
            return this;
        }

        public Builder setMailSubject(String mailSubject) {
            this.mailSubject = mailSubject;
            return this;
        }

        public Builder setAuthFlag(boolean authFlag) {
            this.authFlag = authFlag;
            return this;
        }

        public Boolean getDeBugFlag() {
            return deBugFlag;
        }

        public Builder setDeBugFlag(Boolean deBugFlag) {
            this.deBugFlag = deBugFlag;
            return this;
        }

        public Builder setLogErrorEnable(boolean logErrorEnable) {
            this.logErrorEnable = logErrorEnable;
            return this;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public Builder setSendDate(Date sendDate) {
            this.sendDate = sendDate;
            return this;
        }

        public Builder setAttachments(List<String> attachments) {
            this.attachments = attachments;
            return this;
        }
    }

    class MyAuthenticator extends Authenticator {
        private String strUser;
        private String strPwd;

        public MyAuthenticator(String user, String password) {
            this.strUser = user;
            this.strPwd = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(strUser, strPwd);
        }
    }

    /**
     * 设置发送时间
     */
    private boolean setSendDate(Date sendDate) {
        if (sendDate == null) {
            sendDate = new Date();
        }
        try {
            mimeMsg.setSentDate(sendDate);
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

    /**
     * 设置邮件主题
     */
    private boolean setSubject(String mailSubject, String charset) {
        try {
            mimeMsg.setSubject(mailSubject, charset);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 设置邮件内容,并设置其为文本格式或HTML文件格式，编码方式为UTF-8
     */
    private boolean setBody(String mailBody) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + (mailBody == null ? "" : mailBody), "text/html;charset=UTF-8");
            // 在组件上添加邮件文本
            mp.addBodyPart(bp);
        } catch (Exception e) {
            if (builder.logErrorEnable) {
                LOGGER.error("设置邮件正文时发生错误！error:{}", e.getMessage());
            }
            return false;
        }
        return true;
    }

    /**
     * 添加一个附件
     *
     * @param filename 邮件附件的地址，只能是本机地址而不能是网络地址，否则抛出异常
     */
    public boolean addFileAffix(String filename) {
        try {
            if (StringUtils.isNotBlank(filename)) {
                BodyPart bp = new MimeBodyPart();
                FileDataSource fileds = new FileDataSource(filename);
                bp.setDataHandler(new DataHandler(fileds));
                // 解决附件名称乱码
                bp.setFileName(MimeUtility.encodeText(fileds.getName(), "utf-8", null));
                // 添加附件
                mp.addBodyPart(bp);
                files.add(fileds);
            }
        } catch (Exception e) {
            if (builder.logErrorEnable) {
                LOGGER.error("增加邮件附件:{},error:{}", filename, e.getMessage());
            }
            return false;
        }
        return true;
    }

    /**
     * 删除所有附件
     */
    public boolean delFileAffix() {
        try {
            FileDataSource fileds = null;
            for (Iterator<FileDataSource> it = files.iterator(); it.hasNext(); ) {
                fileds = it.next();
                if (fileds != null && fileds.getFile() != null) {
                    fileds.getFile().delete();
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 设置发件人地址
     *
     * @param from 发件人地址
     */
    private boolean setFrom(String from) {
        try {
            mimeMsg.setFrom(new InternetAddress(from));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 设置收件人邮箱
     *
     * @param to 收件人的邮箱
     * @return
     */
    private boolean setTo(String to) {
        if (StringUtils.isBlank(to)) {
            return false;
        }
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 设置抄送人邮箱
     *
     * @param cc 抄送人邮箱
     */
    private boolean setCC(String cc) {
        if (StringUtils.isBlank(cc)) {
            return false;
        }
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 发送邮件
     */
    public boolean send() {
        Transport transport = null;
        try {
            try {
                mimeMsg.setContent(mp);
            } catch (MessagingException e) {
                if (builder.logErrorEnable) {
                    LOGGER.error("【send】==>设置附件失败,error:{}", e.getMessage());
                }
                return false;
            }
            //保存上面的所有设置
            try {
                mimeMsg.saveChanges();
            } catch (MessagingException e) {
                if (builder.logErrorEnable) {
                    LOGGER.error("【send】==>保存设置异常,error:{}", e.getMessage());
                }
                return false;
            }
            transport = session.getTransport();
            // 连接邮件服务器并进行身份验证
            try {
                transport.connect(builder.host, builder.sendUserName, builder.sendUserPass);
            } catch (MessagingException e) {
                if (builder.logErrorEnable) {
                    LOGGER.error("【send】==>身份验证失败,error:{}", e.getMessage());
                }
                return false;
            }
            // 发送邮件
            try {
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                return true;
            } catch (MessagingException e) {
                if (builder.logErrorEnable) {
                    LOGGER.error("【send】==>发送邮件失败,error:{}", e.getMessage());
                }
            }
        } catch (Exception e) {
            if (builder.logErrorEnable) {
                LOGGER.error("【send】==>邮件发送未知异常,error:{}", e.getMessage());
            }
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    if (builder.logErrorEnable) {
                        LOGGER.error("【send】==>关闭transport失败,error:{}", e.getMessage());
                    }
                }
            }
        }
        return false;
    }

    /*public static void main(String[] args) {
        String userName = "dearbo8@qq.com";
        //String userName = "wubodanran@163.com";
        String pwd = "授权码";
        String to = "wubodanran@qq.com";
        String mailBody = "测试发送邮件";
        String mailSubject = "测试主题" + DateUtil.parseToString(DateUtil.getDate());
        String mailHost = "smtp.qq.com";
        int prod = 465;
        //使用默认
        EmailUtilBuilder.Builder builder = new EmailUtilBuilder.Builder(mailHost, userName, pwd, to);
        builder.setPort(prod);
        builder.setSslEnable(true);
        builder.setDeBugFlag(true).setMailBody(mailBody).setMailSubject(mailSubject).builder().send();
    }*/

}
