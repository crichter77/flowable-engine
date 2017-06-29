/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.engine.impl.persistence.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.impl.persistence.entity.AbstractEntity;
import org.flowable.engine.impl.db.BulkDeleteable;
import org.flowable.engine.runtime.JobInfo;

/**
 * Abstract job entity class.
 *
 * @author Tijs Rademakers
 */
public abstract class AbstractJobEntityImpl extends AbstractEntity implements AbstractRuntimeJobEntity, BulkDeleteable, Serializable {

    private static final long serialVersionUID = 1L;

    protected Date createTime;
    protected Date duedate;

    protected String executionId;
    protected String processInstanceId;
    protected String processDefinitionId;

    protected boolean isExclusive = DEFAULT_EXCLUSIVE;

    protected int retries;

    protected int maxIterations;
    protected String repeat;
    protected Date endDate;

    protected String jobHandlerType;
    protected String jobHandlerConfiguration;

    protected ByteArrayRef exceptionByteArrayRef;
    protected String exceptionMessage;

    protected String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    protected String jobType;

    public Object getPersistentState() {
        Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("retries", retries);
        persistentState.put("createTime", createTime);
        persistentState.put("duedate", duedate);
        persistentState.put("exceptionMessage", exceptionMessage);
        persistentState.put("jobHandlerType", jobHandlerType);

        if (exceptionByteArrayRef != null) {
            persistentState.put("exceptionByteArrayRef", exceptionByteArrayRef);
        }
        
        return persistentState;
    }

    // getters and setters ////////////////////////////////////////////////////////

    public void setExecution(ExecutionEntity execution) {
        executionId = execution.getId();
        processInstanceId = execution.getProcessInstanceId();
        processDefinitionId = execution.getProcessDefinitionId();
    }
    
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public boolean isExclusive() {
        return isExclusive;
    }

    public void setExclusive(boolean isExclusive) {
        this.isExclusive = isExclusive;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public String getJobHandlerType() {
        return jobHandlerType;
    }

    public void setJobHandlerType(String jobHandlerType) {
        this.jobHandlerType = jobHandlerType;
    }

    public String getJobHandlerConfiguration() {
        return jobHandlerConfiguration;
    }

    public void setJobHandlerConfiguration(String jobHandlerConfiguration) {
        this.jobHandlerConfiguration = jobHandlerConfiguration;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getExceptionStacktrace() {
        if (exceptionByteArrayRef == null) {
            return null;
        }

        byte[] bytes = exceptionByteArrayRef.getBytes();
        if (bytes == null) {
            return null;
        }

        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new FlowableException("UTF-8 is not a supported encoding");
        }
    }

    public void setExceptionStacktrace(String exception) {
        if (exceptionByteArrayRef == null) {
            exceptionByteArrayRef = new ByteArrayRef();
        }
        exceptionByteArrayRef.setValue("stacktrace", getUtf8Bytes(exception));
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = StringUtils.abbreviate(exceptionMessage, JobInfo.MAX_EXCEPTION_MESSAGE_LENGTH);
    }

    public ByteArrayRef getExceptionByteArrayRef() {
        return exceptionByteArrayRef;
    }

    protected byte[] getUtf8Bytes(String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new FlowableException("UTF-8 is not a supported encoding");
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + " [id=" + id + "]";
    }

}
