/*
 * Copyright (c) 2015 Huawei, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.nemo.user.vnspacemanager.structurestyle.deleteintent;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.nemo.user.tenantmanager.TenantManage;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.nemo.common.rev151010.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.nemo.common.rev151010.UserId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.nemo.intent.rev151010.Users;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.nemo.intent.rev151010.user.intent.Objects;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.nemo.intent.rev151010.user.intent.objects.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.nemo.intent.rev151010.user.intent.objects.NodeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.nemo.intent.rev151010.users.User;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.nemo.intent.rev151010.users.UserKey;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by z00293636 on 2015/9/2.
 */
public class DeleteNode {

    private DataBroker dataBroker;
    private TenantManage tenantManage;
    private static final Logger LOG = LoggerFactory.getLogger(DeleteNode.class);

    public DeleteNode(DataBroker dataBroker, TenantManage tenantManage)
    {
        this.dataBroker = dataBroker;
        this.tenantManage = tenantManage;
    }

    public String DeleNodeHandling(UserId userId,NodeId nodeId)
    {
        String errorInfo = null;
        Boolean NodeInstanceExist = false;

        tenantManage.fetchVNSpace(userId);

        User user = tenantManage.getUser();
        if (user != null)
        {
            if (user.getObjects() != null)
            {
                if (user.getObjects().getNode() != null)
                {
                    List<Node> nodeList = tenantManage.getUser().getObjects().getNode();

                    for (Node node : nodeList)
                    {
                        if (node.getNodeId().equals(nodeId))
                        {
                            NodeInstanceExist = true;
                            break;
                        }
                    }
                    if (NodeInstanceExist)
                    {
                        DeleteNodeInstance(userId,nodeId);
                    }
                    else
                    {
                        errorInfo = "The node instance" +nodeId.toString()+"is not exist.Could not be deleted";
                    }
                }
                else
                {
                    errorInfo = "There are no nodes instances in data store.";
                }
            }
        }
        else
        {
            errorInfo = "There are no user in data store.";
        }

        return errorInfo;
    }

    private void DeleteNodeInstance(UserId userId,NodeId nodeId)
    {
        WriteTransaction t = dataBroker.newWriteOnlyTransaction();
        UserKey userKey = new UserKey(userId);
        NodeKey nodeKey = new NodeKey(nodeId);

        InstanceIdentifier<Node> nodeid = InstanceIdentifier.builder(Users.class).child(User.class, userKey).child(Objects.class).child(Node.class,nodeKey).build();
        t.delete(LogicalDatastoreType.CONFIGURATION, nodeid);
        CheckedFuture<Void, TransactionCommitFailedException> f = t.submit();
        Futures.addCallback(f, new FutureCallback<Void>() {
            @Override
            public void onFailure(Throwable t) {
                LOG.error("Could not write endpoint base container", t);
            }

            @Override
            public void onSuccess(Void result) {
            }
        });
    }
}
