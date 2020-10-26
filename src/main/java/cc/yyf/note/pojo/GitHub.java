package cc.yyf.note.pojo;

/**
 * 用来上传gitHub的信息
 */
public class GitHub {
    // 仓库地址
    private String gitHubAddress;
    // token
    private String gitHubToken;

    public GitHub(String gitHubAddress, String gitHubToken) {
        this.gitHubAddress = gitHubAddress;
        this.gitHubToken = gitHubToken;
    }

    public String getGitHubAddress() {
        return gitHubAddress;
    }

    public void setGitHubAddress(String gitHubAddress) {
        this.gitHubAddress = gitHubAddress;
    }

    public String getGitHubToken() {
        return gitHubToken;
    }

    public void setGitHubToken(String gitHubToken) {
        this.gitHubToken = gitHubToken;
    }
}
