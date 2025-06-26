package ch.cyberduck.ui.cocoa.controller;

/*
 * Copyright (c) 2002-2016 iterate GmbH. All rights reserved.
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

import ch.cyberduck.binding.application.NSApplication;
import ch.cyberduck.core.AbstractHostCollection;
import ch.cyberduck.core.Host;

import java.util.HashMap;
import java.util.Map;

public final class BookmarkControllerFactory {

    private static final Map<Host, BookmarkController> open
            = new HashMap<Host, BookmarkController>();

    private BookmarkControllerFactory() {
        //
    }

    public static BookmarkController create(final AbstractHostCollection collection, final Host host) {
        synchronized(NSApplication.sharedApplication()) {
            if(!open.containsKey(host)) {
                final BookmarkController c = new ExtendedBookmarkController(host) {
                    @Override
                    public void invalidate() {
                        open.remove(bookmark);
                        super.invalidate();
                    }
                };
                c.addObserver(collection::collectionItemChanged);
                open.put(host, c);
            }
            final BookmarkController controller = open.get(host);
            controller.update();
            return controller;
        }
    }
}
