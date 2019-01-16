package com.dan.common.util.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.lang3.StringUtils;

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
 * @description: 建造者模式-邮件
 */
public class EmailUtilBuilder {

    //必选
    /**
     * 邮件服务器-默认126
     */
    private String smtpHost;
    /**
     * 发件人邮箱
     */
    private String sendUserName;
    /**
     * 发件人密码
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

    /**
     * 抄送，多个抄送以半角逗号分隔
     */
    private String cc;

    /**
     * 主题-默认空
     */
    private String mailSubject;

    /**
     * 是否需要身份认证-默认需要
     */
    private Boolean authFlag;
    /**
     * 设置true可以在控制台（console)上看到发送邮件的过程-默认不显示
     */
    private Boolean deBugFlag;

    /**
     * 邮件发送时间-默认当前
     */
    private Date sendDate;

    /**
     * 使用的协议-默认smtp
     */
    private String protocol;

    /**
     * 附件url
     */
    private List<String> attachments;

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
     * 邮箱端口默认-自动
     */
    private Integer port;

    /**
     * ssl.enable-默认关闭
     */
    private String sslEnable;

    /**
     * smtp邮件服务器读取超时-默认5秒
     */
    private Integer timeOut;

    /**
     * smtp邮件服务器连接超时-默认5秒
     */
    private Integer connectionTimeOut;

    /**
     * 根据内部建造者的参数来初始化对象
     */
    private EmailUtilBuilder(Builder builder) {
        this.smtpHost = builder.smtpHost;
        this.sendUserName = builder.sendUserName;
        this.sendUserPass = builder.sendUserPass;
        this.to = builder.to;
        this.mailBody = builder.mailBody;
        this.cc = builder.cc;
        this.mailSubject = builder.mailSubject;
        this.authFlag = builder.authFlag;
        this.deBugFlag = builder.deBugFlag == null ? false : builder.deBugFlag;
        this.sendDate = builder.sendDate == null ? new Date() : builder.sendDate;
        this.protocol = StringUtils.isNotBlank(builder.protocol) ? builder.protocol : "smtp";
        this.attachments = builder.attachments == null ? new ArrayList<>() : builder.attachments;
        this.port = builder.port;
        this.sslEnable = builder.sslEnable ? "true" : "false";
        this.timeOut = builder.timeOut;
        this.connectionTimeOut = builder.connectionTimeOut;
        this.init();
    }

    private void init() {
        Properties props = System.getProperties();
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", sslEnable);
            props.put("mail.smtp.ssl.required", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
            //使用的协议（JavaMail规范要求）
            props.put("mail.transport.protocol", protocol);
            props.put("mail.smtp.host", smtpHost);
            if (port != null) {
                props.put("mail.smtp.port", port);
            }
            //需要身份验证
            props.put("mail.smtp.auth", authFlag);

            props.put("mail.smtp.timeout", String.valueOf(timeOut));
            props.put("mail.smtp.connectiontimeout", String.valueOf(connectionTimeOut));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        // 根据配置创建会话对象, 用于和邮件服务器交互
        if (authFlag) {
            MyAuthenticator myAuthenticator = new MyAuthenticator(sendUserName, sendUserPass);
            session = Session.getDefaultInstance(props, myAuthenticator);
        } else {
            session = Session.getDefaultInstance(props);
        }
        // 置true可以在控制台（console)上看到发送邮件的过程
        session.setDebug(deBugFlag);
        // 用session对象来创建并初始化邮件对象
        mimeMsg = new MimeMessage(session);
        // 生成附件组件的实例
        mp = new MimeMultipart();
        files = new LinkedList<>();

        setFrom(sendUserName);
        setTo(to);
        setCC(cc);
        setBody(mailBody);
        setSubject(mailSubject);
        setSendDate(sendDate);
        if (attachments != null) {
            for (String attachment : attachments) {
                addFileAffix(attachment);
            }
        }
    }

    public static class Builder {

        /**
         * 默认的发件人用户名
         */
        private final static String defaultSenderName = "";
        /**
         * 默认的发件人密码
         */
        private final static String defaultSenderPass = "";
        /**
         * 默认的邮件服务器地址
         */
        private final static String defaultSmtpHost = "";

        private String smtpHost;

        private String sendUserName;

        private String sendUserPass;

        private String to;

        private String mailBody;

        /**
         * 可选
         */

        private String cc;

        private String mailSubject;
        private String protocol;
        private boolean authFlag = true;
        private Boolean deBugFlag;
        private Date sendDate;
        private List<String> attachments;

        /**
         * 端口
         */
        private Integer port = null;
        /**
         * 是否开启
         */
        private boolean sslEnable = false;

        /**
         * smtp邮件服务器读取超时
         */
        private Integer timeOut = 5000;

        /**
         * smtp邮件服务器连接超时
         */
        private Integer connectionTimeOut = 5000;

        public Builder(String to, String mailBody) {
            this(defaultSmtpHost, defaultSenderName, defaultSenderPass, to, mailBody, null, null);
        }

        public Builder(String to, String mailBody, String cc, String mailSubject) {
            this(defaultSmtpHost, defaultSenderName, defaultSenderPass, to, mailBody, cc, mailSubject);
        }

        public Builder(String smtpHost, String sendUserName, String sendUserPass, String to, String mailBody) {
            this(smtpHost, sendUserName, sendUserPass, to, mailBody, null, null);
        }

        public Builder(String smtpHost, String sendUserName, String sendUserPass, String to, String mailBody, String mailSubject) {
            this(smtpHost, sendUserName, sendUserPass, to, mailBody, null, mailSubject);
        }

        public Builder(String smtpHost, String sendUserName, String sendUserPass, String to, String mailBody, String cc, String mailSubject) {
            this.smtpHost = smtpHost;
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

        public Builder setSslEnable(boolean sslEnable) {
            this.sslEnable = sslEnable;
            return this;
        }

        public Builder setTimeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder setConnectionTimeOut(Integer connectionTimeOut) {
            this.connectionTimeOut = connectionTimeOut;
            return this;
        }

        public Builder setPort(Integer port) {
            this.port = port;
            return this;
        }

        public Builder setSmtpHost(String smtpHost) {
            this.smtpHost = smtpHost;
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

        public String getProtocol() {
            return protocol;
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

        public String getMailSubject() {
            return mailSubject;
        }

        public Builder setMailSubject(String mailSubject) {
            this.mailSubject = mailSubject;
            return this;
        }

        public boolean getAuthFlag() {
            return authFlag;
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

        public Date getSendDate() {
            return sendDate;
        }

        public Builder setSendDate(Date sendDate) {
            this.sendDate = sendDate;
            return this;
        }

        public List<String> getAttachments() {
            return attachments;
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
        try {
            if (sendDate != null) {
                mimeMsg.setSentDate(sendDate);
            }
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

    /**
     * 设置邮件主题
     */
    private boolean setSubject(String mailSubject) {
        try {
            mimeMsg.setSubject(mailSubject);
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
            bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + mailBody, "text/html;charset=UTF-8");
            // 在组件上添加邮件文本
            mp.addBodyPart(bp);
        } catch (Exception e) {
            System.err.println("设置邮件正文时发生错误！" + e);
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
                bp.setFileName(MimeUtility.encodeText(fileds.getName(), "utf-8", null)); // 解决附件名称乱码
                mp.addBodyPart(bp);// 添加附件
                files.add(fileds);
            }
        } catch (Exception e) {
            System.err.println("增加邮件附件：" + filename + "发生错误！" + e);
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
        if (cc == null) {
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
                System.err.println("设置附件失败!" + e.getMessage());
                return false;
            }
            //保存上面的所有设置
            try {
                mimeMsg.saveChanges();
            } catch (MessagingException e) {
                System.err.println("保存设置异常!" + e.getMessage());
                return false;
            }
            transport = session.getTransport();
            // 连接邮件服务器并进行身份验证
            try {
                transport.connect(smtpHost, sendUserName, sendUserPass);
            } catch (MessagingException e) {
                System.err.println("身份验证失败!" + e.getMessage());
                return false;
            }
            // 发送邮件
            try {
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                System.out.println("发送邮件成功！");
                return true;
            } catch (MessagingException e) {
                System.err.println("发送邮件失败!" + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("未知异常!" + e.getMessage());
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    System.out.println("关闭失败!" + e.getMessage());
                }
            }
        }
        return false;
    }

    /*public static void main(String[] args) {
        String userName = "wubodanran@126.com";
        String pwd = "密码";
        String to = "wubodanran@qq.com";
        String mailBody = "测试邮件...";
        //使用默认
        EmailUtilBuilder.Builder builder = new EmailUtilBuilder.Builder(to, mailBody);
        builder.setSendUserName(userName).setSendUserPass(pwd).setDeBugFlag(true).setMailSubject("测试邮箱主题").builder().send();
    }*/

}
