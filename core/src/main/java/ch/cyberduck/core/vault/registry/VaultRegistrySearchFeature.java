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

import ch.cyberduck.core.AttributedList;
import ch.cyberduck.core.Filter;
import ch.cyberduck.core.ListProgressListener;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.Session;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.UnsupportedException;
import ch.cyberduck.core.features.Search;
import ch.cyberduck.core.vault.VaultRegistry;
import ch.cyberduck.core.vault.VaultUnlockCancelException;

import java.util.EnumSet;

public class VaultRegistrySearchFeature implements Search {

    private final Session<?> session;
    private final Search proxy;
    private final VaultRegistry registry;

    public VaultRegistrySearchFeature(final Session<?> session, final Search proxy, final VaultRegistry registry) {
        this.session = session;
        this.proxy = proxy;
        this.registry = registry;
    }

    @Override
    public AttributedList<Path> search(final Path workdir, final Filter<Path> regex, final ListProgressListener listener) throws BackgroundException {
        return registry.find(session, workdir).getFeature(session, Search.class, proxy).search(workdir, regex, listener);
    }

    @Override
    public EnumSet<Flags> features(final Path workdir) {
        try {
            return registry.find(session, workdir).getFeature(session, Search.class, proxy).features(workdir);
        }
        catch(VaultUnlockCancelException e) {
            return proxy.features(workdir);
        }
        catch(UnsupportedException e) {
            return EnumSet.noneOf(Flags.class);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VaultRegistrySearchFeature{");
        sb.append("proxy=").append(proxy);
        sb.append('}');
        return sb.toString();
    }
}
