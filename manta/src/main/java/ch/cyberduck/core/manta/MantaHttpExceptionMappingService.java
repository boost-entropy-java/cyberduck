package ch.cyberduck.core.manta;

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

import ch.cyberduck.core.AbstractExceptionMappingService;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.LoginFailureException;
import ch.cyberduck.core.http.DefaultHttpResponseExceptionMappingService;

import org.apache.http.client.HttpResponseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.joyent.manta.exception.MantaClientHttpResponseException;

public class MantaHttpExceptionMappingService extends AbstractExceptionMappingService<MantaClientHttpResponseException> {
    private static final Logger log = LogManager.getLogger(MantaHttpExceptionMappingService.class);

    @Override
    public BackgroundException map(final MantaClientHttpResponseException failure) {
        log.warn("Map failure {}", failure.toString());
        switch(failure.getStatusCode()) {
            case 403:
                final StringBuilder buffer = new StringBuilder();
                this.append(buffer, failure.getStatusMessage());
                return new LoginFailureException(buffer.toString(), failure);
        }
        return new DefaultHttpResponseExceptionMappingService().map(new HttpResponseException(failure.getStatusCode(), failure.getStatusMessage()));
    }
}
