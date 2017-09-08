/*
 * Copyright 2017 camunda services GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package org.camunda.bpm.engine.impl.cmd;

import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.util.List;

import static org.camunda.bpm.engine.impl.util.EnsureUtil.ensureNotEmpty;
import static org.camunda.bpm.engine.impl.util.EnsureUtil.ensureNotNull;

/**
 * Command to delete process definitions by ids.
 *
 * @author Tassilo Weidner
 */
public class DeleteProcessDefinitionsByIdsCmd extends AbstractDeleteProcessDefinitionCmd {

  private final List<String> processDefinitionIds;
  private final String tenantId;

  public DeleteProcessDefinitionsByIdsCmd(List<String> processDefinitionIds, boolean cascade, boolean skipCustomListeners, String tenantId) {
    this.processDefinitionIds = processDefinitionIds;
    this.cascade = cascade;
    this.skipCustomListeners = skipCustomListeners;
    this.tenantId = tenantId;
  }

  @Override
  public Void execute(CommandContext commandContext) {
    ensureNotNull("processDefinitionIds", processDefinitionIds);

    List<ProcessDefinition> processDefinitions = commandContext.getProcessDefinitionManager()
      .findDefinitionsByIdsAndTenantId(processDefinitionIds, tenantId);
    ensureNotEmpty(NotFoundException.class, "No process definition found",
      "processDefinitions", processDefinitions);

    for (ProcessDefinition processDefinition: processDefinitions) {
      String processDefinitionId = processDefinition.getId();
      deleteProcessDefinitionCmd(commandContext, processDefinitionId, cascade, skipCustomListeners);
    }

    return null;
  }

}
