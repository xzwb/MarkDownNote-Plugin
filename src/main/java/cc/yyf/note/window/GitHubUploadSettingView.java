package cc.yyf.note.window;

import cc.yyf.note.util.DESUtil;
import cc.yyf.note.util.YYFPasswordUtil;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.io.*;

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

    /**
     * 暂时不知道怎么用
     * @return
     */
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
            githubAddressIn = new FileOutputStream(this.getClass()
                    .getResource("/github/gitHubAddress.txt")
                    .getPath());
        } catch (Exception e) {
            e.printStackTrace();
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
            githubAddressIn = GitHubUploadSettingView.class.getResourceAsStream("/github/gitHubAddress.txt");
            githubTokenIn = GitHubUploadSettingView.class.getResourceAsStream("/github/gitHubToken.txt");
            byte[] address = new byte[1024];
            byte[] token = new byte[1024];
            githubAddressIn.read(address);
            githubTokenIn.read(token);
            String githubAddress = new String(address);
//            githubAddress = DESUtil.decrypt(YYFPasswordUtil.YYF_KEY, githubAddress);
            String githubToken = new String(token);
//            githubToken = DESUtil.decrypt(YYFPasswordUtil.YYF_KEY, githubToken);
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
