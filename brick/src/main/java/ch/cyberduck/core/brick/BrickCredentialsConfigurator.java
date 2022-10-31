package ch.cyberduck.core.brick;

/*
 * Copyright (c) 2002-2019 iterate GmbH. All rights reserved.
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

import ch.cyberduck.core.AlphanumericRandomStringService;
import ch.cyberduck.core.Credentials;
import ch.cyberduck.core.CredentialsConfigurator;
import ch.cyberduck.core.Host;

import org.apache.commons.lang3.StringUtils;

public class BrickCredentialsConfigurator implements CredentialsConfigurator {

    @Override
    public Credentials configure(final Host host) {
        if(StringUtils.isBlank(host.getCredentials().getToken())) {
            final Credentials credentials = new Credentials(host.getCredentials());
            credentials.setToken(new AlphanumericRandomStringService().random());
            return credentials;
        }
        return host.getCredentials();
    }

    @Override
    public CredentialsConfigurator reload() {
        return this;
    }
}
