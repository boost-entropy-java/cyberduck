package ch.cyberduck.core.io;

import ch.cyberduck.core.transfer.TransferStatus;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.NullInputStream;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class MD5ChecksumComputeTest {

    @Test
    public void testCompute() throws Exception {
        assertEquals("a43c1b0aa53a0c908810c06ab1ff3967",
            new MD5ChecksumCompute().compute(IOUtils.toInputStream("input", Charset.defaultCharset()), new TransferStatus()).hash);
    }

    @Test
    public void testComputeEmptyString() throws Exception {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e",
            new MD5ChecksumCompute().compute(IOUtils.toInputStream("", Charset.defaultCharset()), new TransferStatus()).hash);
        assertEquals("d41d8cd98f00b204e9800998ecf8427e",
                new MD5ChecksumCompute().compute(new NullInputStream(0L), new TransferStatus().setLength(0)).hash);
    }

    @Test
    public void testNormalize() throws Exception {
        assertEquals("a43c1b0aa53a0c908810c06ab1ff3967",
            new MD5ChecksumCompute().compute(IOUtils.toInputStream("input", Charset.defaultCharset()),
                new TransferStatus()).hash);
        assertEquals("a43c1b0aa53a0c908810c06ab1ff3967",
            new MD5ChecksumCompute().compute(IOUtils.toInputStream("_input", Charset.defaultCharset()),
                    new TransferStatus().setOffset(1)).hash);
        assertEquals("a43c1b0aa53a0c908810c06ab1ff3967",
            new MD5ChecksumCompute().compute(IOUtils.toInputStream("_input_", Charset.defaultCharset()),
                    new TransferStatus().setOffset(1).setLength(5)).hash);
    }
}
