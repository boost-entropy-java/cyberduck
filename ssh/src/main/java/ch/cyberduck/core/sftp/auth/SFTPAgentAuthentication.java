package ch.cyberduck.core.sftp.auth;

/*
 * Copyright (c) 2002-2017 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.AuthenticationProvider;
import ch.cyberduck.core.DefaultIOExceptionMappingService;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.Local;
import ch.cyberduck.core.LoginCallback;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.sftp.SFTPExceptionMappingService;
import ch.cyberduck.core.threading.CancelCallback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

import com.jcraft.jsch.agentproxy.Identity;
import com.jcraft.jsch.agentproxy.sshj.AuthAgent;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.Buffer;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.userauth.UserAuthException;

public class SFTPAgentAuthentication implements AuthenticationProvider<Boolean> {
    private static final Logger log = LogManager.getLogger(SFTPAgentAuthentication.class);

    private final SSHClient client;
    private final AgentAuthenticator agent;

    public SFTPAgentAuthentication(final SSHClient client, final AgentAuthenticator agent) {
        this.client = client;
        this.agent = agent;
    }

    @Override
    public Boolean authenticate(final Host bookmark, final LoginCallback prompt, final CancelCallback cancel)
            throws BackgroundException {
        if(log.isDebugEnabled()) {
            log.debug(String.format("Login using agent %s for %s", agent, bookmark));
        }
        for(Identity identity : this.filterIdentities(bookmark, agent.getIdentities())) {
            try {
                client.auth(bookmark.getCredentials().getUsername(), new AuthAgent(agent.getProxy(), identity));
                // Successfully authenticated
                break;
            }
            catch(UserAuthException e) {
                cancel.verify();
                // Continue;
            }
            catch(Buffer.BufferException e) {
                throw new DefaultIOExceptionMappingService().map(e);
            }
            catch(TransportException e) {
                throw new SFTPExceptionMappingService().map(e);
            }
        }
        return client.isAuthenticated();
    }

    @Override
    public String getMethod() {
        return "publickey";
    }

    protected Collection<Identity> filterIdentities(final Host bookmark, final Collection<Identity> identities) {
        if(bookmark.getCredentials().isPublicKeyAuthentication()) {
            final Local selected = bookmark.getCredentials().getIdentity();
            for(Identity identity : identities) {
                if(identity.getComment() != null) {
                    final String candidate = new String(identity.getComment(), StandardCharsets.UTF_8);
                    if(selected.getAbsolute().equals(candidate)) {
                        if(log.isDebugEnabled()) {
                            log.debug(String.format("Matching identity %s found", candidate));
                        }
                        return Collections.singletonList(identity);
                    }
                }
            }
        }
        return identities;
    }
}
