package ch.cyberduck.core.local;

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

import ch.cyberduck.binding.Proxy;
import ch.cyberduck.binding.application.NSOpenPanel;
import ch.cyberduck.binding.application.SheetCallback;
import ch.cyberduck.binding.foundation.NSArray;
import ch.cyberduck.binding.foundation.NSData;
import ch.cyberduck.binding.foundation.NSEnumerator;
import ch.cyberduck.binding.foundation.NSObject;
import ch.cyberduck.binding.foundation.NSURL;
import ch.cyberduck.core.Local;
import ch.cyberduck.core.LocaleFactory;
import ch.cyberduck.core.exception.AccessDeniedException;
import ch.cyberduck.core.exception.LocalAccessDeniedException;
import ch.cyberduck.core.preferences.SecurityApplicationGroupSupportDirectoryFinder;
import ch.cyberduck.core.preferences.TemporarySupportDirectoryFinder;
import ch.cyberduck.core.threading.DefaultMainAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rococoa.ObjCObjectByReference;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSError;
import org.rococoa.cocoa.foundation.NSInteger;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractPromptBookmarkResolver implements FilesystemBookmarkResolver<NSURL> {
    private static final Logger log = LogManager.getLogger(AbstractPromptBookmarkResolver.class);

    private final int create;
    private final int resolve;

    private final Proxy proxy = new Proxy();

    private static final Local TEMPORARY = new TemporarySupportDirectoryFinder().find();
    private static final Local GROUP_CONTAINER = new SecurityApplicationGroupSupportDirectoryFinder().find();

    /**
     * @param create  Create options
     * @param resolve Resolve options
     */
    public AbstractPromptBookmarkResolver(final int create, final int resolve) {
        this.create = create;
        this.resolve = resolve;
    }

    @Override
    public String create(final Local file) throws AccessDeniedException {
        if(this.skip(file)) {
            return null;
        }
        final ObjCObjectByReference error = new ObjCObjectByReference();
        // Create new security scoped bookmark
        final NSURL url = NSURL.fileURLWithPath(file.getAbsolute());
        log.trace("Resolved file {} to url {}", file, url);
        final NSData data = url.bookmarkDataWithOptions_includingResourceValuesForKeys_relativeToURL_error(
                create, null, null, error);
        if(null == data) {
            log.warn("Failure getting bookmark data for file {}", file);
            final NSError f = error.getValueAs(NSError.class);
            if(null == f) {
                throw new LocalAccessDeniedException(file.getAbsolute());
            }
            throw new LocalAccessDeniedException(String.format("%s", f.localizedDescription()));
        }
        final String encoded = data.base64Encoding();
        log.trace("Encoded bookmark for {} as {}", file, encoded);
        return encoded;
    }

    @Override
    public NSURL resolve(final Local file, final boolean interactive) throws AccessDeniedException {
        if(this.skip(file)) {
            return null;
        }
        final NSData bookmark;
        if(null == file.getBookmark()) {
            if(interactive) {
                if(!file.exists()) {
                    return null;
                }
                // Prompt user if no bookmark reference is available
                log.warn("Missing security scoped bookmark for file {}", file);
                final String reference = this.choose(file);
                if(null == reference) {
                    // Prompt canceled by user
                    return null;
                }
                file.setBookmark(reference);
                bookmark = NSData.dataWithBase64EncodedString(reference);
            }
            else {
                log.warn("No security scoped bookmark for {}", file.getName());
                return null;
            }
        }
        else {
            bookmark = NSData.dataWithBase64EncodedString(file.getBookmark());
        }
        final ObjCObjectByReference error = new ObjCObjectByReference();
        final NSURL resolved = NSURL.URLByResolvingBookmarkData(bookmark, resolve, error);
        if(null == resolved) {
            log.warn("Error resolving bookmark for {} to URL", file);
            final NSError f = error.getValueAs(NSError.class);
            if(null == f) {
                throw new LocalAccessDeniedException(file.getAbsolute());
            }
            throw new LocalAccessDeniedException(String.format("%s", f.localizedDescription()));
        }
        return resolved;
    }

    /**
     * Determine if creating security scoped bookmarks for file should be skipped
     */
    private boolean skip(final Local file) {
        if(null != TEMPORARY) {
            if(file.isChild(TEMPORARY)) {
                // Skip prompt for file in temporary folder where access is not sandboxed
                return true;
            }
        }
        if(null != GROUP_CONTAINER) {
            if(file.isChild(GROUP_CONTAINER)) {
                // Skip prompt for file in application group folder where access is not sandboxed
                return true;
            }
        }
        return false;
    }

    /**
     * @return Security scoped bookmark
     */
    public String choose(final Local file) throws AccessDeniedException {
        final AtomicReference<Local> selected = new AtomicReference<Local>();
        log.warn("Prompt for file {} to obtain bookmark reference", file);
        final DefaultMainAction action = new DefaultMainAction() {
            @Override
            public void run() {
                final NSOpenPanel panel = NSOpenPanel.openPanel();
                panel.setCanChooseDirectories(file.isDirectory());
                panel.setCanChooseFiles(file.isFile());
                panel.setAllowsMultipleSelection(false);
                panel.setMessage(MessageFormat.format(LocaleFactory.localizedString("Select {0}", "Credentials"),
                    file.getAbbreviatedPath()));
                panel.setPrompt(LocaleFactory.localizedString("Choose"));
                final NSInteger modal = panel.runModal(file.getParent().getAbsolute(), file.getName());
                if(modal.intValue() == SheetCallback.DEFAULT_OPTION) {
                    final NSArray filenames = panel.URLs();
                    final NSEnumerator enumerator = filenames.objectEnumerator();
                    NSObject next;
                    while((next = enumerator.nextObject()) != null) {
                        selected.set(new FinderLocal(Rococoa.cast(next, NSURL.class).path()));
                    }
                }
                panel.orderOut(null);
            }
        };
        proxy.invoke(action, action.lock(), true);
        if(selected.get() == null) {
            log.warn("Prompt for {} canceled", file);
            return null;
        }
        // Save Base64 encoded scoped reference
        return this.create(selected.get());
    }

}
