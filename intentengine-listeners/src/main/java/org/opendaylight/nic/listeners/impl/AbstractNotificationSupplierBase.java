/*
 * Copyright (c) 2015 Hewlett-Packard Development Company and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.nic.listeners.impl;

import com.google.common.base.Preconditions;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataBroker.DataChangeScope;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.nic.listeners.api.NotificationSupplierDefinition;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/**
 * Public abstract basic Supplier implementation contains code for a make Supplier instance,
 * registration Supplier like {@link org.opendaylight.controller.md.sal.binding.api.DataChangeListener}
 * and close method. In additional case, it contains help methods for all Supplier implementations.
 *
 * @param <O> - data tree item Object extends {@link DataObject}
 */
public abstract class AbstractNotificationSupplierBase<O extends DataObject> implements
        NotificationSupplierDefinition<O> {

    protected final Class<O> clazz;
    private ListenerRegistration<DataChangeListener> listenerRegistration;

    /**
     * Default constructor for all NicNotification Supplier implementation
     *
     * @param db    - {@link DataBroker}
     * @param clazz - API contract class extended {@link DataObject}
     */
    public AbstractNotificationSupplierBase(final DataBroker db, final Class<O> clazz,
                                            LogicalDatastoreType datastoreType) {
        Preconditions.checkArgument(db != null, "DataBroker can not be null!");
        listenerRegistration = db.registerDataChangeListener(datastoreType, getWildCardPath(), this,
                DataChangeScope.BASE);
        this.clazz = clazz;
    }

    @Override
    public void close() throws Exception {
        if (listenerRegistration != null) {
            listenerRegistration.close();
        }
    }

    /**
     * Method returns a wildCard {@link InstanceIdentifier} for {@link Node} from inventory
     * because this path is a base for every OF paths.
     *
     * @return WildCarded InstanceIdentifier for Node
     */
    protected static InstanceIdentifier<Node> getNodeWildII() {
        return InstanceIdentifier.create(Nodes.class).child(Node.class);
    }

    /**
     * Method returns a {@link InstanceIdentifier} for {@link Node} from inventory
     * because this path is a base for every OF paths.
     *
     * @param ii - key for keyed {@link Node} {@link InstanceIdentifier}
     * @return InstanceIdentifier for Node
     */
    protected static InstanceIdentifier<Node> getNodeII(final InstanceIdentifier<?> ii) {
        final NodeKey key = ii.firstKeyOf(Node.class);
        Preconditions.checkArgument(key != null);
        final InstanceIdentifier<Node> nodeIid = InstanceIdentifier.builder(Nodes.class)
                .child(Node.class, key)
                .build();
        return nodeIid;
    }

    /**
     * @param path pointer to element
     * @return extracted {@link NodeKey} and wrapped in {@link NodeRef}
     */
    public static NodeRef createNodeRef(InstanceIdentifier<?> path) {
        final InstanceIdentifier<Node> nodePath = Preconditions.checkNotNull(path.firstIdentifierOf(Node.class));
        return new NodeRef(nodePath);
    }

    /**
     * @param path pointer to element
     * @return extracted {@link NodeId}
     */
    public static NodeId getNodeId(InstanceIdentifier<?> path) {
        final NodeKey nodeKey = Preconditions.checkNotNull(path.firstKeyOf(Node.class, NodeKey.class));
        return nodeKey.getId();
    }

}
