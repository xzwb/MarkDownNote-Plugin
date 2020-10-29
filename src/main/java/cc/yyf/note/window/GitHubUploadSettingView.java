package cc.yyf.note.window;

import cc.yyf.note.util.DESUtil;
import cc.yyf.note.util.YYFPasswordUtil;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class GitHubUploadSettingView extends UploadSettingView {
    private OtherSettingWindow otherSettingWindow = new OtherSettingWindow();


    /**
     * 获取最外层控件
     * @return
     */
    @Override
    JComponent getViewComponent() {
        return otherSettingWindow.getCommentPanel();
    }

    /**
     * 在setting中展示的名字
     * @return
     */
    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "上传github仓库";
    }


    @Override
    public boolean isModified() {
        return true;
    }

    /**
     * 点击apply或者ok按钮以后调用
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        // 获取到文本框中的内容
        String gitHubAddress = otherSettingWindow.getGithubRepository().getText();
        String gitHubToken = otherSettingWindow.getGithubToken().getText();
        // 加密
        String gitHubAddressInFile = DESUtil.encrypt(YYFPasswordUtil.YYF_KEY, gitHubAddress);
        String gitHubTokenInFile = DESUtil.encrypt(YYFPasswordUtil.YYF_KEY, gitHubToken);
        // 保存进文件
        FileOutputStream githubAddressIn = null;
        FileOutputStream githubTokenIn = null;
        try {
            // gitHubAddress
            String classPath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("/").getPath()+"gitHubAddress.txt");
            classPath = URLDecoder.decode(classPath, StandardCharsets.UTF_8.name());
            githubAddressIn = new FileOutputStream(classPath);
            githubAddressIn.write(gitHubAddressInFile.getBytes(), 0, gitHubAddressInFile.length());
            githubAddressIn.flush();
            // gitHubToken
            classPath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("/").getPath()+"gitHubToken.txt");
            classPath = URLDecoder.decode(classPath, StandardCharsets.UTF_8.name());
            githubTokenIn = new FileOutputStream(classPath);
            githubTokenIn.write(gitHubTokenInFile.getBytes(), 0, gitHubTokenInFile.length());
            githubTokenIn.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (githubAddressIn != null) {
                    githubAddressIn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 一开始进入或者点击右上角reset后调用
     */
    @Override
    public void reset() {
        InputStream githubAddressIn = null;
        InputStream githubTokenIn = null;
        try {
            // 文件如果没有就创建
            File fileToken = new File(this.getClass().getResource(File.separator).getPath() + "gitHubToken.txt");
            if (!fileToken.exists()) {
                fileToken.createNewFile();
            }
            File fileAddress = new File(this.getClass().getResource(File.separator).getPath() + "gitHubAddress.txt");
            if (!fileAddress.exists()) {
                fileAddress.createNewFile();
            }
            // 获取文件的InPutStream
            githubTokenIn = new FileInputStream(fileToken);
            githubAddressIn = new FileInputStream(fileAddress);
            byte[] address = new byte[1024];
            byte[] token = new byte[1024];
            int addressNum = githubAddressIn.read(address);
            int tokenNum = githubTokenIn.read(token);
            String githubAddress = "";
            if (addressNum != -1) {
                githubAddress = new String(address, 0, addressNum-1);
                githubAddress = DESUtil.decrypt(YYFPasswordUtil.YYF_KEY, githubAddress);
            }
            String githubToken = "";
            if (tokenNum != -1) {
                githubToken = new String(token, 0, tokenNum-1);
                githubToken = DESUtil.decrypt(YYFPasswordUtil.YYF_KEY, githubToken);
            }
            otherSettingWindow.getGithubRepository().setText(githubAddress);
            otherSettingWindow.getGithubToken().setText(githubToken);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (githubAddressIn != null) {
                    githubAddressIn.close();
                }
                if (githubTokenIn != null) {
                    githubTokenIn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
