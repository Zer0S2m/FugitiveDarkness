package com.zer0s2m.fugitivedarkness.provider.project;

import java.util.Map;

public abstract class ProjectReaderAdapterAbstract implements ProjectReaderAdapter {

    abstract protected void checkProperties(Map<String, Object> properties)
            throws ProjectMissingPropertiesAdapterException;

}
