/*
 * Copyright (c) 2015 Hewlett Packard Enterprise Development LP.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.nic.listeners.api;

import org.opendaylight.nic.neutron.NeutronSecurityGroup;

/**
 * Interface to model the Neutron Security Group Added Notification
 */
public interface SecurityGroupAdded extends NicNotification {

    NeutronSecurityGroup getSecurityGroup();

}
