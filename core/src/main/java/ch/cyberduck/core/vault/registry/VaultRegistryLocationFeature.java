package ch.cyberduck.core.vault.registry;

/*
 * Copyright (c) 2002-2017 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.Path;
import ch.cyberduck.core.Session;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.UnsupportedException;
import ch.cyberduck.core.features.Location;
import ch.cyberduck.core.vault.VaultRegistry;
import ch.cyberduck.core.vault.VaultUnlockCancelException;

import java.util.Collections;
import java.util.Set;

public class VaultRegistryLocationFeature implements Location {

    private final Session<?> session;
    private final Location proxy;
    private final VaultRegistry registry;

    public VaultRegistryLocationFeature(final Session<?> session, final Location proxy, final VaultRegistry registry) {
        this.session = session;
        this.proxy = proxy;
        this.registry = registry;
    }

    @Override
    public Name getDefault(final Path file) {
        try {
            return registry.find(session, file).getFeature(session, Location.class, proxy).getDefault(file);
        }
        catch(VaultUnlockCancelException e) {
            return proxy.getDefault(file);
        }
        catch(UnsupportedException e) {
            return Location.unknown;
        }
    }

    @Override
    public Set<Name> getLocations(final Path file) {
        try {
            return registry.find(session, file).getFeature(session, Location.class, proxy).getLocations(file);
        }
        catch(VaultUnlockCancelException e) {
            return proxy.getLocations(file);
        }
        catch(UnsupportedException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Name getLocation(final Path file) throws BackgroundException {
        return registry.find(session, file).getFeature(session, Location.class, proxy).getLocation(file);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VaultRegistryLocationFeature{");
        sb.append("proxy=").append(proxy);
        sb.append('}');
        return sb.toString();
    }
}
