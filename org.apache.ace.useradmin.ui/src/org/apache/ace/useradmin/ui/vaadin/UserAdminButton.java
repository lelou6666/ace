/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ace.useradmin.ui.vaadin;

import org.apache.ace.useradmin.ui.editor.UserEditor;
import org.apache.felix.dm.Component;
import org.osgi.service.useradmin.User;

import com.vaadin.ui.Button;

public class UserAdminButton extends Button {
    private final UserAdminWindow m_window;

    private volatile UserEditor m_userUtil;

    public UserAdminButton() {
        setCaption("Manage Users");
        setEnabled(false);

        m_window = new UserAdminWindow();

        addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                m_window.open(getWindow());
            }
        });
    }

    @Override
    public void attach() {
        User user = (User) getApplication().getUser();
        if (m_userUtil.hasRole(user, "editUsers")) {
            setEnabled(true);
        }
        else {
            // no edit user karma, so no use to show this button at all...
            setVisible(false);
        }

        super.attach();
    }

    protected Object[] getComposition() {
        return new Object[] { this, m_window };
    }

    protected void stop(Component component) {
        setEnabled(false);
        setDescription("This service seems to be unavailable at this moment...");
    }
}
