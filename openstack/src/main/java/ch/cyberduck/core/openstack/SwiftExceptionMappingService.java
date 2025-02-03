package ch.cyberduck.core.openstack;

/*
 * Copyright (c) 2013 David Kocher. All rights reserved.
 * http://cyberduck.ch/
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
 * feedback@cyberduck.ch
 */

import ch.cyberduck.core.AbstractExceptionMappingService;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.LoginFailureException;
import ch.cyberduck.core.http.DefaultHttpResponseExceptionMappingService;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.iterate.openstack.swift.exception.GenericException;

public class SwiftExceptionMappingService extends AbstractExceptionMappingService<GenericException> {
    private static final Logger log = LogManager.getLogger(SwiftExceptionMappingService.class);

    @Override
    public BackgroundException map(final GenericException e) {
        log.warn("Map failure {}", e.toString());
        final StringBuilder buffer = new StringBuilder();
        this.append(buffer, e.getMessage());
        final StatusLine status = e.getHttpStatusLine();
        if(null != status) {
            this.append(buffer, String.format("%d %s", status.getStatusCode(), status.getReasonPhrase()));
        }
        switch(e.getHttpStatusCode()) {
            case HttpStatus.SC_BAD_REQUEST:
                return new LoginFailureException(buffer.toString(), e);
        }
        return new DefaultHttpResponseExceptionMappingService().map(new HttpResponseException(e.getHttpStatusCode(), buffer.toString()));
    }
}
