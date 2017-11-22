/*
 * Copyright 2017 flow.ci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flow.platform.plugin.test.util;

import com.flow.platform.plugin.util.GitHelperUtil;
import com.flow.platform.util.git.GitClient;
import com.flow.platform.util.git.GitHttpClient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yh@fir.im
 */


public class GitHelperUtilTest {

    @Test
    public void should_init_bare_repo_success() throws IOException {
        Path path = Paths.get("/tmp/test.git");

        // if folder is exist, then to delete
        if (path.toFile().exists()) {
            FileUtils.deleteDirectory(path.toFile());
        }

        Assert.assertEquals(false, path.toFile().exists());

        // when: init bare git repository
        GitHelperUtil.initBareGitRepository(path);

        // then: /tmp/test.git exist
        Assert.assertEquals(true, Paths.get("/tmp/test.git").toFile().exists());

        // then: folder /tmp/test not exist
        Assert.assertEquals(false, Paths.get("/tmp/test").toFile().exists());

        // when: delete file
        FileUtils.deleteDirectory(Paths.get("/tmp/test.git").toFile());

        // then: not exists
        Assert.assertEquals(false, Paths.get("/tmp/test.git").toFile().exists());
    }

    @Test
    public void should_git_clone_success() throws IOException {
        String gitUrl = "https://github.com/yunheli/info.git";
        Path basePath = Paths.get("/tmp/test");

        initFolder(basePath);

        GitClient gitClient = new GitHttpClient(gitUrl, basePath, "", "");

        // when clone code
        GitHelperUtil.clone(gitClient);

        //then: should is folder
        Assert.assertEquals(true, Paths.get(basePath.toString(), "info").toFile().isDirectory());

        // folder should exist
        Assert.assertEquals(true, Paths.get(basePath.toString(), "info").toFile().exists());

        cleanFolder(basePath);
    }

    @Test
    public void should_get_latest_tag() throws IOException {
        String gitUrl = "https://github.com/yunheli/info.git";
        Path basePath = Paths.get("/tmp/test");

        initFolder(basePath);

        GitClient gitClient = new GitHttpClient(gitUrl, basePath, "", "");

        // when clone code
        GitHelperUtil.clone(gitClient);

        Git git = Git.open(Paths.get(basePath.toString(), "info").toFile());

        // then should get latest tag
        Assert.assertEquals("2.3.1", GitHelperUtil.getLatestTag(Paths.get(basePath.toString(), "info")));

        cleanFolder(basePath);
    }

    @Test
    public void should_set_remote_url_success() throws IOException {
        String gitUrl = "https://github.com/yunheli/info.git";
        Path basePath = Paths.get("/tmp/test");

        initFolder(basePath);
        GitClient gitClient = new GitHttpClient(gitUrl, basePath, "", "");

        // when clone code
        GitHelperUtil.clone(gitClient);

        Path gitPath = Paths.get(basePath.toString(), "info");
        Path localGitPath = Paths.get("/tmp/.git");

        Git git = GitHelperUtil.setLocalRemote(gitPath, localGitPath);

        Assert.assertEquals(2, git.getRepository().getRemoteNames().size());

        cleanFolder(basePath);
    }

    @Test
    public void should_push_local_repo_success() throws IOException {
        String gitUrl = "https://github.com/yunheli/info.git";
        Path basePath = Paths.get("/tmp/test");
        Path bareRepoPath = Paths.get("/tmp/test/test.git");

        initFolder(basePath);
        GitClient gitClient = new GitHttpClient(gitUrl, basePath, "", "");

        // when clone code
        Path path = GitHelperUtil.clone(gitClient);

        // init bare repo
        GitHelperUtil.initBareGitRepository(bareRepoPath);

        // set local remote
        GitHelperUtil.setLocalRemote(path, bareRepoPath);

        // get latest tag
        String tag = GitHelperUtil.getLatestTag(path);

        // push tag to local
        GitHelperUtil.pushTag(path, "local", tag);

        // then: bare repo should has tag
        Assert.assertEquals(tag, GitHelperUtil.getLatestTag(bareRepoPath));

        // clean folder
        cleanFolder(basePath);
    }

    private void initFolder(Path basePath) throws IOException {
        // if exists, delete
        if (basePath.toFile().exists()) {
            FileUtils.deleteDirectory(basePath.toFile());
        }

        if (!basePath.toFile().exists()) {
            Files.createDirectories(basePath);
        }
    }

    private void cleanFolder(Path basePath) throws IOException {
        // when: delete folder
        FileUtils.deleteDirectory(basePath.toFile());

        // then: not exists
        Assert.assertEquals(false, basePath.toFile().exists());
    }
}
