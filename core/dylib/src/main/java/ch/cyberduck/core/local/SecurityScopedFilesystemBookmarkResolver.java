package ch.cyberduck.core.local;

/*
 * Copyright (c) 2002-2014 David Kocher. All rights reserved.
 * http://cyberduck.io/
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
 *
 * Bug fixes, suggestions and comments should be sent to:
 * feedback@cyberduck.io
 */

import static ch.cyberduck.binding.foundation.NSURL.NSURLBookmarkCreationOptions.NSURLBookmarkCreationWithSecurityScope;
import static ch.cyberduck.binding.foundation.NSURL.NSURLBookmarkResolutionOptions.*;

/**
 * To provide persistent access to resources located outside of your container, in a way that doesn’t
 * depend on Resume, use security-scoped bookmarks for persistent access.
 */
public class SecurityScopedFilesystemBookmarkResolver extends NSURLPromptBookmarkResolver {

    public SecurityScopedFilesystemBookmarkResolver() {
        super(NSURLBookmarkCreationWithSecurityScope, NSURLBookmarkResolutionWithoutUI | NSURLBookmarkResolutionWithoutMounting | NSURLBookmarkResolutionWithSecurityScope | NSURLBookmarkResolutionWithoutImplicitStartAccessing);
    }
}
