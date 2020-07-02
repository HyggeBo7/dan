package top.dearbo.frame.common.util.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.lang3.StringUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * Created by Dan on 2017/10/30.
 */
public class EmailUtil {
    /**
     * 默认的发件人用户名，defaultEntity用得到
     */
    private static String defaultSenderName = "";
    /**
     * 默认的发件人密码，defaultEntity用得到
     */
    private static String defaultSenderPass = "";
    /**
     * 默认的邮件服务器地址，defaultEntity用得到
     */
    private static String defaultSmtpHost = "";

    /**
     * 邮件服务器地址
     */
    private String smtpHost;
    /**
     * 发件人的用户名
     */
    private String sendUserName;
    /**
     * 发件人密码
     */
    private String sendUserPass;

    /**
     * 邮件对象
     */
    private MimeMessage mimeMsg;
    private Session session;
    private Properties props;
    /**
     * 附件添加的组件
     */
    private Multipart mp;
    /**
     * 存放附件文件
     */
    private List<FileDataSource> files = new LinkedList<>();

    /**
     * ssl.enable-默认关闭
     */
    private boolean sslEnable = true;

    /**
     * smtp邮件服务器读取超时-默认5秒
     */
    private Integer timeOut = 5000;

    /**
     * smtp邮件服务器连接超时-默认5秒
     */
    private Integer connectionTimeOut = 5000;

    private void init(Boolean deBug, Boolean authFlag) {
        if (props == null) {
            props = System.getProperties();
        }
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", sslEnable ? "true" : "false");
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        props.put("mail.smtp.host", smtpHost);
        // 需要身份验证
        props.put("mail.smtp.auth", authFlag == null ? "false" : authFlag ? "true" : "false");
        session = Session.getDefaultInstance(props, null);
        // 置true可以在控制台（console)上看到发送邮件的过程
        session.setDebug(deBug == null ? false : deBug);
        // 用session对象来创建并初始化邮件对象
        mimeMsg = new MimeMessage(session);
        // 生成附件组件的实例
        mp = new MimeMultipart();
    }

    private EmailUtil(String smtpHost, String sendUserName, String sendUserPass, String to, String cc, String mailSubject, String mailBody, Boolean deBug, Boolean authFlag, Date sendDate, List<String> attachments) {
        this.smtpHost = smtpHost;
        this.sendUserName = sendUserName;
        this.sendUserPass = sendUserPass;
        init(deBug, authFlag);
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

    /**
     * @param smtpHost     邮件服务器地址
     * @param sendUserName 发件邮件地址
     * @param sendUserPass 发件邮箱密码
     * @param to           收件人，多个邮箱地址以半角逗号分隔
     * @param cc           抄送，多个邮箱地址以半角逗号分隔
     * @param mailSubject  邮件主题
     * @param mailBody     邮件正文
     * @param sendDate     发送时间
     * @param deBug        是否dubug显示
     * @param authFlag     是否认证
     * @param attachments  附件路径
     * @return
     */
    public static EmailUtil entity(String smtpHost, String sendUserName, String sendUserPass, String to, String cc, String mailSubject, String mailBody, Boolean deBug, Boolean authFlag, Date sendDate, List<String> attachments) {

        return new EmailUtil(smtpHost, sendUserName, sendUserPass, to, cc, mailSubject, mailBody, deBug, authFlag, sendDate, attachments);
    }

    /**
     * @param smtpHost     邮件服务器地址
     * @param sendUserName 发件邮件地址
     * @param sendUserPass 发件邮箱密码
     * @param to           收件人，多个邮箱地址以半角逗号分隔
     * @param cc           抄送，多个邮箱地址以半角逗号分隔
     * @param mailSubject  邮件主题
     * @param mailBody     邮件正文
     * @param deBug        是否dubug显示
     * @param authFlag     是否认证
     * @return
     */
    public static EmailUtil entity(String smtpHost, String sendUserName, String sendUserPass, String to, String cc, String mailSubject, String mailBody, Boolean deBug, Boolean authFlag) {

        return new EmailUtil(smtpHost, sendUserName, sendUserPass, to, cc, mailSubject, mailBody, deBug, authFlag, null, null);
    }

    public static EmailUtil entity(String smtpHost, String sendUserName, String sendUserPass, String to, String cc, String mailSubject, String mailBody, Boolean deBug, Boolean authFlag, Date sendDate) {

        return new EmailUtil(smtpHost, sendUserName, sendUserPass, to, cc, mailSubject, mailBody, deBug, authFlag, sendDate, null);
    }

    /**
     * @param smtpHost     邮件服务器地址
     * @param sendUserName 发件邮件地址
     * @param sendUserPass 发件邮箱密码
     * @param to           收件人，多个邮箱地址以半角逗号分隔
     * @param cc           抄送，多个邮箱地址以半角逗号分隔
     * @param mailSubject  邮件主题
     * @param mailBody     邮件正文
     */
    public static EmailUtil entity(String smtpHost, String sendUserName, String sendUserPass, String to, String cc, String mailSubject, String mailBody) {

        return new EmailUtil(smtpHost, sendUserName, sendUserPass, to, cc, mailSubject, mailBody, null, null, null, null);
    }


    /**
     * 默认邮件实体，用了默认的发送帐号和邮件服务器
     *
     * @param to          收件人，多个邮箱地址以半角逗号分隔
     * @param cc          抄送，多个邮箱地址以半角逗号分隔
     * @param subject     邮件主题
     * @param body        邮件正文
     * @param attachments 附件路径
     */
    public static EmailUtil defaultEntity(String to, String cc, String subject, String body, List<String> attachments) {
        return new EmailUtil(defaultSmtpHost, defaultSenderName, defaultSenderPass, to, cc, subject, body, false, true, null, attachments);
    }

    /**
     * 默认邮件实体，用了默认的发送帐号和邮件服务器
     *
     * @param to      收件人，多个邮箱地址以半角逗号分隔
     * @param cc      抄送，多个邮箱地址以半角逗号分隔
     * @param subject 邮件主题
     * @param body    邮件正文
     */
    public static EmailUtil defaultEntity(String to, String cc, String subject, String body) {
        return new EmailUtil(defaultSmtpHost, defaultSenderName, defaultSenderPass, to, cc, subject, body, false, true, null, null);
    }

    public static EmailUtil defaultEntity(String to, String cc, String subject, String body, Boolean deBug, Boolean authFlag) {
        return new EmailUtil(defaultSmtpHost, defaultSenderName, defaultSenderPass, to, cc, subject, body, deBug, authFlag, null, null);
    }

    public static EmailUtil defaultEntity(String sendUserName, String sendUserPass, String to, String cc, String subject, String body, Boolean deBug, Boolean authFlag) {
        return new EmailUtil(defaultSmtpHost, sendUserName, sendUserPass, to, cc, subject, body, deBug, authFlag, null, null);
    }

    /**
     * 设置发送时间
     *
     * @param sendDate
     * @return
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
     *
     * @param mailSubject
     * @return
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
     *
     * @param mailBody
     * @return
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
     * @return
     */
    public boolean addFileAffix(String filename) {
        try {
            if (filename != null && filename.length() > 0) {
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
     *
     * @return
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
     * @return
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
     * @return
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
     *
     * @return
     */
    public boolean send() {
        Transport transport = null;
        try {
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            System.out.println("正在发送邮件....");
            transport = session.getTransport("smtp");
            // 连接邮件服务器并进行身份验证
            transport.connect(smtpHost, sendUserName, sendUserPass);
            // 发送邮件
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            System.out.println("发送邮件成功！");
            return true;
        } catch (MessagingException e) {
            System.out.println("身份验证失败!" + e.getMessage());
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
}
