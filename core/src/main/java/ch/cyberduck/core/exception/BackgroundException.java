package ch.cyberduck.core.exception;

/*
 * Copyright (c) 2002-2013 David Kocher. All rights reserved.
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

import ch.cyberduck.core.LocaleFactory;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.StringAppender;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class BackgroundException extends Exception {
    private static final long serialVersionUID = -6114495291207129418L;

    private Path file;
    private String message;
    private String detail;

    public BackgroundException() {
        this.message = null;
        this.detail = null;
    }

    public BackgroundException(final Throwable cause) {
        super(cause);
        this.message = null;
        this.detail = cause.getMessage();
    }

    public BackgroundException(final String message, final String detail) {
        this.message = message;
        this.detail = detail;
    }

    public BackgroundException(final String detail, final Throwable cause) {
        super(cause);
        this.message = null;
        this.detail = detail;
    }

    public BackgroundException(final String message, final String detail, final Throwable cause) {
        super(cause);
        this.message = message;
        this.detail = detail;
    }

    public void setMessage(final String title) {
        this.message = title;
    }

    public void setDetail(final String detail) {
        this.detail = detail;
    }

    @Override
    public String getMessage() {
        return null == message ? LocaleFactory.localizedString("Unknown") : message;
    }

    public void setFile(final Path file) {
        this.file = file;
    }

    public BackgroundException withFile(final Path file) {
        this.file = file;
        return this;
    }

    public Path getFile() {
        return file;
    }

    public String getHelp() {
        return StringUtils.EMPTY;
    }

    /**
     * @return Detailed message from the underlying cause.
     */
    public String getDetail() {
        return this.getDetail(true);
    }

    public String getDetail(boolean help) {
        final StringAppender appender = new StringAppender();
        appender.append(detail);
        if(help) {
            appender.append(this.getHelp());
        }
        return appender.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BackgroundException{");
        sb.append("class=").append(this.getClass());
        sb.append(", file=").append(file);
        sb.append(", message='").append(message).append('\'');
        sb.append(", detail='").append(detail).append('\'');
        sb.append(", cause='").append(this.getCause()).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof BackgroundException)) {
            return false;
        }
        BackgroundException that = (BackgroundException) o;
        if(this.getCause() != null ? !this.getCause().equals(that.getCause()) : that.getCause() != null) {
            return false;
        }
        if(!Objects.equals(detail, that.detail)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = detail != null ? detail.hashCode() : 0;
        result = 31 * result + (this.getCause() != null ? this.getCause().hashCode() : 0);
        return result;
    }
}
