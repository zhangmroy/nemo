/*
 * Copyright (c) 2015 Huawei, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.nemo.user.vnspacemanager.structurestyle.updateintent;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.nemo.user.tenantmanager.TenantManage;
import org.opendaylight.nemo.user.vnspacemanager.instancecheck.ResultInstanceCheck;
import org.opendaylight.nemo.user.vnspacemanager.syntaxcheck.ResultDefinitionCheck;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.nemo.common.rev151010.UserId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.nemo.intent.rev151010.user.intent.Results;

/**
 * Created by z00293636 on 2015/8/31.
 */
public class UpdateResult {

    private DataBroker dataBroker;
    private TenantManage tenantManage;
    private ResultDefinitionCheck resultDefinitionCheck;
    private ResultInstanceCheck resultInstanceCheck;

    public UpdateResult(DataBroker dataBroker, TenantManage tenantManage)
    {
        this.dataBroker = dataBroker;
        this.tenantManage = tenantManage;
        resultDefinitionCheck = new ResultDefinitionCheck();
        resultInstanceCheck = new ResultInstanceCheck(tenantManage);
    }

    public String ResultHandling(UserId userId, Results results)
    {
        String errorDefinition = resultDefinitionCheck.CheckDefinition(results);
        String errorInstance = resultInstanceCheck.checkResultInstance(userId,results);

        if (errorDefinition!= null)
        {
            return errorDefinition;
        }
        else if (errorInstance != null)
        {
            return errorInstance;
        }
        else
        {
            //todo
        }
        return null;
    }
}
