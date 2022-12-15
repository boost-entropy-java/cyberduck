package ch.cyberduck.core.synchronization;

/*
 * Copyright (c) 2002-2022 iterate GmbH. All rights reserved.
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

import ch.cyberduck.core.Path;
import ch.cyberduck.core.PathAttributes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ETagComparisonService implements ComparisonService {
    private static final Logger log = LogManager.getLogger(ETagComparisonService.class.getName());

    @Override
    public Comparison compare(final Path.Type type, final PathAttributes local, final PathAttributes remote) {
        if(null != local.getETag() && null != remote.getETag()) {
            if(local.getETag().equals(remote.getETag())) {
                if(log.isDebugEnabled()) {
                    log.debug(String.format("Equal ETag %s", remote.getETag()));
                }
                return Comparison.equal;
            }
            log.warn(String.format("ETag %s in cache differs from %s on server", remote.getETag(), local.getETag()));
            return Comparison.notequal;
        }
        return Comparison.unknown;
    }
}
