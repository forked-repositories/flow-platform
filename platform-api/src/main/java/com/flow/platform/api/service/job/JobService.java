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
package com.flow.platform.api.service.job;

import com.flow.platform.api.domain.CmdQueueItem;
import com.flow.platform.api.domain.job.Job;
import com.flow.platform.api.domain.node.Node;
import com.flow.platform.api.domain.job.NodeResult;
import java.math.BigInteger;
import java.util.List;

/**
 * @author yh@firim
 */
public interface JobService {

    /**
     * create job
     *
     * @param path any node path
     * @return job
     */
    Job createJob(String path);

    /**
     * handle callback
     **/
    void callback(CmdQueueItem cmdQueueItem);

    /**
     * run node
     */
    void run(Node node, Job job);

    /**
     * find job by id
     */
    Job find(BigInteger id);

    /**
     * find by flow name and number
     */
    Job find(String flowName, Integer number);

    /**
     * list all jobs
     */
    List<Job> listJobs(String flowPath, List<String> flowPaths);

    /**
     * list node results
     */
    List<NodeResult> listNodeResult(String flowName, Integer number);

    /**
     * send cmd to queue
     */
    void enterQueue(CmdQueueItem cmdQueueItem);

    /**
     * stop job
     */
    Job stopJob(String name, Integer buildNumber);
}