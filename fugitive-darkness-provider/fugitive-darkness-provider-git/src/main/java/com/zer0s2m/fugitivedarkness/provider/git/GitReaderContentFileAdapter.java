package com.zer0s2m.fugitivedarkness.provider.git;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * An interface for implementing an adapter to provide services for reading content.
 * <p>Adapters are needed to implement receiving content from different sources for subsequent processing.</p>
 */
public interface GitReaderContentFileAdapter {

    InputStream read(Map<String, Object> properties) throws IOException;

}
