package ch.cyberduck.core.worker;

/*
 * Copyright (c) 2002-2020 iterate GmbH. All rights reserved.
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

import ch.cyberduck.core.Cache;
import ch.cyberduck.core.CachingAttributesFinderFeature;
import ch.cyberduck.core.LocaleFactory;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.PathAttributes;
import ch.cyberduck.core.Session;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.features.AttributesFinder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;

public class AttributesWorker extends Worker<PathAttributes> {
    private static final Logger log = LogManager.getLogger(AttributesWorker.class.getName());

    private final Cache<Path> cache;
    private final Path file;

    public AttributesWorker(final Cache<Path> cache, final Path file) {
        this.cache = cache;
        this.file = file;
    }

    @Override
    public PathAttributes run(final Session<?> session) throws BackgroundException {
        if(log.isDebugEnabled()) {
            log.debug(String.format("Read latest attributes for file %s", file));
        }
        final AttributesFinder find = new CachingAttributesFinderFeature(cache, session.getFeature(AttributesFinder.class));
        final PathAttributes attr = find.find(file);
        if(log.isDebugEnabled()) {
            log.debug(String.format("Return %s for file %s", attr, file));
        }
        return attr;
    }

    @Override
    public PathAttributes initialize() {
        return PathAttributes.EMPTY;
    }

    @Override
    public String getActivity() {
        return MessageFormat.format(LocaleFactory.localizedString("Reading metadata of {0}", "Status"), file.getName());
    }
}
